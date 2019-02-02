/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.Cargo;

public class EjectCargo extends Command {
  Timer tim;
  Cargo cargo;

  public EjectCargo(Cargo cargo) {
    requires(cargo);
    this.cargo = cargo;
  }

  @Override
  protected void initialize() {
    tim.reset();
    tim.start();
  }

  @Override
  protected void execute() {
    cargo.runIntake(-1);
    //TODO: Make sure this is correct direction
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
