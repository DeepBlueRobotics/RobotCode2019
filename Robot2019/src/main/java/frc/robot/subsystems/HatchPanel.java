/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class HatchPanel extends Subsystem {
  private DoubleSolenoid grabPiston, ejectPistons;
  public State state;

  /**
   * Subsystem for controlling the hatch panel mechanism
   * 
   * @param grabPiston the center piston that is used to grab the hatch panel through the hole
   * @param ejectPistons the two side pistons that facilitate ejecting the hatch panel
   */
  public HatchPanel(DoubleSolenoid grabPiston, DoubleSolenoid ejectPistons) {
    this.grabPiston = grabPiston;
    this.ejectPistons = ejectPistons;

    reset();

    SmartDashboard.putString("Hatch Piston State", state.name());
  }

  public void reset() {
    grabPiston.set(DoubleSolenoid.Value.kReverse);
    ejectPistons.set(DoubleSolenoid.Value.kReverse);
    state = State.DEFAULT;
  }

  public void grab() {
    grabPiston.set(DoubleSolenoid.Value.kForward);
    ejectPistons.set(DoubleSolenoid.Value.kReverse);
    state = State.GRABBING;
  }

  public void eject() {
    grabPiston.set(DoubleSolenoid.Value.kReverse);
    ejectPistons.set(DoubleSolenoid.Value.kForward);
    state = State.EJECTING;
  }

  @Override
  public void initDefaultCommand() {}

  public enum State {
    DEFAULT,
    GRABBING,
    EJECTING
  }
}
