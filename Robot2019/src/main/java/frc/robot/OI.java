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
import frc.robot.commands.ToggleHatch;
import frc.robot.subsystems.HatchPanel;
import frc.robot.commands.ToggleCamera;
import frc.robot.subsystems.Cargo;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
  Joystick leftJoy, rightJoy, manipulator;

  JoystickButton leftSlowBtn, rightSlowBtn;
  JoystickButton toggleHatchBtn;
  JoystickButton cargoIntakeBtn, cargoEjectBtn;

  JoystickButton toggleCameraBtn;

  OI(Cargo cargo, HatchPanel hp, UsbCamera driveCamera, UsbCamera hatchCamera, VideoSink cameraServer) {
    leftJoy = new Joystick(0);
    rightJoy = new Joystick(1);
    manipulator = new Joystick(2);

    leftSlowBtn = new JoystickButton(leftJoy, 1);
    leftSlowBtn.whileHeld(new SlowDrive(SlowDrive.Side.LEFT));
    rightSlowBtn = new JoystickButton(rightJoy, 1);
    rightSlowBtn.whileHeld(new SlowDrive(SlowDrive.Side.RIGHT));

    toggleHatchBtn = new JoystickButton(manipulator, 1); // TODO: set ports to correct values
    toggleHatchBtn.whenPressed(new ToggleHatch(hp));

    cargoIntakeBtn = new JoystickButton(manipulator, 2); // TODO: set ports to correct values
    cargoIntakeBtn.whenPressed(new IntakeCargo(cargo));
    cargoEjectBtn = new JoystickButton(manipulator, 3); // TODO: set ports to correct values
    cargoEjectBtn.whenPressed(new EjectCargo(cargo));

    toggleCameraBtn = new JoystickButton(leftJoy, 2);
    toggleCameraBtn.whenPressed(new ToggleCamera(driveCamera, hatchCamera, cameraServer));
  }
}
