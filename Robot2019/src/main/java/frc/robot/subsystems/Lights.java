/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.commands.SetLight;

/**
 * Add your docs here.
 */
public class Lights extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private VictorSP lights;
  private double lightsValue = 0;

  /**
   * 0 is off 0.5 is orange 1 is yellow
   */

  public Lights(VictorSP lights) {
    this.lights = lights;
  }

  public void setLights() {
    lights.set(lightsValue);
  }

  public void toggleLights() {
    if (lightsValue == 0) {
      lightsValue = 0.5;
    } else if (lightsValue == 0.5) {
      lightsValue = 1;
    } else {
      lightsValue = 0;
    }
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new SetLight(this));
  }
}
