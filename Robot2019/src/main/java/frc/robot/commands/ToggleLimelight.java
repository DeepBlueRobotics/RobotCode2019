/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ToggleLimelight extends InstantCommand {
  public ToggleLimelight() {
  }

  @Override
  protected void initialize() {
    if (SmartDashboard.getBoolean("Using Limelight", false)) {
        SmartDashboard.putBoolean("Using Limelight", false);
    } else {
        SmartDashboard.putBoolean("Using Limelight", true);
    }
  }
}
