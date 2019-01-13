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
  Joystick leftJoy, rightJoy;
  double forward = 0.0;
  double rotation = 0.0;

  public TeleopDrive(Drivetrain dt) {
    requires(dt);
    this.dt = dt;
    leftJoy = dt.oi.leftJoy;
    rightJoy = dt.oi.rightJoy;
  }

  @Override
  protected void initialize() {
  }

  @Override
  protected void execute() {
    forward = leftJoy.getY();
    rotation = rightJoy.getY();

    // NOTE: The problem with this code is that it relies on the driver
    //        to not touch the right joystick if we want the robot to go forward
    if (rotation == 0.0) {
      dt.TeleopDrive(forward, forward);
    }
    else if (rotation > 0.0) {
      dt.TeleopDrive(forward, 0.0);
    }
    else if (rotation < 0.0) {
      dt.TeleopDrive(0.0, forward);
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
