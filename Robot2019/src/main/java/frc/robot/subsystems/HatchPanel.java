/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;


public class HatchPanel extends Subsystem {
  private DoubleSolenoid pistons;

  /**
   * Subsystem for controlling the hatch panel mechanism
   * 
   * @param pistons the solenoid that controls all three pistons on the mechanism
   */
  public HatchPanel(DoubleSolenoid pistons) {
    this.pistons = pistons;
  }

  /**
   * toggles the hatch panel pistons
   * 
   * @return if the pistons are extended after the call
   */
  public boolean toggle() {
    if (pistons.get() == DoubleSolenoid.Value.kForward) {
      pistons.set(DoubleSolenoid.Value.kReverse);
      return false;
    } else {
      pistons.set(DoubleSolenoid.Value.kForward);
      return true;
    }
  }

  @Override
  public void initDefaultCommand() {}
}
