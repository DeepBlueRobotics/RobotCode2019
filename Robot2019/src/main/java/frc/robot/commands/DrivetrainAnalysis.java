/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.DrivetrainCharAnalysis;

public class DrivetrainAnalysis extends Command {
    String file1, file2, outfile;
  /**
   * Handles all the teleoperated driving functionality
   * 
   * @param dt the Drivetrain object to use, passing it in is useful for testing
   *           purposes
   */
  public DrivetrainAnalysis() {
    file1 = "/home/lvuser/drive_char_linear.csv";
    file2 = "/home/lvuser/drive_char_stepwise.csv";
    outfile = "/home/lvuser/drive_char_params.csv";
  }

  @Override
  protected void execute() {
    DrivetrainCharAnalysis.ordinaryLeastSquares(file1, file2, outfile);
    System.out.println("Drivetrain Characterization Analysis Successful. All data has been dumped into " + outfile);
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