/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.subsystems.Intake;

/**
 * Add your docs here.
 */
public class ConfigureWrist extends InstantCommand {

  private Intake intake;
  private double configDeg;

  /**
   * Add your docs here.
   */
  public ConfigureWrist(Intake intake, double configDeg) {
    super();
    requires(intake);
    this.intake = intake;
    this.configDeg = configDeg;
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  // Called once when the command executes
  @Override
  protected void initialize() {
    intake.configure(configDeg/360.0);
  }

}
