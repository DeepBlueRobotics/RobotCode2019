/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be apccompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
  static WPI_TalonSRX leftMaster, rightMaster;
  static WPI_VictorSPX leftSlave1, leftSlave2, rightSlave1, rightSlave2;
  static String driveMode;

  static {
    // TODO: Put ports
    // Initialize motors on the left side of the drivetrain.
    leftMaster = new WPI_TalonSRX(-1);
    leftSlave1 = new WPI_VictorSPX(-1);
    leftSlave2 = new WPI_VictorSPX(-1);

    // Initialize motors on the right side of the drivetrain.
    rightMaster = new WPI_TalonSRX(-1);
    rightSlave1 = new WPI_VictorSPX(-1);
    rightSlave2 = new WPI_VictorSPX(-1);

    // Slave the Victors to the Talons.
    leftSlave1.follow(leftMaster);
    leftSlave2.follow(leftMaster);
    rightSlave1.follow(rightMaster);
    rightSlave2.follow(rightMaster);

    // Either arcade or tank
    driveMode = "arcade";
  }
}
