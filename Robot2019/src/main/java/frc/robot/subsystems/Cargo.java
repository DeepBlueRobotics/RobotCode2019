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
  private PowerDistributionPanel pdp;
  private int rollerPort;   // The port for the VictorSP on the PDP, not the RoboRIO.

  public Cargo(VictorSP roller, PowerDistributionPanel pdp, int rollerPort) {
    this.roller = roller;
    this.pdp = pdp;
    this.rollerPort = rollerPort;
  }

  public void stopIntake() {
    roller.stopMotor();
  }

  public void runIntake() {
    roller.set(-1);
  }

  public void runEject() {
    roller.set(1);
  }

  public boolean hasCargo() {
    return pdp.getCurrent(rollerPort) > 15;
    // TODO: set ports to actual cargo motor port
  }

  @Override
  public void initDefaultCommand() {
  }
}