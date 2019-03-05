/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;

public class ManualClimb extends Command {
  private Climber climber;
  private Drivetrain dt;
  private Joystick leftJoy, rightJoy;
  
  final private double retractDist = 0;
  final private double climbDist = 24.46; // In inches

  public ManualClimb(Climber climber, Drivetrain dt, Joystick leftJoy, Joystick rightJoy) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(climber);
    requires(dt);

    this.climber = climber;
    this.dt = dt;
    this.leftJoy = leftJoy;
    this.rightJoy = rightJoy;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    SmartDashboard.putNumber("current angle", 0);
    SmartDashboard.putNumber("max angle", 0);
    SmartDashboard.putNumber("min angle", 0);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double climbSpeed = -leftJoy.getY();
    double driveSpeed = -rightJoy.getY();

    if (climbSpeed > 0 && climber.getEncDistance() >= climbDist) {
      climbSpeed = 0;
    } else if (climbSpeed < 0 && climber.getEncDistance() <= retractDist) {
      climbSpeed = 0;
    }
    climber.runClimber(climbSpeed);
    dt.drive(driveSpeed, driveSpeed);

    double angle = climber.getAngle();
    SmartDashboard.putNumber("current angle", angle);
    if (angle > SmartDashboard.getNumber("max angle", 0)) {
      SmartDashboard.putNumber("max angle", angle);
    }
    if (angle < SmartDashboard.getNumber("min angle", 0)) {
      SmartDashboard.putNumber("min angle", angle);
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    climber.stopClimber();
    dt.stop();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
