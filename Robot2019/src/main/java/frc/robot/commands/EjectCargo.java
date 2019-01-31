/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.CargoIntakeEject;

public class EjectCargo extends Command {
  Timer tim;
  CargoIntakeEject cargo;

  public EjectCargo(CargoIntakeEject cargo) {
    requires(cargo);
    this.cargo = cargo;
  }

  @Override
  protected void initialize() {
  }

  @Override
  protected void execute() {
    cargo.runIntake(-1.0);
    if (!cargo.hasCargo()) {
        tim.start();
    } else {
        tim.stop();
        tim.reset();
    }
  }

  @Override
  protected boolean isFinished() {
    return (tim.get() > 0.5);
  }

  @Override
  protected void end() {
    cargo.stopIntake();
  }

  @Override
  protected void interrupted() {
    end();
  }
}
