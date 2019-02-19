/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.commands.KeepClimber;

public class Climber extends Subsystem {
  private VictorSP motor; // Mini-CIM
  private Encoder enc;
  private AHRS ahrs;
  private DoubleSolenoid pistons;
  public boolean climbing;

  final private double minTilt = 0; // In degrees // TODO: Update wtih actual number
  final private double minDist = 13; // In inches // TODO: Update with actual number
  final private double maxTilt = 30; // In degrees // TODO: Update with actual number
  final private double maxDist = 15; // In inches // TODO: Update with actual number
  final private double slipTolerance = 0.5; // In inches // TODO: Update with actual number;

  public Climber(VictorSP motor, Encoder enc, AHRS ahrs, DoubleSolenoid pistons) {
    this.motor = motor;
    this.enc = enc;
    this.ahrs = ahrs;
    this.pistons = pistons;
    climbing = false;

    double pulseFraction = 1.0/256;
    double pitchDiameter = 1.790; // https://www.vexrobotics.com/35-sprockets.html#Drawing
    enc.setDistancePerPulse(pulseFraction * Math.PI * pitchDiameter);
    enc.reset();
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
    double angle = Math.atan2(ahrs.getRawAccelZ(), ahrs.getRawAccelX());
    angle *= 180 / Math.PI;
    return angle < maxTilt || enc.getDistance() < minDist;
  }

  public boolean canDrop() {
    double angle = Math.atan2(ahrs.getRawAccelZ(), ahrs.getRawAccelX());
    angle *= 180 / Math.PI;
    return angle > minTilt || enc.getDistance() > maxDist;
  }

  public double getEncDistance() {
    return enc.getDistance();
  }

  public void resetEncoder() {
    enc.reset();
  }

  public boolean slipping() {
    return enc.getDistance() > slipTolerance;
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new KeepClimber(this));
  }
}
