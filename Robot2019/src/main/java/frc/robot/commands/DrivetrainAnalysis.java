/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.lib.CharacterizationAnalysis;
import frc.robot.subsystems.Drivetrain;

public class DrivetrainAnalysis extends Command {
    Drivetrain dt;
    String[] in_files;
    String outfile;
  /**
   * Handles all the teleoperated driving functionality
   * 
   * @param dt the Drivetrain object to use, passing it in is useful for testing
   *           purposes
   */
  public DrivetrainAnalysis(Drivetrain dt, String[] in_files, String outfile) {
    this.dt = dt;
    this.in_files = in_files;
    this.outfile = outfile;
  }

  @Override
  protected void execute() {
    try {
        PrintWriter pw = new PrintWriter(outfile);
        pw.close();
    } catch (FileNotFoundException f) {
        f.printStackTrace();
    }
    CharacterizationAnalysis.characterize(in_files, outfile);
    System.out.println("Drivetrain Characterization Analysis Successful. All data has been dumped into " + outfile);
    dt.updateDrivetrainParameters(outfile);
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