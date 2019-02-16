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
  private boolean up;
  private State state;

  public Climb(Climber climber, Drivetrain dt, Joystick joy) {
    // Use requires() here to declare subsystem dependencies
    this.climber = climber;
    this.dt = dt;
    dtJoy = joy;
    requires(climber);
    requires(dt);
    up = true;
    state = State.CLIMB;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if (state == State.CLIMB) {
      climb();
      if (!dt.isStalled() || Math.abs(dtJoy.getY()) > 0.1) { // 0.1 = Placeholder/approximation 
        state = State.RETRACT;
        climber.resetEncoder();
      }
    } else {
      retract();
      if (climber.getEncDistance() > 23) { // 23 = Placeholder/approximation 
        state = State.FINISHED;
      }
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return state == state.FINISHED;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    climber.retractClimber();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }

  private void climb() {
    dt.drive(-0.5, -0.5);
    if (up) {
      climber.runClimber(0.5);
      if (!climber.needToClimb())
        up = false;
    } else {
      climber.runClimber(-0.5);
      if (!climber.canDrop())
        up = true;
    }
  }

  private void retract() {
    climber.retractClimber();
  }

  private enum State {
    CLIMB,
    RETRACT,
    FINISHED
  }
}
