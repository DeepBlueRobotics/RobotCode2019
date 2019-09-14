/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Add your docs here.
 */
public class SlowClimb extends Command {
  /**
   * Add your docs here.
   */
  public SlowClimb() {
    super();
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  // Called once when the command executes
  @Override
  protected void initialize() {
    SmartDashboard.putBoolean("Slow Climb", true);
  }

  @Override
  protected void end() {
    SmartDashboard.putBoolean("Slow Climb", false);
  }

  @Override
  protected void interrupted() {
    end();
  }
  
  @Override
  protected boolean isFinished() { return false; }
}
