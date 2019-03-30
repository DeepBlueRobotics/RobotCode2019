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

  private final double intakeCurrentThreshold = 8.0; // Amps
  private final double rollerIntakeSpeed = 6.0 / 12;
  private final double rollerStallSpeed = 3.0 / 12;
  private final double rollerEjectSpeed = 12.0 / 12;

  private VictorSP roller;
  private PowerDistributionPanel pdp;
  private int rollerPort; // The port for the VictorSP on the PDP, not the RoboRIO.

  public Cargo(VictorSP roller, PowerDistributionPanel pdp, int rollerPort) {
    this.roller = roller;
    this.pdp = pdp;
    this.rollerPort = rollerPort;
  }

  public void stopIntake() {
    roller.stopMotor();
  }

  /**
   * keeps the cargo in the mechanism by running the rollers at a slower speed
   */
  public void keepIntake() {
    roller.set(rollerStallSpeed);
  }

  public void runIntake() {
    roller.set(rollerIntakeSpeed);
  }

  public void runEject() {
    roller.set(-1 * rollerEjectSpeed);
  }

  public boolean hasCargo() {
    return pdp.getCurrent(rollerPort) > intakeCurrentThreshold;
  }

  @Override
  public void initDefaultCommand() {
  }
}