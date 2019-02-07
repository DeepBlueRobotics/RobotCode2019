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
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import frc.robot.commands.Climb;

public class ClimberSubsystem extends Subsystem {
  private VictorSP motor; // Mini-CIM
  private PowerDistributionPanel pdp;
  private Encoder enc;
  private Accelerometer accel;
  private int PDPClimberPort;
  private double miniCIMStallCurrent, minimumTilit, maximumTilt;

  public ClimberSubsystem(VictorSP motor, PowerDistributionPanel pdp, Encoder enc, Accelerometer accel) {
    this.motor = motor;
    this.pdp = pdp;
    this.enc = enc;
    this.accel = accel;

    PDPClimberPort = -1; // Update this with actual port number
    miniCIMStallCurrent = 89.0;   // To determine whether or not the climber motor has overdrawn 
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

  public boolean isStalled() {
    if (pdp.getCurrent(PDPClimberPort) > miniCIMStallCurrent) {
      return true;
    } else {
      return false;
    }
  }

  public boolean isTilted() {
    double zTilt = getTilt()[2];
    if (zTilt > minimumTilit && zTilt < maximumTilt) {
      return true;
    } else {
      return false;
    }
  }

  public double getEncDistance() {
    return enc.getDistance();
  }

  public double[] getTilt() {
    double x = accel.getX();
    double y = accel.getY();
    double z = accel.getZ();
    double[] tilts = {x, y, z};
    return tilts;
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new Climb(this));
  }
}
