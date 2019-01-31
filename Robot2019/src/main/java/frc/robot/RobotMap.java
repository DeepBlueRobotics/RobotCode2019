/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.ArrayList;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SPI;
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
  static AHRS gyro;

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

    // Configuration for the motors.
    configureTalon(leftMaster);
    configureTalon(rightMaster);
    configureVictors(leftSlave1);
    configureVictors(leftSlave2);
    configureVictors(rightSlave1);
    configureVictors(rightSlave2);

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
    gyro = new AHRS(SPI.Port.kMXP);

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

  private static void configureTalon(WPI_TalonSRX tsrx) {
    // Put all configurations for the talon motor controllers in here.
    // All values are from last year's code.
		tsrx.configNominalOutputForward(0, 10);
		tsrx.configNominalOutputReverse(0, 10);
		tsrx.configPeakOutputForward(1, 10);
		tsrx.configPeakOutputReverse(-1, 10);
		tsrx.configPeakCurrentLimit(0, 0);
		tsrx.configPeakCurrentDuration(0, 0);
		// 40 Amps is the amp limit of a CIM. lThe PDP has 40 amp circuit breakers,
		tsrx.configContinuousCurrentLimit(40, 0);
		tsrx.enableCurrentLimit(true);
		tsrx.configNeutralDeadband(0.001, 10);
		tsrx.setNeutralMode(NeutralMode.Brake);
  }

  private static void configureVictors(VictorSPX vspx) {
    // Put all configurations for the victor motor controllers in here.
      vspx.configNominalOutputForward(0, 10);
      vspx.configNominalOutputReverse(0, 10);
      vspx.configPeakOutputForward(1, 10);
      vspx.configPeakOutputReverse(-1, 10); 
      vspx.configNeutralDeadband(0.001, 10);
      vspx.setNeutralMode(NeutralMode.Brake);
  }
}
