/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.ArrayList;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.commands.ArcadeDrive;
import frc.robot.commands.TankDrive;

public class Drivetrain extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  private WPI_TalonSRX leftMaster, rightMaster;
  private WPI_VictorSPX leftSlave1, leftSlave2, rightSlave1, rightSlave2;
  private double leftK, rightK;
  private String drive_mode;

  private Encoder leftEnc, rightEnc;
  private AHRS gyro;

  public Joystick leftJoy, rightJoy;

  public Drivetrain(ArrayList<WPI_TalonSRX> masters, ArrayList<WPI_VictorSPX> slaves, ArrayList<Joystick> joys,
      ArrayList<Encoder> encs, AHRS fancygyro) {
    leftMaster = masters.get(0);
    rightMaster = masters.get(1);
    leftSlave1 = slaves.get(0);
    leftSlave2 = slaves.get(1);
    rightSlave1 = slaves.get(2);
    rightSlave2 = slaves.get(3);

    leftJoy = joys.get(0);
    rightJoy = joys.get(1);

    leftEnc = encs.get(0);
    rightEnc = encs.get(1);
    gyro = fancygyro;

    leftK = 1.0;
    rightK = 1.0;

    drive_mode = "Arcade";
  }

  public void TeleopDrive(double val1, double val2) {
      leftMaster.set(Math.signum(val1) * leftK * val1 * val1);
      rightMaster.set(Math.signum(val2) * rightK * val2 * val2);
  }

  public double getEncDist(String type) {
    if (type.equals("left")) {
      return leftEnc.getDistance();
    }
    else {
      return rightEnc.getDistance();
    }
  }

  public double getEncRate(String type) {
    if (type.equals("left")) {
      return leftEnc.getRate();
    }
    else {
      return rightEnc.getRate();
    }
  }

  public void changeDriveMode() {
    if (drive_mode.equals("Arcade")) {
      drive_mode = "Tank";
    } else {
      drive_mode = "Arcade";
    }
    initDefaultCommand();
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

  @Override
  public void initDefaultCommand() {
    if (drive_mode.equals("Arcade")) {
      setDefaultCommand(new ArcadeDrive(this));
    } else {
      setDefaultCommand(new TankDrive(this));
    }
  }
}
