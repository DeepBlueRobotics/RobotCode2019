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
  private NetworkTableInstance inst;
  private NetworkTable table;
  private double tv, tx, ty, ta;

  public Limelight() {
    inst = NetworkTableInstance.getDefault();
    table = inst.getTable("limelight");
  }

  // Adjusts the distance between a vision target and the robot. Uses basic PID with the ty value from the network table.
  public double distanceAssist() {
    ty = table.getEntry("ty").getDouble(0.0);
    SmartDashboard.putNumber("Crosshair Vertical Offset", ty);
    ta = table.getEntry("ta").getDouble(0.0);
    double adjustment = 0.0;
    double area_threshold = 0.5;  // TODO: Set the desired area.
    double Kp = 0.2;   // TODO: Set PID K value.

    if (ta < area_threshold) {
      adjustment += Kp * ty;
    }
    return adjustment;
  }

  // Adjusts the angle facing a vision target. Uses basic PID with the tx value from the network table.
  public double steeringAssist() {
    tv = table.getEntry("tv").getDouble(0.0);
    tx = -table.getEntry("tx").getDouble(0.0);
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
