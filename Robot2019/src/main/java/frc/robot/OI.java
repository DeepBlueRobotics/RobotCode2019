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
import frc.robot.commands.Climb;
import frc.robot.commands.ConfigureWrist;
import frc.robot.commands.EjectCargo;
import frc.robot.commands.EjectHatch;
import frc.robot.commands.GradualDrive;
import frc.robot.commands.IntakeCargo;
import frc.robot.commands.IntakeHatch;
import frc.robot.commands.ManualClimb;
import frc.robot.commands.NormalDrive;
import frc.robot.commands.SlowClimb;
import frc.robot.commands.ResetWobble;
import frc.robot.commands.SetArcadeOrTank;
import frc.robot.commands.SlowDrive;
import frc.robot.commands.ToggleCamera;
import frc.robot.commands.ToggleClimberRails;
import frc.robot.commands.WobbleDrive;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Lift;
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
  JoystickButton slowClimbBtn;
  JoystickButton toggleCameraBtn;
  JoystickButton wobbleDriveBtn;
  JoystickButton cycleLightBtn;
  JoystickButton liftTestBtn;
  JoystickButton wristDownBtn, wristUpBtn;

  OI(Drivetrain dt, HatchPanel hp, Intake intake, Lift lift, Climber climber, Lights lights, UsbCamera driveCamera,
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
    normDriveBtn.whenPressed(new NormalDrive());
    gradDriveBtn = new JoystickButton(leftJoy, 5);
    gradDriveBtn.whenPressed(new GradualDrive());

    hatchIntakeBtn = new JoystickButton(manipulator, Manip.X);
    hatchIntakeBtn.whenPressed(new IntakeHatch(intake, lift, dt));
    hatchEjectBtn = new JoystickButton(manipulator, Manip.Y);
    hatchEjectBtn.whenPressed(new EjectHatch(intake));

    cargoIntakeBtn = new JoystickButton(manipulator, Manip.A); // TODO: set ports to correct values
    cargoIntakeBtn.whenPressed(new IntakeCargo(intake, lift, lights));
    cargoEjectBtn = new JoystickButton(manipulator, Manip.B); // TODO: set ports to correct values
    cargoEjectBtn.whenPressed(new EjectCargo(intake));

    climberRailBtn = new JoystickButton(manipulator, Manip.RB_rShoulder);
    climberRailBtn.whenPressed(new ToggleClimberRails(climber));

    autoClimbBtn = new JoystickButton(manipulator, Manip.LT_lTrigger);
    autoClimbBtn.toggleWhenPressed(new Climb(climber, dt, leftJoy, lights));

    manualClimbBtn = new JoystickButton(manipulator, Manip.RT_rTrigger);
    manualClimbBtn.toggleWhenPressed(new ManualClimb(climber, manipulator, lights));


    slowClimbBtn = new JoystickButton(manipulator, Manip.LB_lShoulder);
    slowClimbBtn.whileHeld(new SlowClimb());

    toggleCameraBtn = new JoystickButton(leftJoy, 2);
    toggleCameraBtn.whenPressed(new ToggleCamera(driveCamera, hatchCamera, cameraServer));

    wristDownBtn = new JoystickButton(manipulator, Manip.BACK);
    wristDownBtn.whenPressed(new ConfigureWrist(intake, -5));
    wristUpBtn = new JoystickButton(manipulator, Manip.START);
    wristUpBtn.whenPressed(new ConfigureWrist(intake, 5));

    /*cycleLightBtn = new JoystickButton(manipulator, Manip.START);
    cycleLightBtn.whenPressed(new ToggleLight(lights));*/

    //liftTestBtn = new JoystickButton(manipulator, Manip.BACK);
    //liftTestBtn.whenPressed(new LiftTest(lift, leftJoy));
  }

  private class Manip {
    static final int X = 1, A = 2, B = 3, Y = 4, LB_lShoulder = 5, RB_rShoulder = 6, LT_lTrigger = 7, RT_rTrigger = 8,
        BACK = 9, START = 10;

    // Front four buttons look like:
    //  Y
    // X B
    //  A
  }
}
