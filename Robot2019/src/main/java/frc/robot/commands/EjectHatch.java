/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.subsystems.HatchPanel;

public class EjectHatch extends InstantCommand {
  private HatchPanel hp;

  /**
   * Toggles the eject pistons of the hatch panel mechanism
   */
  public EjectHatch(HatchPanel hp) {
    requires(hp);
    this.hp = hp;
  }

  @Override
  protected void initialize() {
    // If the pistons are currently extend them, retract them back, otherwise release and then eject
    if (hp.state == HatchPanel.State.EJECTING) {
      hp.reset();
    } else {
      hp.reset();
      hp.eject();
    }
  }
}
