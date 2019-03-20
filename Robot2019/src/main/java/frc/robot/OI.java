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
import frc.robot.commands.ToggleClimberRails;
import frc.robot.commands.Climb;
import frc.robot.commands.EjectCargo;
import frc.robot.commands.GradualDrive;
import frc.robot.commands.ToggleHatchEject;
import frc.robot.commands.IntakeCargo;
import frc.robot.commands.ToggleHatchIntake;
import frc.robot.commands.ManualClimb;
import frc.robot.commands.NormalDrive;
import frc.robot.commands.ResetWobble;
import frc.robot.commands.SetArcadeOrTank;
import frc.robot.commands.SlowDrive;
import frc.robot.commands.ToggleCamera;
import frc.robot.commands.ToggleLight;
import frc.robot.commands.WobbleDrive;
import frc.robot.subsystems.Cargo;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.HatchPanel;
import frc.robot.subsystems.Lights;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
  Joystick leftJoy, rightJoy, manipulator;

  JoystickButton leftSlowBtn, rightSlowBtn;
  JoystickButton arcadeOrTankBtn;
  JoystickButton normDriveBtn;
  JoystickButton gradDriveBtn;
  JoystickButton hatchIntakeBtn, hatchEjectBtn;
  JoystickButton cargoIntakeBtn, cargoEjectBtn;
  JoystickButton climberRailBtn;
  JoystickButton autoClimbBtn;
  JoystickButton manualClimbBtn;
  JoystickButton toggleCameraBtn;
  JoystickButton wobbleDriveBtn;
  JoystickButton cycleLightBtn;

  OI(Drivetrain dt, HatchPanel hp, Cargo cargo, Climber climber, Lights lights, UsbCamera driveCamera,
      UsbCamera hatchCamera, VideoSink cameraServer) {
    leftJoy = new Joystick(0);
    rightJoy = new Joystick(1);
    manipulator = new Joystick(2);

    leftSlowBtn = new JoystickButton(leftJoy, 1);
    leftSlowBtn.whileHeld(new SlowDrive(SlowDrive.Side.LEFT));
    rightSlowBtn = new JoystickButton(rightJoy, 1);
    rightSlowBtn.whileHeld(new SlowDrive(SlowDrive.Side.RIGHT));
    wobbleDriveBtn = new JoystickButton(rightJoy, 4); // TODO: confirm button with drivers
    wobbleDriveBtn.whenPressed(new WobbleDrive(dt));
    wobbleDriveBtn.whenReleased(new ResetWobble(dt));

    arcadeOrTankBtn = new JoystickButton(leftJoy, 4);
    arcadeOrTankBtn.whenPressed(new SetArcadeOrTank());
    normDriveBtn = new JoystickButton(leftJoy, 3);
    normDriveBtn.whileHeld(new NormalDrive());
    gradDriveBtn = new JoystickButton(leftJoy, 4); // TODO: confirm button with drivers
    gradDriveBtn.whenPressed(new GradualDrive());

    hatchIntakeBtn = new JoystickButton(manipulator, Manip.X);
    hatchIntakeBtn.whenPressed(new ToggleHatchIntake(hp));
    hatchEjectBtn = new JoystickButton(manipulator, Manip.Y);
    hatchEjectBtn.whenPressed(new ToggleHatchEject(hp));

    cargoIntakeBtn = new JoystickButton(manipulator, Manip.A); // TODO: set ports to correct values
    cargoIntakeBtn.whenPressed(new IntakeCargo(cargo));
    cargoEjectBtn = new JoystickButton(manipulator, Manip.B); // TODO: set ports to correct values
    cargoEjectBtn.whenPressed(new EjectCargo(cargo));

    climberRailBtn = new JoystickButton(manipulator, Manip.LB_lShoulder);
    climberRailBtn.whenPressed(new ToggleClimberRails(climber));

    autoClimbBtn = new JoystickButton(manipulator, Manip.RT_rTrigger);
    autoClimbBtn.toggleWhenPressed(new Climb(climber, dt, leftJoy));

    manualClimbBtn = new JoystickButton(manipulator, Manip.LT_lTrigger);
    manualClimbBtn.toggleWhenPressed(new ManualClimb(climber, manipulator));

    toggleCameraBtn = new JoystickButton(leftJoy, 2);
    toggleCameraBtn.whenPressed(new ToggleCamera(driveCamera, hatchCamera, cameraServer));

    cycleLightBtn = new JoystickButton(manipulator, Manip.START);
    cycleLightBtn.whenPressed(new ToggleLight(lights));
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
