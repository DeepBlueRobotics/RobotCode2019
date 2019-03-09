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

  /**
   * Toggles the eject pistons of the hatch panel mechanism
   */
  public EjectHatch(HatchPanel hp) {
    requires(hp);
    this.hp = hp;

    timey = new Timer();

    if (!SmartDashboard.containsKey("Hatch Eject Delay")) {
      SmartDashboard.putNumber("Hatch Eject Delay", 0.5);
    }
  }

  @Override
  protected void initialize() {
    // If the pistons are currently extend them, retract them back, otherwise release and then eject
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

  @Override
  protected boolean isFinished() {
    // if it's ejecting, wait until end of delay to extend pistons after release
    return !ejecting || (timey.get() > SmartDashboard.getNumber("Hatch Eject Delay", 0.5));
  }

  @Override
  protected void end() {
    if (ejecting) {
      hp.eject();
    }
  }

  @Override
  protected void execute() {}

  @Override
  protected void interrupted() {}
}
