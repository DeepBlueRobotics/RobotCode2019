/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Drivetrain;

/**
 * Handles all the teleoperated driving functionality
 */
public class TeleopDrive extends Command {
  Drivetrain dt;

  public TeleopDrive(Drivetrain dt) {
    requires(dt);
    this.dt = dt;
  }

  @Override
  protected void execute() {
    if (SmartDashboard.getBoolean("Arcade Drive", true)) {
      arcadeDrive();
    } else {
      tankDrive();
    }
  }

  private void arcadeDrive() {
    double speed = dt.leftJoy.getY();
    double rot = dt.rightJoy.getX();
    if (SmartDashboard.getBoolean("Slow Left", false)) {
      speed *= SmartDashboard.getNumber("Speed Slow Ratio", 0.5);
    }
    if (SmartDashboard.getBoolean("Slow Left", false)) {
      rot *= SmartDashboard.getNumber("Rotation Slow Ratio", 0.5);
    }

    double left, right;

    double maxInput = Math.copySign(Math.max(Math.abs(speed), Math.abs(rot)), speed);

    if (speed >= 0.0) {
      // First quadrant, else second quadrant
      if (rot >= 0.0) {
        left = maxInput;
        right = speed - rot;
      } else {
        left = speed + rot;
        right = maxInput;
      }
    } else {
      // Third quadrant, else fourth quadrant
      if (rot >= 0.0) {
        left = speed + rot;
        right = maxInput;
      } else {
        left = maxInput;
        right = speed - rot;
      }
    }

    if (SmartDashboard.getBoolean("Square Joysticks", true)) {
      left = Math.signum(left * left);
      right = Math.signum(right * right);
    }

    dt.drive(left, right);
  }

  private void tankDrive() {
    double left = dt.leftJoy.getY();
    double right = dt.rightJoy.getY();
    if (SmartDashboard.getBoolean("Slow Left", false)) {
      left *= SmartDashboard.getNumber("Speed Slow Ratio", 0.5);
    }
    if (SmartDashboard.getBoolean("Slow Right", false)) {
      right *= SmartDashboard.getNumber("Speed Slow Ratio", 0.5);
    }

    if (SmartDashboard.getBoolean("Square Joysticks", true)) {
      left = Math.signum(left * left);
      right = Math.signum(right * right);
    }

    dt.drive(left, right);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    dt.stop();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
