/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Drivetrain.Side;

public class WobbleDrive extends Command {
  Drivetrain dt;
  Side side = Side.LEFT; // We start with left side. TODO: confirm this
  Timer tim;
  boolean leftSideDone;
  boolean rightSideDone;
  private final double minTime = 0.2;
  private final double wobbleTime = 0.3; // TODO: Set to actual number
  private final double driveSpeed = 0.3; // TODO: Set to actual number
  private final double encRateTolerance = 1; // TODO: Set to actual number

  public WobbleDrive(Drivetrain dt) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(dt);
    this.dt = dt;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    tim = new Timer();
    tim.reset();
    tim.start();
    dt.setWobbleDone(false);
    leftSideDone = false;
    rightSideDone = false;
    SmartDashboard.putBoolean("Wobble drive done", false);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    boolean done = dt.getEncRate(Side.LEFT) < encRateTolerance && dt.getEncRate(Side.RIGHT) < encRateTolerance;
    if (tim.get() > minTime) {
      if (side == Side.LEFT) {
        leftSideDone = done;
      } else {
        rightSideDone = done;
      }
    }
    if (leftSideDone) {
      tim.reset();
      tim.start();
      side = Side.RIGHT;
    }
    if (rightSideDone) {
      tim.reset();
      tim.start();
      side = Side.LEFT;
    }

    if (tim.get() > wobbleTime && (!leftSideDone && !rightSideDone)) {
      if (side == Side.LEFT) {
        side = Side.RIGHT;
      } else {
        side = Side.LEFT;
      }
      tim.reset();
      tim.start();
      dt.drive(0, 0);
    } else {
      dt.drive(side == Side.LEFT ? driveSpeed : 0, side == Side.RIGHT ? driveSpeed : 0);
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return dt.wobbleDone() || (leftSideDone && rightSideDone);
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    dt.stop();
    SmartDashboard.putBoolean("Wobble drive done", true);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
