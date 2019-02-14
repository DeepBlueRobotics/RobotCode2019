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
import frc.robot.commands.ActuateClimberRails;
import frc.robot.commands.CharDrive;
import frc.robot.commands.Climb;
import frc.robot.commands.EjectCargo;
import frc.robot.commands.IntakeCargo;
import frc.robot.commands.SlowDrive;
import frc.robot.commands.ToggleCamera;
import frc.robot.commands.ToggleHatch;
import frc.robot.subsystems.Cargo;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.HatchPanel;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
  Joystick leftJoy, rightJoy, manipulator;

  JoystickButton leftSlowBtn, rightSlowBtn;
  JoystickButton charDriveBtn;
  JoystickButton toggleHatchBtn;
  JoystickButton cargoIntakeBtn, cargoEjectBtn;
  JoystickButton climberRailBtn;
  JoystickButton climbBtn;
  JoystickButton toggleCameraBtn;

  OI(Drivetrain dt, HatchPanel hp, Cargo cargo, Climber climber, UsbCamera driveCamera, UsbCamera hatchCamera,
      VideoSink cameraServer) {
    leftJoy = new Joystick(0);
    rightJoy = new Joystick(1);
    manipulator = new Joystick(2);

    leftSlowBtn = new JoystickButton(leftJoy, 1);
    leftSlowBtn.whileHeld(new SlowDrive(SlowDrive.Side.LEFT));
    rightSlowBtn = new JoystickButton(rightJoy, 1);
    rightSlowBtn.whileHeld(new SlowDrive(SlowDrive.Side.RIGHT));

    charDriveBtn = new JoystickButton(leftJoy, 3);
    charDriveBtn.whileHeld(new CharDrive());

    toggleHatchBtn = new JoystickButton(manipulator, 1); // TODO: set ports to correct values
    toggleHatchBtn.whenPressed(new ToggleHatch(hp));

    cargoIntakeBtn = new JoystickButton(manipulator, 2); // TODO: set ports to correct values
    cargoIntakeBtn.whenPressed(new IntakeCargo(cargo));
    cargoEjectBtn = new JoystickButton(manipulator, 3); // TODO: set ports to correct values
    cargoEjectBtn.whenPressed(new EjectCargo(cargo));

    climberRailBtn = new JoystickButton(manipulator, 5); // TODO: confirm button number
    climberRailBtn.whenPressed(new ActuateClimberRails(climber));

    climbBtn = new JoystickButton(manipulator, 4); // TODO: confirm button number
    climbBtn.whenPressed(new Climb(climber, dt));

    toggleCameraBtn = new JoystickButton(leftJoy, 2);
    toggleCameraBtn.whenPressed(new ToggleCamera(driveCamera, hatchCamera, cameraServer));
  }
}
