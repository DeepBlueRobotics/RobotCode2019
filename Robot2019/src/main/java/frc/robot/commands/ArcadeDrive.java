/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.Drivetrain;

public class ArcadeDrive extends Command {
  Drivetrain dt;

  public ArcadeDrive(Drivetrain dt) {
    requires(dt);
    this.dt = dt;
  }

  @Override
  protected void initialize() {
  }

  @Override
  protected void execute() {
    double lvalue, rvalue, val1, val2;
    lvalue = dt.leftJoy.getY();
    rvalue = dt.rightJoy.getX();
    double maxInput = Math.copySign(Math.max(Math.abs(lvalue), Math.abs(rvalue)), lvalue);

    if (lvalue >= 0.0) {    // Forward
        if (rvalue >= 0.0) {  // Right
            val1 = maxInput;
            val2 = lvalue - rvalue;
        } else {              // Left
            val1 = lvalue + rvalue;
            val2 = maxInput;
        }
        } else {                // Backward
        if (rvalue >= 0.0) {  // Right
            val1 = lvalue + rvalue;
            val2 = maxInput;
        } else {              // Left
            val1 = maxInput;
            val2 = lvalue - rvalue;
        }
    }
    dt.TeleopDrive(val1, val2);
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
