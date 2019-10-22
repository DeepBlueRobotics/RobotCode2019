/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import frc.robot.lib.Limelight;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AllignAssist extends Command {
  private Drivetrain dt;
  private Limelight.Mode limelightMode;

  public AllignAssist(Drivetrain dt, Limelight.Mode mode) {
    this.dt = dt;
    this.limelightMode = mode;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
      if (limelightMode == Limelight.Mode.DIST) {

      }
      else if (limelightMode == Limelight.Mode.STEER) {

      } else {

      }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
      if (!SmartDashboard.getBoolean("Use Limelight", false)) {
        return true;
      } else {
        return false;
      }
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
