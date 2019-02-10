/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.VictorSP;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.commands.Climb;

public class Climber extends Subsystem {
  private VictorSP motor; // Mini-CIM
  private Encoder enc;
  private AHRS gyro;
  private DoubleSolenoid pistons;

  final private double minTilt = 91; // TODO: Update with actual number
  final private double maxTilt = 130; // TODO: Update with actual number

  public Climber(VictorSP motor, Encoder enc, AHRS gyro, DoubleSolenoid pistons) {
    this.motor = motor;
    this.enc = enc;
    this.gyro = gyro;
    this.pistons = pistons;
  }

  public void actuateRails() {
    pistons.set(DoubleSolenoid.Value.kForward);
  }
  
  public void runClimber(double speed) {
    motor.set(speed);
  }

  public void stopClimber() {
    motor.stopMotor();
  }

  public boolean isTilted() {
    return gyro.getRoll() > minTilt && gyro.getRoll() < maxTilt; // TODO: set to the actual correct method
  }

  public double getEncDistance() {
    return enc.getDistance();
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new Climb(this));
  }
}
