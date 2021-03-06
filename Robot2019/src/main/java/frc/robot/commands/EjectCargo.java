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
public class EjectCargo extends Command {

  final double ejectDuration = 0.5; // seconds

  Timer timer;
  Cargo cargo;

  public EjectCargo(Cargo cargo) {
    requires(cargo);
    this.cargo = cargo;
    timer = new Timer();
  }

  @Override
  protected void initialize() {
    timer.reset();
    timer.start();
  }

  @Override
  protected void execute() {
    cargo.runEject();
  }

  @Override
  protected boolean isFinished() {
    return (timer.get() > ejectDuration);
  }

  @Override
  protected void end() {
    cargo.stopIntake();
    if (isFinished()) {
      SmartDashboard.putBoolean("Has cargo", false);
    }
  }

  @Override
  protected void interrupted() {
    end();
  }
}
