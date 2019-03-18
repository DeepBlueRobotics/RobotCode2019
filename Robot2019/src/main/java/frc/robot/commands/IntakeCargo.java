/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Cargo;
import frc.robot.subsystems.Lights;
import frc.robot.commands.SetLight;
public class IntakeCargo extends Command {
  Timer timer;
  Cargo cargo;
  Lights lights;
  boolean overdraw;

  public IntakeCargo(Cargo cargo, Lights lights) {
    requires(cargo);
    this.cargo = cargo;
    this.lights = lights;
    timer = new Timer();
    overdraw = false;
  }

  @Override
  protected void initialize() {
    timer.reset();
    SetLight signalCargo = new SetLight(lights, Lights.LightState.CARGO);
  }

  @Override
  protected void execute() {
    cargo.runIntake();
    if (cargo.hasCargo()) {
      if (!overdraw) {
        overdraw = true;
        timer.start();
      }
    } else {
      overdraw = false;
      timer.stop();
      timer.reset();
    }
  }

  @Override
  protected boolean isFinished() {
    return (timer.get() > 0.5);
  }

  @Override
  protected void end() {
    if (isFinished()) {
      cargo.keepIntake();
      SmartDashboard.putBoolean("Has cargo", true);
    } else {
      cargo.stopIntake();
    }
  }

  @Override
  protected void interrupted() {
    end();
  }
}
