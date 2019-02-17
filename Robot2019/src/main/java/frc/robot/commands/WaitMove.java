/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Drivetrain.Side;

public class WaitMove extends Command {
  Drivetrain dt;
  double startl;
  double startr;

  private final double reqDist = 2; // TODO: Test value

  public WaitMove(Drivetrain dt) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    this.dt = dt;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    startr = dt.getEncDist(Side.RIGHT);
    startl = dt.getEncDist(Side.LEFT);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return Math.abs(dt.getEncDist(Side.RIGHT) - startr) > reqDist
        && Math.abs(dt.getEncDist(Side.LEFT) - startl) > reqDist;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
