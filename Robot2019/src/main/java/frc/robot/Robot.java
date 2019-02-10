/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.IncreaseVoltageLinear;
import frc.robot.commands.IncreaseVoltageStepwise;
import frc.robot.commands.TeleopDrive;
import frc.robot.subsystems.Cargo;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.HatchPanel;

public class Robot extends TimedRobot {
  private static Drivetrain dt;
  private static HatchPanel hp;
  private static OI oi;
  private static Cargo cargo;

  Command autonomousCommand;
  SendableChooser<Command> chooser = new SendableChooser<>();

  @Override
  public void robotInit() {
    hp = new HatchPanel(RobotMap.hatchPistons);
    cargo = new Cargo(RobotMap.cargoRoller, RobotMap.pdp);
    
    oi = new OI(cargo, hp, RobotMap.driveCamera, RobotMap.hatchCamera, RobotMap.cameraServer);

    dt = new Drivetrain(RobotMap.leftMaster, RobotMap.leftSlave1, RobotMap.leftSlave2, RobotMap.rightMaster,
        RobotMap.rightSlave1, RobotMap.rightSlave2, oi.leftJoy, oi.rightJoy, RobotMap.leftEnc, RobotMap.rightEnc,
        RobotMap.gyro);

    chooser.setDefaultOption("Default Auto", new TeleopDrive(dt));
    SmartDashboard.putData("Auto Mode", chooser);

    String fname = "/home/lvuser/drive_char.csv";
    SmartDashboard.putData(new IncreaseVoltageLinear(dt, 0.25 / 50, fname));
		SmartDashboard.putData(new IncreaseVoltageStepwise(dt, 6.0, fname));
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like diagnostics that you want ran during disabled, autonomous,
   * teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void autonomousInit() {
    autonomousCommand = chooser.getSelected();

    if (autonomousCommand != null) {
      autonomousCommand.start();
    }
  }

  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (autonomousCommand != null) {
      autonomousCommand.cancel();
    }
  }

  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void testPeriodic() {
  }
}
