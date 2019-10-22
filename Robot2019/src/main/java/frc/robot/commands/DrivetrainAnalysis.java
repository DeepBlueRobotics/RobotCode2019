/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.lib.CharacterizationAnalysis;
import frc.robot.subsystems.Drivetrain;

public class DrivetrainAnalysis extends Command {
    Drivetrain dt;
    String[] inFiles;
    String outFile;

  public DrivetrainAnalysis(Drivetrain dt, String[] inFiles, String outFile) {
    this.dt = dt;
    this.inFiles = inFiles;
    this.outFile = outFile;
  }

  @Override
  protected void execute() {
    CharacterizationAnalysis.characterize(inFiles, outFile);
    System.out.println("Drivetrain Characterization Analysis Successful. All data has been dumped into " + outFile);
    dt.updateDrivetrainParameters(outFile);
  }

  @Override
  protected boolean isFinished() {
    return true;
  }

  @Override
  protected void end() {
  }

  @Override
  protected void interrupted() {
    end();
  }
}