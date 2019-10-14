/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Lift;
import frc.robot.subsystems.Lights;

public class IntakeCargo extends Command {
  Timer timer;
  Intake intake;
  Lift lift;
  Lights lights;
  boolean overdraw;

  public IntakeCargo(Intake intake, Lift lift, Lights lights) {
    requires(intake);
    requires(lights);
    this.intake = intake;
    this.lift = lift;
    this.lights = lights;
    timer = new Timer();
  }

  @Override
  protected void initialize() {
    timer.reset();
    Scheduler.getInstance().add(new MoveLift(lift, Lift.Position.CARGO_GROUND));
    lights.setLights(Lights.LightState.CARGO);
    intake.setSidePIDF(SmartDashboard.getNumberArray("Cargo Side Roller PIDF", Intake.PIDF.CARGO_SIDE));
    intake.setTopPIDF(SmartDashboard.getNumberArray("Cargo Top Roller PIDF", Intake.PIDF.CARGO_TOP));
    intake.prepareCargo();
    overdraw = false;
  }

  @Override
  protected void execute() {
    intake.intakeCargo();
    if (intake.hasCargo()) {
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
    return (timer.get() > 0.1);
  }

  @Override
  protected void end() {
    if (isFinished()) {
      intake.keepCargo();
      SmartDashboard.putBoolean("Has cargo", true);
    } else {
      intake.stopRollers();
    }
  }

  @Override
  protected void interrupted() {
    end();
  }
}
