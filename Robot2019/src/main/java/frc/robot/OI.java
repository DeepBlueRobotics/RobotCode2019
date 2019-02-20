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
import frc.robot.commands.NormalDrive;
import frc.robot.commands.Climb;
import frc.robot.commands.EjectCargo;
import frc.robot.commands.IntakeOnlyCargo;
import frc.robot.commands.ManualClimb;
import frc.robot.commands.SetArcadeOrTank;
import frc.robot.commands.SlowDrive;
import frc.robot.commands.ToggleCamera;
import frc.robot.commands.ToggleHatch;
import frc.robot.commands.WobbleDrive;
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
  JoystickButton arcadeOrTankBtn;
  JoystickButton normDriveBtn;
  JoystickButton toggleHatchBtn;
  JoystickButton cargoIntakeBtn, cargoEjectBtn;
  JoystickButton climberRailBtn;
  JoystickButton climbBtn;
  JoystickButton toggleCameraBtn;
  JoystickButton wobbleDriveBtn;

  OI(Drivetrain dt, HatchPanel hp, Cargo cargo, Climber climber, UsbCamera driveCamera, UsbCamera hatchCamera,
      VideoSink cameraServer) {
    leftJoy = new Joystick(0);
    rightJoy = new Joystick(1);
    manipulator = new Joystick(2);

    leftSlowBtn = new JoystickButton(leftJoy, 1);
    leftSlowBtn.whileHeld(new SlowDrive(SlowDrive.Side.LEFT));
    rightSlowBtn = new JoystickButton(rightJoy, 1);
    rightSlowBtn.whileHeld(new SlowDrive(SlowDrive.Side.RIGHT));
    wobbleDriveBtn = new JoystickButton(rightJoy, 4); // TODO: confirm button with drivers
    wobbleDriveBtn.whileHeld(new WobbleDrive(dt));

    arcadeOrTankBtn = new JoystickButton(leftJoy, 4);
    arcadeOrTankBtn.whenPressed(new SetArcadeOrTank());
    normDriveBtn = new JoystickButton(leftJoy, 3);
    normDriveBtn.whileHeld(new NormalDrive());

    toggleHatchBtn = new JoystickButton(manipulator, Manip.X); // TODO: set ports to correct values
    toggleHatchBtn.whenPressed(new ToggleHatch(hp));

    cargoIntakeBtn = new JoystickButton(manipulator, Manip.A); // TODO: set ports to correct values
    cargoIntakeBtn.whenPressed(new IntakeOnlyCargo(cargo, hp, dt));
    cargoEjectBtn = new JoystickButton(manipulator, Manip.B); // TODO: set ports to correct values
    cargoEjectBtn.whenPressed(new EjectCargo(cargo));

    climberRailBtn = new JoystickButton(manipulator, Manip.LB_lShoulder); // TODO: confirm button number
    climberRailBtn.whenPressed(new ActuateClimberRails(climber));

    climbBtn = new JoystickButton(manipulator, Manip.Y); // TODO: confirm button number
    climbBtn.toggleWhenPressed(new ManualClimb(climber, dt, leftJoy, rightJoy));

    toggleCameraBtn = new JoystickButton(leftJoy, 2);
    toggleCameraBtn.whenPressed(new ToggleCamera(driveCamera, hatchCamera, cameraServer));
  }

  private class Manip {
    static final int X = 1, A = 2, B = 3, Y = 4, LB_lShoulder = 5, RB_rShoulder = 6, LT_lTrigger = 7, RT_rTrigger = 8,
        BACK = 9, START = 10;

    // Front four buttons look like:
    // Y
    // X B
    // A
  }
}
