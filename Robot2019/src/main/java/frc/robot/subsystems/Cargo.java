/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Cargo extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  private VictorSP roller;
  private final PowerDistributionPanel pdp;

  public Cargo(VictorSP roller, PowerDistributionPanel pdp) {
      this.roller = roller;
      this.pdp = pdp;
  }

  public void stopIntake() {
    roller.stopMotor();
  }

  public void runIntake(double speed) {
    roller.set(speed);
  }

  public void runOutake(double speed) {
    roller.set(-speed);
  }

  public boolean hasCargo() {
    return pdp.getCurrent(4) > 15;
    //TODO: set ports to actual cargo motor port
}

  @Override
  public void initDefaultCommand() {
  }
}