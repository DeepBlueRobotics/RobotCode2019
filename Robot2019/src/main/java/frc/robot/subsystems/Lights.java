/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.SetLight;

/**
 * Add your docs here.
 */
public class Lights extends Subsystem {

  public enum LightState {
    OFF,
    CARGO,
    HATCH,
    CLIMBER
  }
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private Relay lights;
  private LightState lightsState;

  /**
   * 0 is off 0.5 is orange 1 is yellow
   */

  public Lights(Relay lights) {
    this.lights = lights;
    lightsState = LightState.HATCH;
  }

  public void setLights(LightState newState) {
    String color = "";
    switch (newState) {
      case OFF:
      lights.set(Relay.Value.kOff);
      color = "None";
      break;
      case CARGO:
      lights.set(Relay.Value.kForward);
      color = "Orange";
      break;
      case HATCH:
      lights.set(Relay.Value.kReverse);
      color = "Blue";
      break;
      case CLIMBER:
      lights.set(Relay.Value.kOn);
      color = "Rainbow";
      break;
    }
    lightsState = newState;
    SmartDashboard.putString("Lights Current Color", color);
  }

  public void toggleLights() {
    if (lightsState == LightState.OFF) {
      lightsState = LightState.CARGO;
    } else if (lightsState == LightState.CARGO) {
      lightsState = LightState.HATCH;
    } else if (lightsState == LightState.HATCH) {
      lightsState = LightState.CLIMBER;
    }
    else {
      lightsState = LightState.OFF;
    }
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new SetLight(this, LightState.HATCH));
  }
}
