/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.DrivetrainAnalysis;
import frc.robot.commands.IncreaseVoltageLinear;
import frc.robot.commands.IncreaseVoltageStepwise;
import frc.robot.commands.TeleopDrive;
import frc.robot.subsystems.Cargo;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.HatchPanel;

public class Robot extends TimedRobot {
  private static Drivetrain dt;
  private static HatchPanel hp;
  private static OI oi;
  private static Cargo cargo;
  private static Climber climber;

  @Override
  public void robotInit() {
    String[] in_files = {"/home/lvuser/drive_char_linear_for.csv", "/home/lvuser/drive_char_stepwise_for.csv",
                       "/home/lvuser/drive_char_linear_back.csv", "/home/lvuser/drive_char_stepwise_back.csv"};
    String outfile = "/home/lvuser/drive_char_params.csv";

    dt = new Drivetrain(RobotMap.leftMaster, RobotMap.leftSlave1, RobotMap.leftSlave2, RobotMap.rightMaster,
        RobotMap.rightSlave1, RobotMap.rightSlave2, RobotMap.leftEnc, RobotMap.rightEnc, RobotMap.ahrs, outfile);
    hp = new HatchPanel(RobotMap.hatchPistons);
    cargo = new Cargo(RobotMap.cargoRoller, RobotMap.pdp, RobotMap.cargoPDPPort);
    climber = new Climber(RobotMap.climberMotor, RobotMap.climberEncoder, RobotMap.ahrs, RobotMap.climberPistons);

    oi = new OI(dt, hp, cargo, climber, RobotMap.driveCamera, RobotMap.hatchCamera, RobotMap.cameraServer);

    double step = 0.25 / 50;
    double voltageStep = 6.0;

    IncreaseVoltageLinear ivlf = new IncreaseVoltageLinear(dt, step, voltageStep, in_files[0], "forward");
    IncreaseVoltageStepwise ivsf = new IncreaseVoltageStepwise(dt, step, voltageStep, in_files[1], "forward");
    IncreaseVoltageLinear ivlb = new IncreaseVoltageLinear(dt, step, voltageStep, in_files[2], "backward");
    IncreaseVoltageStepwise ivsb = new IncreaseVoltageStepwise(dt, step, voltageStep, in_files[3], "backward");
    DrivetrainAnalysis da = new DrivetrainAnalysis(dt, in_files, outfile);
    SmartDashboard.putData("Increase Voltage Linearly Forward", ivlf);
    SmartDashboard.putData("Increase Voltage Stepwise Forward", ivsf);
    SmartDashboard.putData("Increase Voltage Linearly Backward", ivlb);
    SmartDashboard.putData("Increase Voltage Stepwise Backward", ivsb);
    SmartDashboard.putData("Drivetrain Characterization Analysis", da);

    dt.setDefaultCommand(new TeleopDrive(dt, oi.leftJoy, oi.rightJoy));
    SmartDashboard.putNumber("Max Acceleration", dt.getMaxSpeed() / 1.0);
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
    cargo.stopIntake();
  }

  @Override
  public void disabledPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void autonomousInit() {
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
  }

  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void testPeriodic() {
  }
}
