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

  private final double backDrive = -0.5; // TODO: Set this to reasonable/tested value;
  private final double climbUp = 1;
  private final double climbDown = -1;
  private final double retract = -1;
  private final double overrideThreshold = 0.1; // TODO: Set this to reasonable/tested value;
  private final double retractGoal = 0; // TODO: Set this to reasonable/tested value;

  public Climb(Climber climber, Drivetrain dt, Joystick joy) {
    // Use requires() here to declare subsystem dependencies
    this.climber = climber;
    this.dt = dt;
    dtJoy = joy;
    requires(climber);
    requires(dt);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    state = State.CLIMBING;
    climber.resetEncoder();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    switch (state) {
    case CLIMBING:
      dt.drive(backDrive, backDrive);
      climber.runClimber(climbUp);
      if (!climber.needToClimb()) {
        state = State.LEVELING;
      }
      if (robotOnPlatform())
        state = State.RETRACTING;
      break;
    case LEVELING:
      dt.drive(backDrive, backDrive);
      climber.runClimber(climbDown);
      if (!climber.canDrop()) {
        state = State.CLIMBING;
      }
      if (robotOnPlatform())
        state = State.RETRACTING;
      break;
    case RETRACTING:
      climber.runClimber(retract);
      if (climber.getEncDistance() <= retractGoal) {
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

  /**
   * This method checks whether 1) The driver is attempting to override the
   * climbing sequence; or 2) The drivetrain current draw is above average,
   * implying that the robot is on the platform.
   * 
   * @return Returns whether the robot is on the plaform, or the driver is
   *         overriding the climb
   */
  private boolean robotOnPlatform() {
    return dt.isStalled() || Math.abs(dtJoy.getY()) > overrideThreshold;
  }

  private enum State {
    CLIMBING, // going up
    LEVELING, // going down (needed to level robot)
    RETRACTING, // retract foot once on ledge
    FINISHED
  }
}
