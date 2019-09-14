/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.subsystems.Lights;

public class SetLight extends InstantCommand {
  private Lights lights;
  private Lights.LightState state;

  public SetLight(Lights lights, Lights.LightState state) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    this.lights = lights;
    requires(lights);
    this.state = state;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    lights.setLights(state);
  }

}
