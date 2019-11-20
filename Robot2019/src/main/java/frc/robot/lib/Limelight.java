/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.lib;

import edu.wpi.first.networktables.NetworkTable;
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

  // Adjusts the distance between a vision target and the robot. Uses basic PID with the ty value from the network table.
  public double distanceAssist() {
    tv = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0.0);
    ta = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0.0);
    SmartDashboard.putNumber("Crosshair Vertical Offset", ty);
    double adjustment = 0.0;
    double area_threshold = 10;  // TODO: Set the desired area ratio. 0 to 100.
    double Kp = 0.2;   // TODO: Set PID K value.

    if (tv == 1.0) {
      adjustment = (area_threshold-ta)*Kp;
    }
    return adjustment;
  }

  // Adjusts the angle facing a vision target. Uses basic PID with the tx value from the network table.
  public double steeringAssist() {
    tv = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0.0);
    tx = -NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0.0);
    SmartDashboard.putBoolean("Found Vision Target", tv == 1.0);
    SmartDashboard.putNumber("Crosshair Horizontal Offset", tx);
    double adjustment = 0.0;
    double steering_factor = 0.25;
    double Kp = 0.2;   // TODO: Set PID K value.

    if (tv == 1.0) {
      adjustment += Kp * tx;
    } else {
      adjustment += steering_factor;
    }
    return adjustment;
  }

  // Combination of distance assist and steering assist
  public double[] autoTarget() {
    double dist_assist = distanceAssist();
    double steer_assist = steeringAssist();
    double[] params = {dist_assist + steer_assist, dist_assist - steer_assist};
    return params;
  }
}
