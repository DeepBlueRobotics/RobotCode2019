/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.subsystems.HatchPanel;

public class SetHatch extends InstantCommand {
  private HatchPanel hp;
  private String state;

  public SetHatch(HatchPanel hp, String state) {
    super();
    requires(hp);
    this.state = state;
    this.hp = hp;
  }

  // Called once when the command executes
  @Override
  protected void initialize() {
    if ("IN".equals(state)) {
      hp.setIn();
      return;
    }
    if ("OUT".equals(state)) {
      hp.setOut();
      return;
    }
    System.out.println("Failed: bug in SetHatch");
  }

}
