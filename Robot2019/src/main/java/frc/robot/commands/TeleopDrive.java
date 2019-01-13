/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.Drivetrain;

public class TeleopDrive extends Command {
  Drivetrain dt;

  public TeleopDrive(Drivetrain dt) {
    requires(dt);
    this.dt = dt;
  }

  @Override
  protected void initialize() {
  }

  @Override
  protected void execute() {
    double lvalue, rvalue, val1, val2;
    if (dt.drive_mode.equals("arcade")) {
      lvalue = dt.leftJoy.getY();
      rvalue = dt.rightJoy.getX();

      if (lvalue >= 0.0) {    // Forward
        if (rvalue >= 0.0) {  // Right
          val1 = lvalue;
          val2 = lvalue - rvalue;
        } else {              // Left
          val1 = lvalue + rvalue;
          val2 = lvalue;
        }
      } else {                // Backward
        if (rvalue >= 0.0) {  // Right
          val1 = lvalue + rvalue;
          val2 = lvalue;
        } else {              // Left
          val1 = lvalue;
          val2 = lvalue - rvalue;
        }
      }
      dt.TeleopDrive(val1, val2);
    } else if (dt.drive_mode.equals("tank")) {
      val1 = dt.leftJoy.getY();
      val2 = dt.rightJoy.getY();
      dt.TeleopDrive(val1, val2);   
    }
  }

  @Override
  protected boolean isFinished() {
    return false;
  }

  @Override
  protected void end() {
  }

  @Override
  protected void interrupted() {
    end();
  }
}
