/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.subsystems.HatchPanel;

public class IntakeHatch extends InstantCommand {
  private HatchPanel hp;

  /**
   * Toggles the grabbing piston of the hatch mechanism
   */
  public IntakeHatch(HatchPanel hp) {
    requires(hp);
    this.hp = hp;
  }

  @Override
  protected void initialize() {
    switch(hp.state) {
      case EJECTING:
      case DEFAULT:
        hp.grab();
        break;
      case GRABBING:
        hp.reset();
        break;
    }
  }
}
