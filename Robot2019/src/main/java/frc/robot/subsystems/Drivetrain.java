/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

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

  private double maxVoltage;

  private double flKV, flKA, flVI;
  private double frKV, frKA, frVI;
  private double blKV, blKA, blVI;
  private double brKV, brKA, brVI;

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
    updateDrivetrainParameters();

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

  public void updateDrivetrainParameters() {
    try {
      Scanner filereader = new Scanner(new File("/home/lvuser/drive_char_params.csv"));
      String line = filereader.next();
      // Forward-Left
      flKV = Double.valueOf(line.split(",")[0]);
      flKA = Double.valueOf(line.split(",")[1]);
      flVI = Double.valueOf(line.split(",")[2]);
      System.out.println(flKV + "," + flKA + "," + flVI);

      line = filereader.next();
      // Forward-right
      frKV = Double.valueOf(line.split(",")[0]);
      frKA = Double.valueOf(line.split(",")[1]);
      frVI = Double.valueOf(line.split(",")[2]);
      System.out.println(frKV + "," + frKA + "," + frVI);

      line = filereader.next();
      // Backward-Left
      blKV = Double.valueOf(line.split(",")[0]);
      blKA = Double.valueOf(line.split(",")[1]);
      blVI = Double.valueOf(line.split(",")[2]);
      System.out.println(blKV + "," + blKA + "," + blVI);

      line = filereader.next();
      // Backward-Right
      brKV = Double.valueOf(line.split(",")[0]);
      brKA = Double.valueOf(line.split(",")[1]);
      brVI = Double.valueOf(line.split(",")[2]);
      System.out.println(brKV + "," + brKA + "," + brVI);
      filereader.close();

      // Averaging numbers because they vary so much
      double avg = (flKA + frKA + blKA + brKA) / 4;
      flKA = avg;
      frKA = avg;
      blKA = avg;
      brKA = avg;
    } catch (FileNotFoundException e) {
      flKV = 0.06369046755507658;
      flKA = 0.0215894793277297;
      flVI = 0.8403701236277824;

      frKV = 0.0619423013628032;
      frKA = 0.04044703465602449;
      frVI = 0.810212379284332;

      blKV = 0.06388520699977113;
      blKA = 0.025492804438184545;
      blVI = 0.8071078220643216;

      brKV = 0.06140765089854154;
      brKA = 0.042046502553651215;
      brVI = 0.7929289166816246;
      
      e.printStackTrace();
    }
  }

  public double calculateVoltage(Direction dir, double velocity, double acceleration) {
    /*System.out.println("Velocity: " + velocity + ", Acceleration: " + acceleration);
    System.out.println(flKV + "," + flKA + "," + flVI);
    System.out.println(frKV + "," + frKA + "," + frVI);
    System.out.println(blKV + "," + blKA + "," + blVI);
    System.out.println(brKV + "," + brKA + "," + brVI);*/

    if (dir == Direction.FL) {
      return flKV * velocity + flKA * acceleration + flVI;
    } else if (dir == Direction.FR) {
      return frKV * velocity + frKA * acceleration + frVI;
    } else if (dir == Direction.BL) {
      return blKV * velocity + blKA * acceleration - blVI;
    } else {
      return brKV * velocity + brKA * acceleration - brVI;
    }
  }

  public double calculateAcceleration(Direction dir, double velocity, double voltage) {
    if (dir == Direction.FL) {
      return (voltage - flKV * velocity - flVI) / flKA;
    } else if (dir == Direction.FR) {
      return (voltage - frKV * velocity - frVI) / frKA;
    } else if (dir == Direction.BL) {
      return (voltage - blKV * velocity - blVI) / blKA;
    } else {
      return (voltage - brKV * velocity + brVI) / brKA;
    }
  }
}
