/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.io.FileWriter;
import java.io.IOException;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.TeleopDrive;

public class Drivetrain extends Subsystem {
  public enum Side {
    LEFT, RIGHT
  }

  private WPI_TalonSRX leftMotor, rightMotor;
  private Encoder leftEnc, rightEnc;
  public Joystick leftJoy, rightJoy;
  private AHRS gyro;

  public Drivetrain(WPI_TalonSRX leftMaster, BaseMotorController leftSlave1, BaseMotorController leftSlave2,
      WPI_TalonSRX rightMaster, BaseMotorController rightSlave1, BaseMotorController rightSlave2, Joystick leftJoy,
      Joystick rightJoy, Encoder leftEnc, Encoder rightEnc, AHRS gyro) {

    leftSlave1.follow(leftMaster);
    leftSlave2.follow(leftMaster);
    this.leftMotor = leftMaster;

    rightSlave1.follow(rightMaster);
    rightSlave2.follow(rightMaster);
    this.rightMotor = rightMaster;

    rightMaster.setInverted(true);
    rightSlave1.setInverted(true);
    rightSlave2.setInverted(true);

    this.leftJoy = leftJoy;
    this.rightJoy = rightJoy;

    this.leftEnc = leftEnc;
    this.rightEnc = rightEnc;

    double pulseFraction = 1.0 / 256;
    double wheelDiameter = 5;
    leftEnc.setDistancePerPulse(pulseFraction * Math.PI * wheelDiameter);
    rightEnc.setDistancePerPulse(pulseFraction * Math.PI * wheelDiameter);

    this.gyro = gyro;
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new TeleopDrive(this));
  }

  public void drive(double left, double right) {
    leftMotor.set(left);
    rightMotor.set(right);
    SmartDashboard.putNumber("Encoder Distance Left:", leftEnc.getDistance());
    SmartDashboard.putNumber("Encoder Distance Right:", rightEnc.getDistance());
  }

  public void stop() {
    leftMotor.stopMotor();
    rightMotor.stopMotor();
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
    gyro.reset();
  }

  public double getGyroRate() {
    return gyro.getRate();
  }

  public double getGyroAngle() {
    return gyro.getYaw();
  }

  public double getMaxSpeed() { // Return must be adjusted in the future;
    return 204.0;
  }

  public void setVoltageCompensation(double volts) {
    ErrorCode ecVoltSat = leftMotor.configVoltageCompSaturation(volts, 10);

    if (!ecVoltSat.equals(ErrorCode.OK)) {
      throw new RuntimeException("Voltage Saturation Configuration could not be set");
    }

    ecVoltSat = rightMotor.configVoltageCompSaturation(volts, 10);

    if (!ecVoltSat.equals(ErrorCode.OK)) {
      throw new RuntimeException("Voltage Saturation Configuration could not be set");
    }

    leftMotor.enableVoltageCompensation(true);
    rightMotor.enableVoltageCompensation(true);
  }

  public void disableVoltageCompensation() {
    leftMotor.enableVoltageCompensation(false);
    rightMotor.enableVoltageCompensation(false);
  }
}
