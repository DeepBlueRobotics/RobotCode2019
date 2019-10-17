/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.robot.subsystems.Drivetrain;

/**
 * Add your docs here.
 */
public class QueueMoveBack extends InstantCommand {

  private Drivetrain dt;
  private double time;
  /**
   * Add your docs here.
   */
  public QueueMoveBack(Drivetrain dt, double time) {
    super();
    this.dt = dt;
    this.time = time;
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  // Called once when the command executes
  @Override
  protected void initialize() {
    Scheduler.getInstance().add(new MoveBack(dt, time));
  }

}
