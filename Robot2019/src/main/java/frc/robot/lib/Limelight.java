/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.lib;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Limelight {
  public enum Mode {
    DIST, STEER, TARGET
  }

  /* http://docs.limelightvision.io/en/latest/networktables_api.html
  tv = Whether the limelight has any valid targets (0 or 1)
  tx = Horizontal Offset From Crosshair To Target (-27 degrees to 27 degrees)
  ty = Vertical Offset From Crosshair To Target (-20.5 degrees to 20.5 degrees)
  ta = Target Area (0% of image to 100% of image)
  There are more values we could be using. Check the documentation.
  */
  private double tv, tx, ty, ta;
  private boolean stopSteer = false;
  private double mounting_angle;
  private double prev_tx = 1.0;
  
  // Parameters for vision using linear algebra. 
  private double[][] rotMat = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
  private double[] translateVec = {0, 0, 0};
  private double[] defaultValue = {0, 0, 0, 0};

  public Limelight() {
    SmartDashboard.putNumber("Area Threshold", 0.02);
  }

  // For the shooter. Given what the limelight sees and the shooter angle, compute the desired initial speed for the shooter.
  public double computeSpeed(double angle, double cameraHeight, double objectHeight) {
    double distance = determineObjectDist(cameraHeight, objectHeight);
    return Math.sqrt((16.1 * Math.pow(distance, 2)) / (distance * Math.tan(angle) - cameraHeight - objectHeight)) / Math.cos(angle);
  }

  /* Determine the mounting angle of the camera given a vision target and its known distance, height off of the ground,
   and the height of the camera off of the ground. */
  public void determineMountingAngle(double distance, double cameraHeight, double objectHeight) {
    // NOTE: ty may be negative.
    ty = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0.0);
    mounting_angle = Math.atan((cameraHeight - objectHeight) / distance) - ty;
  }

  // Determine the distance an object is from the limelight given the camera's height off of the ground and the object's height off of the ground.
  public double determineObjectDist(double cameraHeight, double objectHeight) {
    ty = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0.0);
    return (objectHeight - cameraHeight) / (Math.tan(mounting_angle + ty));
  }

  /* Given what is currently seen, determine the entries rotMat and translateVec parameters
    by solving a system of equations using Gaussian-elimination */
  public void computeParams(double[] worldXs, double[] worldYs, double[] worldZs) {
    double[] cornerXs = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tcornx").getDoubleArray(defaultValue);
    double[] cornerYs = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tcorny").getDoubleArray(defaultValue);
    double[][] corners = {cornerXs, cornerYs, {1, 1, 1, 1}};

    for (int i = 0; i < 3; i++) {
      // Set up the matrix
      double[][] matrix = new double[4][5];
      for (int row = 0; row < 4; row++) {
        matrix[row][0] = worldXs[row];
        matrix[row][1] = worldYs[row];
        matrix[row][2] = worldZs[row];
        matrix[row][3] = 1;
        matrix[row][4] = corners[i][row];
      }

      /* Row reduce and find solutions; assumed that echelon is of the form [I | x] 
         where I is the identity matrix and x are the solutions. This has not been tested yet. */
      double[][] echelon = Gaussian(matrix);
      rotMat[i][0] = echelon[0][4];
      rotMat[i][1] = echelon[1][4];
      rotMat[i][2] = echelon[2][4];
      translateVec[i] = echelon[3][4];
    }
  }

  // Adjusts the distance between a vision target and the robot. Uses basic PID with the ty value from the network table.
  public double distanceAssist() {
    tv = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0.0);
    ta = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0.0);
    SmartDashboard.putNumber("Crosshair Vertical Offset", ty);
    double adjustment = 0.0;
    double area_threshold = 1.75;
    double Kp = 0.225;

    if (tv == 1.0) {
      adjustment = (area_threshold - ta) * Kp;
    }
    adjustment = Math.signum(adjustment) * Math.min(Math.abs(adjustment), 0.5);
    return adjustment;
  }

  // Adjusts the angle facing a vision target. Uses basic PID with the tx value from the network table.
  public double steeringAssist() {
    tv = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0.0);
    tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0.0);
    ta = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0.0);
    SmartDashboard.putNumber("Crosshair Horizontal Offset", tx);
    SmartDashboard.putNumber("Found Vision Target", tv);
    SmartDashboard.putNumber("Prev_tx", prev_tx);
    double adjustment = 0.0;
    double steering_factor = 0.25;
    double Kp = 0.015;

    if (tv == 1.0 && !stopSteer) {
      if (ta > SmartDashboard.getNumber("Area Threshold", 0.02)) {
        adjustment += Kp * tx;
        prev_tx = tx;
      }
    } else {
      adjustment += Math.signum(prev_tx) * steering_factor;
    }

    if (Math.abs(tx) < 1.0) {
      stopSteer = true;
    } else {
      stopSteer = false;
    }
    SmartDashboard.putBoolean("Stop Auto Steering", stopSteer);

    adjustment = Math.signum(adjustment) * Math.min(Math.abs(adjustment), 0.5);
    SmartDashboard.putNumber("Adjustment", adjustment);
    return adjustment;
  }

  // Combination of distance assist and steering assist
  public double[] autoTarget() {
    double dist_assist = distanceAssist();
    double steer_assist = steeringAssist();
    double[] params = {dist_assist + steer_assist, dist_assist - steer_assist};
    return params;
  }

  /* Given a desired straight-line distance targetDist away from the vision target, determine the distance 
    in order to face the target from head-on. Returns the required distance at the current heading.
  */
  public double[] determineDist(double targetDist) {
    // Get the x and y coordinates of the corners of the bounding box.
    double[] cornerXs = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tcornx").getDoubleArray(defaultValue);
    double[] cornerYs = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tcorny").getDoubleArray(defaultValue);
    
    // Average the corners to get the center of the vision target as viewed by the camera.
    double xSum = 0.0;
    double ySum = 0.0;
    for (int i = 0; i < 4; i += 1) {
      xSum += cornerXs[i];
      ySum += cornerYs[i];
    }
    double[] cameraPos = {xSum / 4, ySum / 4, 1};

    // Calculate the position of the center in 3D space.
    double[] worldPos = dot(transpose(rotMat), difference(cameraPos, translateVec));
    double x = worldPos[0];
    double y = worldPos[1];

    // Use trigonometry to find the required distance.
    double r = Math.sqrt(x * x + y * y);
    double a1 = Math.atan2(y, x);
    double a3 = Math.asin(r * Math.sin(a1) / targetDist);
    double a2 = Math.PI - a1 - a3;
    double[] params = {r * Math.sin(a2) / Math.sin(a3), a3};
    return params;
  }

  // Returns the transpose of a matrix.
  private double[][] transpose(double[][] matrix) {
    int n = matrix.length;
    int m = matrix[0].length;
    double[][] matrixTranspose = new double[m][n];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        matrixTranspose[m][n] = matrix[n][m];
      }
    }
    return matrixTranspose;
  }

  // Returns matrix-vector product.
  private double[] dot(double[][] matrix, double[] vector) {
    int n = matrix.length;
    int m = matrix[0].length;
    assert m == vector.length;
    double[] dotVector = new double[n];
    for (int i = 0; i < n; i++) {
      dotVector[i] = dot(matrix[i], vector);
    }
    return dotVector;
  }

  // Returns x * yT.
  private double dot(double[] x, double[] y) {
    int n = x.length;
    assert x.length == y.length;
    double dot = 0.0;
    for (int i = 0; i < n; i++) { dot += x[i] * y[i]; }
    return dot;
  }

  // Returns the difference of two vectors.
  private double[] difference(double[] x, double[] y) {
    double[] diff = new double[x.length];
    for (int i = 0; i < x.length; i++) { diff[i] = x[i] - y[i]; }
    return diff;
  }

  // Returns the reduced row-echelon form of a matrix.
  private double[][] Gaussian(double[][] matrix) {
    int n = matrix.length;
    int m = matrix[0].length;
    double[][] echelon = new double[n][];
    for(int i = 0; i < matrix.length; i++) {
      echelon[i] = matrix[i].clone();
    }

    while (!isEchelon(echelon)) {
      for (int i = 0; i < Math.min(n, m); i++) {
        double pivot = matrix[i][i];
        if (pivot != 0) {
          if (pivot != 1) {
            for (int j = 0; j < m; j++) { echelon[i][j] /= pivot; }
          }
          for (int rowNum = 0; rowNum < n; rowNum++) {
            for (int j = 0; j < m; j++) { echelon[rowNum][j] -= echelon[rowNum][i] * echelon[i][j]; }
          }
        }
      }
    }
    return echelon;
  }

  // Tests whether a matrix is in reduced-row-echelon form
  private boolean isEchelon(double[][] matrix) {
    int n = matrix.length;
    int m = matrix[0].length;
    double[][] matrixTranspose = transpose(matrix);
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        double val = matrix[i][j];
        if (Math.abs(val) > 0) {
          int non_zero_count = 0;
          for (int k = 0; k < n; k++) {
            if (matrixTranspose[j][k] != 0) { non_zero_count += 1; }
          }
          if (non_zero_count != 1){ return false; }
        }
      }
    }
    return true;
  }
}
