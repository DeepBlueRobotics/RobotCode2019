/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.Drivetrain;

public class PIDDrive extends Command {
  Drivetrain dt;

  public PIDDrive(Drivetrain dt, double leftTarget, double rightTarget) {
      requires(dt);
      this.dt = dt;
      dt.setPIDTarget("left", leftTarget);
      dt.setPIDTarget("right", rightTarget);
  }

  @Override
  protected void initialize() {
      dt.enablePID();
  }

  @Override
  protected void execute() {
      double leftVelocity = dt.returnPIDOutput("left");
      double rightVelocity = dt.returnPIDOutput("right");
      double leftAcceleration = (leftVelocity - dt.prevLeftVel) / 0.5;
      double rightAcceleration = (rightVelocity - dt.prevRightVel) / 0.5;
      double leftVoltage = dt.getExpectedVoltage(leftVelocity, leftAcceleration);
      double rightVoltage = dt.getExpectedVoltage(rightVelocity, rightAcceleration);

      if (leftVoltage > 12.0) { leftVoltage = 12.0; }
      if (rightVoltage > 12.0) { rightVoltage = 12.0; }

      dt.drive(leftVoltage / 12.0, rightVoltage / 12.0);
      dt.prevLeftVel = leftVelocity;
      dt.prevRightVel = rightVelocity;
  }

  @Override
  protected boolean isFinished() {
    return dt.isOnTarget("left") && dt.isOnTarget("right");
  }

  @Override
  protected void end() {
      dt.disablePID();
  }

  @Override
  protected void interrupted() {
    end();
  }
}
