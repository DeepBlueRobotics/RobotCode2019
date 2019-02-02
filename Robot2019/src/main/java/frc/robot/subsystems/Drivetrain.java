/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.commands.TeleopDrive;

public class Drivetrain extends Subsystem {
  private SpeedController leftMotor, rightMotor;
  private Encoder leftEnc, rightEnc;
  public Joystick leftJoy, rightJoy;
  private AHRS gyro;

  public Drivetrain(WPI_TalonSRX leftMaster, WPI_VictorSPX leftSlave1, WPI_VictorSPX leftSlave2,
      WPI_TalonSRX rightMaster, WPI_VictorSPX rightSlave1, WPI_VictorSPX rightSlave2, Joystick leftJoy,
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

    this.gyro = gyro;
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new TeleopDrive(this));
  }

  public void drive(double left, double right) {
    leftMotor.set(left);
    rightMotor.set(right);
  }

  public void stop() {
    leftMotor.stopMotor();
    rightMotor.stopMotor();
  }

  public double getEncDist(String type) {
    if (type.equals("left")) {
      return leftEnc.getDistance();
    } else {
      return rightEnc.getDistance();
    }
  }

  public double getEncRate(String type) {
    if (type.equals("left")) {
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
}
