/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotMap;

public class Drivetrain extends Subsystem {
  public enum Side {
    LEFT, RIGHT
  }

  public enum Direction {
    FL, FR, BL, BR    // Forward-Left, Forward-Right, Backward-Left, Backward-Right
  }

  private WPI_TalonSRX leftMaster, rightMaster;
  private Encoder leftEnc, rightEnc;
  private AHRS ahrs;

  private double kVolt, kVel, kAccel;
  private double maxAccel;

  private double maxVoltage;
  private boolean wobbleDone;

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
    leftEnc.reset();
    rightEnc.reset();
    rightEnc.setReverseDirection(true);

    this.ahrs = ahrs;

    // TODO: Update the actual values
    kVolt = 0.0;
    kVel = 0.0;
    kAccel = 0.0;
    maxAccel = 0.0;

    maxVoltage = 12.0;
    wobbleDone = false;
  }

  /**
   * teleop drive initialized in Robot.robotInit() to avoid dependency loops
   * between dt and oi
   */
  @Override
  public void initDefaultCommand() {
  }

  public void drive(double left, double right) {
    SmartDashboard.putNumber("left Input", left);
    SmartDashboard.putNumber("right Input", right);

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
    return leftMaster.getOutputCurrent() >= 30 || rightMaster.getOutputCurrent() >= 30;
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

  public void setWobbleDone(boolean set) {
    wobbleDone = set;
  }

  public boolean wobbleDone() {
    return wobbleDone;
  }
  
  public double getMaxSpeed() { // Return must be adjusted in the future;
    return 13 * 12;
  }

  public void setMaxVoltage(double volts) {
    maxVoltage = volts;
  }

  public double getMaxVoltage() {
    return maxVoltage;
  }

  public void setVoltageCompensation(double volts) {
    ErrorCode ecVoltSat = leftMaster.configVoltageCompSaturation(volts, 10);
    RobotMap.catchError(ecVoltSat);

    ecVoltSat = rightMaster.configVoltageCompSaturation(volts, 10);
    RobotMap.catchError(ecVoltSat);

    leftMaster.enableVoltageCompensation(true);
    rightMaster.enableVoltageCompensation(true);
  }

  public void disableVoltageCompensation() {
    leftMaster.enableVoltageCompensation(false);
    rightMaster.enableVoltageCompensation(false);
  }

  // Characterizes the left/right motor values.
  // Left and right are both values from -1 to 1.
  public double[] characterizedDrive(double left, double right) {
    double dt = 0.02;   // Characterized Drive is called 50 times a second

    double actualLeftVel = getEncRate(Side.LEFT);
    double actualRightVel = getEncRate(Side.RIGHT);

    double desiredLeftVel = this.getMaxSpeed() * left;
    double desiredRightVel = this.getMaxSpeed() * right;

    double leftAccel = (desiredLeftVel - actualLeftVel) / dt;
    double rightAccel = (desiredRightVel - actualRightVel) / dt;

    double newLeft = kVolt + kVel * actualLeftVel + kAccel * leftAccel;
    double newRight = kVolt + kVel * actualRightVel + kAccel * rightAccel;
    return new double[]{newLeft, newRight};
  }
}
