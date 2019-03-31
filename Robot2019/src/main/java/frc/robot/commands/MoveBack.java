/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.Drivetrain;

public class MoveBack extends Command {
  Timer tim;
  Drivetrain dt;
  double time;
  public MoveBack(Drivetrain dt, double time) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(dt);
    this.dt = dt;
    this.time = time;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    tim = new Timer();
    tim.reset();
    tim.start();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    dt.drive(-1, -1);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return tim.get() >= time;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
