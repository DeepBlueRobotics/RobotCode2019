/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj.Joystick;

public class Climb extends Command {
  private Climber climber;
  private Drivetrain dt;
  private Joystick dtJoy;
  private State state;

  public Climb(Climber climber, Drivetrain dt, Joystick joy) {
    // Use requires() here to declare subsystem dependencies
    this.climber = climber;
    this.dt = dt;
    dtJoy = joy;
    requires(climber);
    requires(dt);
    state = State.CLIMBING;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    switch (state) {
      case CLIMBING: 
        dt.drive(-0.5, -0.5);
        climber.runClimber(0.5);
        if (!climber.needToClimb()) {
          state = State.LEVELING;
        }
        checkForRetracting();
        break;
      case LEVELING: 
        dt.drive(-0.5, -0.5);
        climber.runClimber(-0.5);
        if (!climber.canDrop()) {
          state = State.CLIMBING;
        }
        checkForRetracting();
        break;
      case RETRACTING:
        climber.retractClimber();
        double retractGoal = 23.5; // Test for actual number 
        if (climber.getEncDistance() > retractGoal) {
          state = State.FINISHED;
        }
        break;
      case FINISHED:
        end();
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return state == State.FINISHED;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    climber.stopClimber();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }

  private void checkForRetracting() {
    double joyMovingThreshold = 0.1; // Test for actual number
      if (!dt.isStalled() || Math.abs(dtJoy.getY()) > joyMovingThreshold) { 
        state = State.RETRACTING;
        climber.resetEncoder();
      }
  }

  private enum State {
    CLIMBING,
    LEVELING,
    RETRACTING,
    FINISHED
  }
}
