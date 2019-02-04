/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

import frc.robot.commands.EjectCargo;
import frc.robot.commands.IntakeCargo;
import frc.robot.commands.SlowDrive;
import frc.robot.commands.ToggleCamera;
import frc.robot.subsystems.Cargo;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
  Joystick leftJoy;
  Joystick rightJoy;
  Joystick manipulator;

  JoystickButton leftSlowBtn;
  JoystickButton rightSlowBtn;

  JoystickButton cargoIntakeBtn;
  JoystickButton cargoEjectBtn;

  JoystickButton toggleCameraBtn;

  Cargo cargo;

  OI(Cargo cargo, UsbCamera driveCamera, UsbCamera hatchCamera, VideoSink cameraServer) {
    this.cargo = cargo;

    leftJoy = new Joystick(0); // TODO: set ports to correct values
    rightJoy = new Joystick(1); // TODO: set ports to correct values

    leftSlowBtn = new JoystickButton(leftJoy, 1);
    leftSlowBtn.whileHeld(new SlowDrive(SlowDrive.Side.LEFT));
    rightSlowBtn = new JoystickButton(rightJoy, 1);
    rightSlowBtn.whileHeld(new SlowDrive(SlowDrive.Side.RIGHT));

    cargoIntakeBtn = new JoystickButton(manipulator, 0);
    cargoIntakeBtn.whenPressed(new IntakeCargo(cargo));
    cargoEjectBtn = new JoystickButton(manipulator, 1);
    cargoEjectBtn.whenPressed(new EjectCargo(cargo));

    toggleCameraBtn = new JoystickButton(leftJoy, 2);
    toggleCameraBtn.whenPressed(new ToggleCamera(driveCamera, hatchCamera, cameraServer));
  }
}
