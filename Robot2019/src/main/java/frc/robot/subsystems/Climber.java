/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.commands.Climb;

public class Climber extends Subsystem {
  private VictorSP motor; // Mini-CIM
  private Encoder enc;
  private AHRS gyro;
  private DoubleSolenoid pistons;
  private BuiltInAccelerometer bia;

  final private double minTilt = 0; //In degrees
  final private double maxTilt = 30; //In degrees // TODO: Update with actual number

  public Climber(VictorSP motor, Encoder enc, AHRS gyro, DoubleSolenoid pistons, BuiltInAccelerometer bia) {
    this.motor = motor;
    this.enc = enc;
    this.gyro = gyro;
    this.pistons = pistons;
    this.bia = bia;
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

  public boolean needToClimb() {
    double angle = Math.atan2(bia.getZ(), bia.getX());
    return angle > minTilt;
  }
  public boolean canDrop() {
    double angle = Math.atan2(bia.getZ(), bia.getX());
    return angle < maxTilt;
  }

  public double getEncDistance() {
    return enc.getDistance();
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new Climb(this));
  }
}
