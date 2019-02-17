/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.subsystems.HatchPanel;

public class ToggleHatch extends InstantCommand {
  private HatchPanel hp;

  public ToggleHatch(HatchPanel hp) {
    super();
    requires(hp);

    this.hp = hp;
  }

  // Called once when the command executes
  @Override
  protected void initialize() {
    hp.toggle();
  }

}
