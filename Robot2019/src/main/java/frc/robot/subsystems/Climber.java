/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.VictorSP;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import frc.robot.commands.Climb;

public class Climber extends Subsystem {
  private VictorSP motor; // Mini-CIM
  private Encoder enc;
  private AHRS accel;
  private DoubleSolenoid pistons;

  final private double minTilt = 91; // TODO: Update with actual number
  final private double maxTilt = 130; // TODO: Update with actual number

  public Climber(VictorSP motor, Encoder enc, AHRS accel, DoubleSolenoid pistons) {
    this.motor = motor;
    this.enc = enc;
    this.accel = accel;
    this.pistons = pistons;
  }

  public void actuateRails() {
    pistons.set(DoubleSolenoid.Value.kForward);
  }
  
  public void raiseClimber(double speed) {
    motor.set(speed);
  }

  public void lowerClimber(double speed) {
    motor.set(-speed);
  }

  public void stopClimber() {
    motor.stopMotor();
  }

  public boolean isTilted() {
    double zTilt = getTilt()[2];
    if (zTilt > minTilt && zTilt < maxTilt) {
      return true;
    } else {
      return false;
    }
  }

  public double getEncDistance() {
    return enc.getDistance();
  }

  public double[] getTilt() {
    double x = accel.getRoll();
    double y = accel.getPitch();
    double z = accel.getYaw();
    double[] tilts = {x, y, z};
    return tilts;
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new Climb(this));
  }
}
