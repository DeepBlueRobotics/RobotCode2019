/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
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
  // For example to map the left and right motors, you could define the
  // following variables to use with your drivetrain subsystem.
  // public static int leftMotor = 1;
  // public static int rightMotor = 2;

  // If you are using multiple modules, make sure to define both the port
  // number and the module. For example you with a rangefinder:
  // public static int rangefinderPort = 1;
  // public static int rangefinderModule = 1;
  static WPI_TalonSRX left1, right1;
  static WPI_VictorSPX left2, left3, right2, right3;

  static {
    // TODO: Put ports
    left1 = new WPI_TalonSRX(-1);
    left2 = new WPI_VictorSPX(-1);
    left2.follow(left1);
    left3 = new WPI_VictorSPX(-1);
    left3.follow(left1);
    right1 = new WPI_TalonSRX(-1);
    right2 = new WPI_VictorSPX(-1);
    right2.follow(right1);
    right3 = new WPI_VictorSPX(-1);
    right3.follow(right1);
  }

}
