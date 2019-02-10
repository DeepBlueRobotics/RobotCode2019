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
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.TeleopDrive;

public class Drivetrain extends Subsystem {
  private enum Side {
    LEFT, RIGHT
  }

  private SpeedController leftMotor, rightMotor;
  private Encoder leftEnc, rightEnc;
  public Joystick leftJoy, rightJoy;
  private AHRS gyro;
  public double suppliedVoltage, maxVoltage, voltage_runtime;

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

    suppliedVoltage = 0.0;
    maxVoltage = 12.0; // For drivetrain characterization.
    voltage_runtime = 0.0;
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

  public void writeMeasuredVelocity(FileWriter fw) {
    double leftMotorVelocity, rightMotorVelocity;
    StringBuilder sb = new StringBuilder();

    leftMotorVelocity = getEncRate(Side.LEFT);
    rightMotorVelocity = getEncRate(Side.RIGHT);

    voltage_runtime += 0.02; // IncreaseVoltage occurs every 1/50 of a second
    sb.append(String.valueOf(voltage_runtime) + ",");
    sb.append(String.valueOf(suppliedVoltage) + ",");
    sb.append(String.valueOf(leftMotorVelocity) + ",");
    sb.append(String.valueOf(rightMotorVelocity) + "\r\n");

    try {
      fw.write(sb.toString());
    } catch (IOException e) {
      System.out.println("FileWriter object cannot write StringBuilder object: " + e);
    }
  }

  public Side getSideValue(String type) {
    if (type.equals("LEFT")) {
      return Side.LEFT;
    } else if (type.equals("RIGHT")) {
      return Side.RIGHT;
    } else {
      System.out.println(
          "Type value provided to Drivetrain.getSideValue does not match either LEFT or RIGHT. Value of type: " + type);
      return Side.LEFT;
    }
  }

  public void setVoltageCompensation(double volts) {
    ((BaseMotorController) leftMotor).enableVoltageCompensation(true);
    ErrorCode ecVoltSat = ((BaseMotorController) leftMotor).configVoltageCompSaturation(volts, 10);

    if (!ecVoltSat.equals(ErrorCode.OK)) {
      throw new RuntimeException("Voltage Saturation Configuration could not be set");
    }

    ((BaseMotorController) rightMotor).enableVoltageCompensation(true);
    ecVoltSat = ((BaseMotorController) rightMotor).configVoltageCompSaturation(volts, 10);

    if (!ecVoltSat.equals(ErrorCode.OK)) {
      throw new RuntimeException("Voltage Saturation Configuration could not be set");
    }

    maxVoltage = volts;
  }

  public void disableVoltageCompensation() {
    ((BaseMotorController) leftMotor).enableVoltageCompensation(false);
    ((BaseMotorController) rightMotor).enableVoltageCompensation(false);
  }
}
