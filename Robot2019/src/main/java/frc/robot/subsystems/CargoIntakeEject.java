/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Subsystem;

public class CargoIntakeEject extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  private WPI_VictorSPX leftMotor, rightMotor;
  private final PowerDistributionPanel pdp;

  public CargoIntakeEject(WPI_VictorSPX leftMotor, WPI_VictorSPX rightMotor, PowerDistributionPanel pdp) {
      this.leftMotor = leftMotor;
      this.rightMotor = rightMotor;
      this.pdp = pdp;
  }

  public void stopIntake() {
    leftMotor.stopMotor();
    rightMotor.stopMotor();
  }

  public void runIntake(double speed) {
    leftMotor.set(speed);
    rightMotor.set(-speed);
  }

  public void runOutake(double speed) {
    leftMotor.set(-speed);
    rightMotor.set(speed);
  }

  public boolean hasCargo() {
    return pdp.getCurrent(4) > 15 || pdp.getCurrent(11) > 39;
}

  @Override
  public void initDefaultCommand() {
  }
}