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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.KeepClimber;

public class Climber extends Subsystem {
  private VictorSP motor; // Mini-CIM
  private Encoder enc;
  private AHRS ahrs;
  private DoubleSolenoid pistons;

  final private double minTilt = 0; // In degrees // TODO: Update wtih actual number
  final private double minDist = 20; // In inches // TODO: Update with actual number
  final private double maxTilt = 30; // In degrees // TODO: Update with actual number
  final private double maxDist = 24; // In inches // TODO: Update with actual number
  final private double slipTolerance = 0.5; // In inches // TODO: Update with actual number;

  public Climber(VictorSP motor, Encoder enc, AHRS ahrs, DoubleSolenoid pistons) {
    this.motor = motor;
    this.enc = enc;
    this.ahrs = ahrs;
    this.pistons = pistons;

    double pulseFraction = 1.0 / 256;
    double pitchDiameter = 1.790; // https://www.vexrobotics.com/35-sprockets.html#Drawing
    enc.setDistancePerPulse(pulseFraction * Math.PI * pitchDiameter);
    enc.setReverseDirection(true);
    enc.reset();
  }

  public void toggleRails() {
    if (!SmartDashboard.getBoolean("Outreach Mode", false)) {
      if (pistons.get() == DoubleSolenoid.Value.kForward) {
        pistons.set(DoubleSolenoid.Value.kReverse);
      } else {
        pistons.set(DoubleSolenoid.Value.kForward);
      }
    }
  }

  public void runClimber(double speed) {
    motor.set(speed);
  }

  public void stopClimber() {
    motor.stopMotor();
  }

  public double getAngle() {
    return ahrs.getPitch();
  }

  //We are erring on the side of changing directions too much
  public boolean needToClimb() {
    return getAngle() < maxTilt && enc.getDistance() < maxDist;
  }

  public boolean canDrop() {
    return getAngle() > minTilt && enc.getDistance() > minDist;
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
