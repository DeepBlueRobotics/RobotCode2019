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

public class IntakeCargo extends Command {
  Timer timer;
  Cargo cargo;
  boolean overdraw;

  public IntakeCargo(Cargo cargo) {
    requires(cargo);
    this.cargo = cargo;
    timer = new Timer();
    overdraw = false;
  }

  @Override
  protected void initialize() {
    timer.reset();
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
    cargo.stopIntake();
    SmartDashboard.putBoolean("Has cargo", true);
  }

  @Override
  protected void interrupted() {
    end();
  }
}
