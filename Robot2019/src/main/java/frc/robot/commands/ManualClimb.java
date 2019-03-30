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
import frc.robot.subsystems.Lights;

public class ManualClimb extends Command {
  private Climber climber;
  private Lights lights;
  private Joystick manip;
  
  final private double retractDist = 0;
  final private double climbDist = 24.46; // In inches

  final private int climbJoyAxis = 3;

  public ManualClimb(Climber climber, Joystick manip, Lights lights) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(climber);
    requires(lights);

    this.climber = climber;
    this.lights = lights;
    this.manip = manip;

    if (!SmartDashboard.containsKey("Climber Max Height")) {
      SmartDashboard.putNumber("Climber Max Height", 25);
    }
    if (!SmartDashboard.containsKey("Climber Drive Limit")) {
      SmartDashboard.putNumber("Climber Drive Limit", 0);
    }
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    SmartDashboard.putNumber("Current Angle", 0);
    SmartDashboard.putNumber("Max Angle", 0);
    SmartDashboard.putNumber("Min Angle", 0);
    lights.setLights(Lights.LightState.CLIMBER);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double climbSpeed = manip.getRawAxis(climbJoyAxis);

    // if (climbSpeed > 0 && climber.getEncDistance() >= climbDist) {
    //   climbSpeed = 0;
    // } else if (climbSpeed < 0 && climber.getEncDistance() <= retractDist) {
    //   climbSpeed = 0;
    // }
    
    // climber soft stop to prevent bindinng
    if (climber.getEncDistance() > SmartDashboard.getNumber("Climber Max Height", 25) && (-climbSpeed) > SmartDashboard.getNumber("Climber Drive Limit", 0)) {
      climbSpeed = -SmartDashboard.getNumber("Climber Drive Limit", 0);
    }

    climber.runClimber(climbSpeed);

    double angle = climber.getAngle();
    SmartDashboard.putNumber("Current Angle", angle);
    if (angle > SmartDashboard.getNumber("Max Angle", 0)) {
      SmartDashboard.putNumber("Max Angle", angle);
    }
    if (angle < SmartDashboard.getNumber("Min Angle", 0)) {
      SmartDashboard.putNumber("Min Angle", angle);
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
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
