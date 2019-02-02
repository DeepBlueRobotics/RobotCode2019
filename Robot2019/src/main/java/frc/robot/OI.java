/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.robot.commands.EjectCargo;
import frc.robot.commands.IntakeCargo;
import frc.robot.commands.SlowDrive;
import frc.robot.subsystems.Cargo;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
  Joystick leftJoy;
  Joystick rightJoy;
  Joystick manipulator;

  JoystickButton leftSlowButton;
  JoystickButton rightSlowButton;

  JoystickButton cargoIn;
  JoystickButton cargoOut;

  Cargo cargo;

  OI(Cargo cargo) {
    this.cargo = cargo;

    leftJoy = new Joystick(0); // TODO: set ports to correct values
    rightJoy = new Joystick(1); // TODO: set ports to correct values

    leftSlowButton = new JoystickButton(leftJoy, 1);
    leftSlowButton.whileHeld(new SlowDrive(SlowDrive.Side.LEFT));
    rightSlowButton = new JoystickButton(rightJoy, 1);
    rightSlowButton.whileHeld(new SlowDrive(SlowDrive.Side.RIGHT));

    cargoIn = new JoystickButton(manipulator, 0);
    cargoIn.whenPressed(new IntakeCargo(cargo));
    cargoOut = new JoystickButton(manipulator, 1);
    cargoOut.whenPressed(new EjectCargo(cargo));
  }
}
