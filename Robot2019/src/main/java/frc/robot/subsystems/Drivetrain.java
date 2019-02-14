/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drivetrain extends Subsystem {
  public enum Side {
    LEFT, RIGHT
  }

  private WPI_TalonSRX leftMaster, rightMaster;
  private Encoder leftEnc, rightEnc;
  private AHRS ahrs;
  private double kV, kA, vIntercept;

  public Drivetrain(WPI_TalonSRX leftMaster, BaseMotorController leftSlave1, BaseMotorController leftSlave2,
      WPI_TalonSRX rightMaster, BaseMotorController rightSlave1, BaseMotorController rightSlave2, Encoder leftEnc,
      Encoder rightEnc, AHRS ahrs) {

    leftSlave1.follow(leftMaster);
    leftSlave2.follow(leftMaster);
    this.leftMaster = leftMaster;

    rightSlave1.follow(rightMaster);
    rightSlave2.follow(rightMaster);
    this.rightMaster = rightMaster;

    rightMaster.setInverted(true);
    rightSlave1.setInverted(true);
    rightSlave2.setInverted(true);

    this.leftEnc = leftEnc;
    this.rightEnc = rightEnc;

    double pulseFraction = 1.0 / 256;
    double wheelDiameter = 5;
    leftEnc.setDistancePerPulse(pulseFraction * Math.PI * wheelDiameter);
    rightEnc.setDistancePerPulse(pulseFraction * Math.PI * wheelDiameter);

    this.ahrs = ahrs;

    try {
      Scanner filereader = new Scanner(new File("/home/lvuser/drive_char_params.csv"));
      filereader.nextLine();
      String line = filereader.next();
      kV = Double.valueOf(line.split(",")[0]);
      kA = Double.valueOf(line.split(",")[1]);
      vIntercept = Double.valueOf(line.split(",")[2]);
      filereader.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  /**
   * teleop drive initialized in Robot.robotInit() to avoid dependency loops
   * between dt and oi
   */
  @Override
  public void initDefaultCommand() {
  }

  public void drive(double left, double right) {
    leftMaster.set(left);
    rightMaster.set(right);
    SmartDashboard.putNumber("Encoder Distance Left:", leftEnc.getDistance());
    SmartDashboard.putNumber("Encoder Distance Right:", rightEnc.getDistance());
  }

  public void stop() {
    leftMaster.stopMotor();
    rightMaster.stopMotor();
  }

  public boolean isStalled() {
    return leftMaster.getOutputCurrent() >= 30 || rightMaster.getOutputCurrent() >= 30; // TODO: Find value that
                                                                                        // actually works (test)
  }

  public double getEncDist(Side type) {
    if (type == Side.LEFT) {
      return leftEnc.getDistance();
    } else {
      return rightEnc.getDistance();
    }
  }

  public double getEncRate(Side type) {
    if (type == Side.LEFT) {
      return leftEnc.getRate();
    } else {
      return rightEnc.getRate();
    }
  }

  public void resetGyro() {
    ahrs.reset();
  }

  public double getGyroRate() {
    return ahrs.getRate();
  }

  public double getGyroAngle() {
    return ahrs.getYaw();
  }

  public double getMaxSpeed() { // Return must be adjusted in the future;
    return 204.0;
  }

  public void setVoltageCompensation(double volts) {
    ErrorCode ecVoltSat = leftMaster.configVoltageCompSaturation(volts, 10);

    if (!ecVoltSat.equals(ErrorCode.OK)) {
      throw new RuntimeException("Voltage Saturation Configuration could not be set");
    }

    ecVoltSat = rightMaster.configVoltageCompSaturation(volts, 10);

    if (!ecVoltSat.equals(ErrorCode.OK)) {
      throw new RuntimeException("Voltage Saturation Configuration could not be set");
    }

    leftMaster.enableVoltageCompensation(true);
    rightMaster.enableVoltageCompensation(true);
  }

  public void disableVoltageCompensation() {
    leftMaster.enableVoltageCompensation(false);
    rightMaster.enableVoltageCompensation(false);
  }

  public double calculateVoltage(double velocity, double acceleration) {
    return kV * velocity + kA * acceleration + vIntercept;
  }
}
