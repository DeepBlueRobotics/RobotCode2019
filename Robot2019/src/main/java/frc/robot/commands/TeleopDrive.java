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

  double prevSpeed = 0, prevLeft = 0, prevRight = 0;

  double outreachSpeed = 0.3;

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

    if (!SmartDashboard.containsKey("Gradual Drive Max dV")) {
      SmartDashboard.putNumber("Gradual Drive Max dV", 0.04); // between zero (no movement) to 2 (any movement)
    }

    if (!SmartDashboard.containsKey("Characterized Drive")) {
      SmartDashboard.putBoolean("Characterized Drive", false);
    }

    if (!SmartDashboard.containsKey("Gradual Drive")) {
      SmartDashboard.putBoolean("Gradual Drive", true);
    }
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
    SmartDashboard.putNumber("input speed", speed);
    SmartDashboard.putNumber("input rot", rot);

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

    if (SmartDashboard.getBoolean("Gradual Drive", true)) {
      double dV = SmartDashboard.getNumber("Gradual Drive Max dV", 0.04);
      if (Math.abs(speed - prevSpeed) > dV) {
        speed = prevSpeed + dV * Math.signum(speed - prevSpeed);
      }
    }
    prevSpeed = speed;

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
      SmartDashboard.putBoolean("is in char drive", true);
      charDrive(left, right);
    } else {
      SmartDashboard.putBoolean("is in char drive", false);
      if (SmartDashboard.getBoolean("Outreach Mode", false)) {
        dt.drive(left * outreachSpeed, right * outreachSpeed);
      } else {
        dt.drive(left, right);
      }
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

    if (SmartDashboard.getBoolean("Gradual Drive", true)) {
      double dV = SmartDashboard.getNumber("Gradual Drive Max dV", 0.04);
      if (Math.abs(left - prevLeft) > dV) {
        left = prevLeft + dV * Math.signum(left - prevLeft);
      }
      if (Math.abs(right - prevRight) > dV) {
        right = prevRight + dV * Math.signum(right - prevRight);
      }
    }
    prevLeft = left;
    prevRight = right;

    if (SmartDashboard.getBoolean("Characterized Drive", false)) {
      charDrive(left, right);
    } else {
      if (SmartDashboard.getBoolean("Outreach Mode", false)) {
        dt.drive(left * outreachSpeed, right * outreachSpeed);
      } else {
        dt.drive(left, right);
      }
    }
  }

  private void charDrive(double left, double right) {
    double leftV, rightV;
    dt.setVoltageCompensation(12.0);
    double maxV = dt.getMaxVoltage();

    double DT = 1 / 4.0;
    double actualLeftVel = dt.getEncRate(Drivetrain.Side.LEFT);
    double actualRightVel = dt.getEncRate(Drivetrain.Side.RIGHT);
    double actualAvgVel = 0.5 * (actualLeftVel + actualRightVel);

    double desiredLeftVel = left * dt.getMaxSpeed();
    double desiredRightVel = right * dt.getMaxSpeed();
    double desiredAvgVel = 0.5 * (desiredLeftVel + desiredRightVel);

    double leftDV = desiredLeftVel - actualLeftVel;
    double rightDV = desiredRightVel - actualRightVel;
    double avgDV = 0.5 * (desiredAvgVel - actualAvgVel);

    double leftA = leftDV / DT;
    double rightA = rightDV / DT;
    double avgA = avgDV / DT;

    SmartDashboard.putNumber("Left Speed", actualLeftVel);
    SmartDashboard.putNumber("Right Speed", actualRightVel);
    //System.out.println("Left Speed: " + actualLeftVel + ", Right Speed: " + actualRightVel);
    double maxAccel = SmartDashboard.getNumber("Max Acceleration", dt.getMaxSpeed() / 1.0);

    if (Math.abs(avgA) >= maxAccel) { // dt.getMaxSpeed() is a temporary value. The actual value will be determined
                                     // through experimentation
      leftA = Math.signum(leftA) * Math.abs(leftA / avgA) * maxAccel;
    }
    if (Math.abs(avgA) >= maxAccel) {
      rightA = Math.signum(rightA) * Math.abs(rightA / avgA) * maxAccel;
    }

    //System.out.println("Left Accel: " + leftA + ", Right Accel: " + rightA);

    if (left >= 0.0) {
      leftV = dt.calculateVoltage(Drivetrain.Direction.FL, actualLeftVel, leftA);
    } else {
      leftV = dt.calculateVoltage(Drivetrain.Direction.BL, actualLeftVel, leftA);
    }

    if (right >= 0.0) {
      rightV = dt.calculateVoltage(Drivetrain.Direction.FR, actualRightVel, rightA);
    } else {
      rightV = dt.calculateVoltage(Drivetrain.Direction.BR, actualRightVel, rightA);
    }

    if (Math.abs(leftV) >= maxV) {
      leftV = maxV * Math.signum(leftV);
    }
    if (Math.abs(rightV) >= maxV) {
      rightV = maxV * Math.signum(rightV);
    }

    SmartDashboard.putNumber("Left Volts", leftV);
    SmartDashboard.putNumber("Right Volts", rightV);
    //System.out.println("LeftV: " + leftV + ", RightV: " + rightV);

    if (SmartDashboard.getBoolean("Outreach Mode", false)) {
      dt.drive(leftV / maxV * outreachSpeed, rightV / maxV * outreachSpeed);
    } else {
      dt.drive(leftV / maxV, rightV / maxV);
    }
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
