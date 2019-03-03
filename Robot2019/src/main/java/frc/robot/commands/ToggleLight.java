/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.subsystems.Lights;

/**
 * Add your docs here.
 */
public class ToggleLight extends InstantCommand {
  /**
   * Add your docs here.
   */
  private Lights lights;

  public ToggleLight(Lights lights) {
    super();
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    this.lights = lights;
  }

  // Called once when the command executes
  @Override
  protected void initialize() {
    lights.toggleLights();
  }

}
