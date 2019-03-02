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
import frc.robot.DrivetrainCharAnalysis;
import frc.robot.subsystems.Drivetrain;

public class DrivetrainAnalysis extends Command {
    Drivetrain dt;
    String file1, file2, file3, file4, outfile;
  /**
   * Handles all the teleoperated driving functionality
   * 
   * @param dt the Drivetrain object to use, passing it in is useful for testing
   *           purposes
   */
  public DrivetrainAnalysis(Drivetrain dt) {
    this.dt = dt;
    file1 = "/home/lvuser/drive_char_linear_for.csv";
    file2 = "/home/lvuser/drive_char_stepwise_for.csv";
    file3 = "/home/lvuser/drive_char_linear_back.csv";
    file4 = "/home/lvuser/drive_char_stepwise_back.csv";
    outfile = "/home/lvuser/drive_char_params.csv";
  }

  @Override
  protected void execute() {
    try {
        PrintWriter pw = new PrintWriter(outfile);
        pw.close();
    } catch (FileNotFoundException f) {
        f.printStackTrace();
    }
    DrivetrainCharAnalysis.ordinaryLeastSquares(file1, file2, outfile);
    DrivetrainCharAnalysis.ordinaryLeastSquares(file3, file4, outfile);
    System.out.println("Drivetrain Characterization Analysis Successful. All data has been dumped into " + outfile);
    dt.updateDrivetrainParameters();
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