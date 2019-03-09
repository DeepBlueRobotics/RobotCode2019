/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.HatchPanel;

public class EjectHatch extends Command {
  private HatchPanel hp;
  private Timer timey;
  private boolean ejecting;

  public EjectHatch(HatchPanel hp) {
    requires(hp);
    this.hp = hp;

    timey = new Timer();

    if (!SmartDashboard.containsKey("Hatch Eject Delay")) {
      SmartDashboard.putNumber("Hatch Eject Delay", 0.5);
    }
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    if (hp.state == HatchPanel.State.EJECTING) {
      hp.reset();
      ejecting = false;
    } else {
      hp.reset();
      ejecting = true;

      timey.reset();
      timey.start();
    }
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return !ejecting || (timey.get() > SmartDashboard.getNumber("Hatch Eject Delay", 0.5));
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    hp.eject();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
