/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Drivetrain;

public class TeleopDrive extends Command {
  Drivetrain dt;
  Joystick leftJoy, rightJoy;

  /**
   * Handles all the teleoperated driving functionality
   * 
   * @param dt the Drivetrain object to use, passing it in is useful for testing
   *           purposes
   */
  public TeleopDrive(Drivetrain dt, Joystick leftJoy, Joystick rightJoy) {
    requires(dt);
    this.dt = dt;
    this.leftJoy = leftJoy;
    this.rightJoy = rightJoy;
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
    double speed = -leftJoy.getY();
    double rot = rightJoy.getX();

    // System.out.println("Speed: " + speed + ", Rotation: " + rot);

    if (SmartDashboard.getBoolean("Square Joysticks", true)) {
      speed = Math.copySign(speed * speed, speed);
      rot = Math.copySign(rot * rot, rot);
    }

    if (SmartDashboard.getBoolean("Slow Left", false)) {
      speed *= SmartDashboard.getNumber("Speed Slow Ratio", 0.5);
    }
    if (SmartDashboard.getBoolean("Slow Right", false)) {
      rot *= SmartDashboard.getNumber("Rotation Slow Ratio", 0.35);
    }

    double left, right;

    // double maxInput = Math.copySign(Math.max(Math.abs(speed), Math.abs(rot)),
    // speed);
    // copySign is returning incorrect signs in operation but not tests
    double maxInput = Math.max(Math.abs(speed), Math.abs(rot));

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
      maxInput *= -1;
      if (rot >= 0.0) {
        left = speed + rot;
        right = maxInput;
      } else {
        left = maxInput;
        right = speed - rot;
      }
    }

    if (SmartDashboard.getBoolean("Characterized Drive", false)) {
      charDrive(left, right);
    } else {
      dt.drive(left, right);
    }
  }

  private void tankDrive() {
    double left = -leftJoy.getY();
    double right = -rightJoy.getY();

    if (SmartDashboard.getBoolean("Square Joysticks", true)) {
      left = Math.copySign(left * left, left);
      right = Math.copySign(right * right, right);
    }

    if (SmartDashboard.getBoolean("Slow Left", false)) {
      left *= SmartDashboard.getNumber("Speed Slow Ratio", 0.5);
    }
    if (SmartDashboard.getBoolean("Slow Right", false)) {
      right *= SmartDashboard.getNumber("Speed Slow Ratio", 0.5);
    }

    if (SmartDashboard.getBoolean("Characterized Drive", false)) {
      charDrive(left, right);
    } else {
      dt.drive(left, right);
    }
  }

  private void charDrive(double left, double right) {
    left *= dt.getMaxSpeed();
    right *= dt.getMaxSpeed();
    double leftV, rightV, maxV;
    dt.setVoltageCompensation(12.0);
    maxV = dt.getMaxVoltage();

    if (left >= 0.0) {
      leftV = dt.calculateVoltage(Drivetrain.Direction.FL, left, 0.0);
    } else {
      leftV = dt.calculateVoltage(Drivetrain.Direction.BL, left, 0.0);
    }

    if (right >= 0.0) {
      rightV = dt.calculateVoltage(Drivetrain.Direction.FR, right, 0.0);
    } else {
      rightV = dt.calculateVoltage(Drivetrain.Direction.BR, right, 0.0);
    }

    if (Math.abs(leftV) >= maxV) {
      leftV = maxV * Math.signum(leftV);
    }
    if (Math.abs(rightV) >= maxV) {
      rightV = maxV * Math.signum(rightV);
    }
    
    dt.drive(leftV / maxV, rightV / maxV);
    dt.disableVoltageCompensation();
  }

  @Override
  protected boolean isFinished() {
    return false;
  }

  @Override
  protected void end() {
    dt.stop();
  }

  @Override
  protected void interrupted() {
    end();
  }
}
