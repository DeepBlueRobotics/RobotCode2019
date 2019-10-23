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
import frc.robot.commands.Drive;
import frc.robot.lib.Limelight;
import frc.robot.subsystems.Cargo;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.HatchPanel;
import frc.robot.subsystems.Lights;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends TimedRobot {
  private static Drivetrain dt;
  private static HatchPanel hp;
  private static OI oi;
  private static Limelight lime;
  private static Cargo cargo;
  private static Climber climber;
  private static Lights lights;
  private static String fname1, fname2, fname3, fname4;
  private static Timer timey;

  @Override
  public void robotInit() {
    dt = new Drivetrain(RobotMap.leftMaster, RobotMap.leftSlave1, RobotMap.leftSlave2, RobotMap.rightMaster,
        RobotMap.rightSlave1, RobotMap.rightSlave2, RobotMap.leftEnc, RobotMap.rightEnc, RobotMap.ahrs);
    hp = new HatchPanel(RobotMap.hatchGrabberPiston, RobotMap.hatchEjectPistons);
    cargo = new Cargo(RobotMap.cargoRoller, RobotMap.pdp, RobotMap.cargoPDPPort);
    climber = new Climber(RobotMap.climberMotor, RobotMap.climberEncoder, RobotMap.ahrs, RobotMap.climberPistons);
    lights = new Lights(RobotMap.lights);
    oi = new OI(dt, hp, cargo, climber, lights, RobotMap.driveCamera, RobotMap.hatchCamera, RobotMap.cameraServer);
    lime = new Limelight();

    fname1 = "/home/lvuser/drive_char_linear_for.csv";
    fname2 = "/home/lvuser/drive_char_stepwise_for.csv";
    fname3 = "/home/lvuser/drive_char_linear_back.csv";
    fname4 = "/home/lvuser/drive_char_stepwise_back.csv";
    IncreaseVoltageLinear ivlf = new IncreaseVoltageLinear(dt, 0.25 / 50, 6.0, fname1, "forward");
    IncreaseVoltageStepwise ivsf = new IncreaseVoltageStepwise(dt, 0.25 / 50, 6.0, fname2, "forward");
    IncreaseVoltageLinear ivlb = new IncreaseVoltageLinear(dt, 0.25 / 50, 6.0, fname3, "backward");
    IncreaseVoltageStepwise ivsb = new IncreaseVoltageStepwise(dt, 0.25 / 50, 6.0, fname4, "backward");
    DrivetrainAnalysis dca = new DrivetrainAnalysis(dt);
    SmartDashboard.putData("Increase Voltage Linearly Forward", ivlf);
    SmartDashboard.putData("Increase Voltage Stepwise Forward", ivsf);
    SmartDashboard.putData("Increase Voltage Linearly Backward", ivlb);
    SmartDashboard.putData("Increase Voltage Stepwise Backward", ivsb);
    SmartDashboard.putData("Drivetrain Characterization Analysis", dca);

    dt.setDefaultCommand(new Drive(dt, lime, oi.leftJoy, oi.rightJoy));
    SmartDashboard.putNumber("Max Acceleration", dt.getMaxSpeed() / 1.0);

    SmartDashboard.putBoolean("Outreach Mode", false);
    SmartDashboard.putBoolean("Using Limelight", false);
    timey = new Timer();
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
    timey.reset();
    timey.start();
    hp.grab();
  }

  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();

    SmartDashboard.putNumber("Time left", (int) (15 - timey.get() + 1));
  }

  @Override
  public void teleopInit() {
    timey.reset();
    timey.start();
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
  }

  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();

    SmartDashboard.putNumber("Time left", (int) (135 - timey.get() + 1));
  }

  @Override
  public void testPeriodic() {
  }
}
