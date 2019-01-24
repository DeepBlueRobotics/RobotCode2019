/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be apccompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.ArrayList;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
  static WPI_TalonSRX leftMaster, rightMaster;

  static WPI_VictorSPX leftSlave1, leftSlave2, rightSlave1, rightSlave2;

  static Encoder leftEnc, rightEnc;

  static String driveMode;

  static ArrayList<WPI_TalonSRX> masters;
  static ArrayList<WPI_VictorSPX> slaves;
  static ArrayList<Encoder> encoders;

  static {
    // Initialize motors on the left side of the drivetrain.
    leftMaster = new WPI_TalonSRX(getPort("LeftMaster"));
    leftSlave1 = new WPI_VictorSPX(getPort("LeftSlave1"));
    leftSlave2 = new WPI_VictorSPX(getPort("LeftSlave2"));

    // Initialize motors on the right side of the drivetrain.
    rightMaster = new WPI_TalonSRX(getPort("RightMaster"));
    rightSlave1 = new WPI_VictorSPX(getPort("RightSlave1"));
    rightSlave2 = new WPI_VictorSPX(getPort("RightSlave2"));

    // Slave the Victors to the Talons.
    leftSlave1.follow(leftMaster);
    leftSlave2.follow(leftMaster);
    rightSlave1.follow(rightMaster);
    rightSlave2.follow(rightMaster);

    masters.add(leftMaster);
    masters.add(rightMaster);
    slaves.add(leftSlave1);
    slaves.add(leftSlave2);
    slaves.add(rightSlave1);
    slaves.add(rightSlave2);

    leftEnc = new Encoder(new DigitalInput(getPort("LeftEncoder1")), new DigitalInput(getPort("LeftEncoder2")));
    rightEnc = new Encoder(new DigitalInput(getPort("rightEncoder1")), new DigitalInput(getPort("rightEncoder2")));
  
    encoders.add(leftEnc);
    encoders.add(rightEnc);
  }

  private static int getPort(String name) {
    int port;
    port = (int)(SmartDashboard.getNumber("Port/" + name, -1));
    
    if (port == -1) {
      System.out.println("Port name" + name + "is not defined in SmartDashboard.");
      return -1;
    } else {
      return port;
    }
  }
}
