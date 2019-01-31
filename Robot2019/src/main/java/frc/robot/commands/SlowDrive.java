/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SlowDrive extends Command {
  public enum Side {
    LEFT, RIGHT
  }

  String sdValue;

  /**
   * Reduces the joystick input values while this command is running
   * 
   * @param side the joystick to reduce
   */
  public SlowDrive(Side side) {
    sdValue = (side == Side.LEFT ? "Slow Left" : "Slow Right");
  }

  @Override
  protected void initialize() {
    SmartDashboard.putBoolean(sdValue, true);
  }

  @Override
  protected boolean isFinished() {
    return false;
  }

  @Override
  protected void end() {
    SmartDashboard.putBoolean(sdValue, false);
  }

  @Override
  protected void interrupted() {
    end();
  }
}
