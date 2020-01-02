/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.DrivetrainAnalysis;
import frc.robot.commands.IncreaseVoltageLinear;
import frc.robot.commands.IncreaseVoltageStepwise;
import frc.robot.commands.TeleopDrive;
import frc.robot.commands.KeepLift;
import frc.robot.commands.KeepWrist;
import frc.robot.commands.LeaveLiftStartingPos;
import frc.robot.subsystems.Cargo;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Lift;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.HatchPanel;
import frc.robot.subsystems.Lights;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends TimedRobot {
  private static Drivetrain dt;
  private static HatchPanel hp;
  private static OI oi;
  @SuppressWarnings("unused")
  private static Cargo cargo;
  private static Intake intake;
  private static Lift lift;
  private static Climber climber;
  private static Lights lights;
  private static String fname1, fname2, fname3, fname4;
  private static Timer timey;
  private static boolean osp;

  @Override
  public void robotInit() {
    osp = false;
    dt = new Drivetrain(RobotMap.leftMaster, RobotMap.leftSlave1, RobotMap.leftSlave2, RobotMap.rightMaster,
        RobotMap.rightSlave1, RobotMap.rightSlave2, RobotMap.leftEnc, RobotMap.rightEnc, RobotMap.ahrs);
    // hp = new HatchPanel(RobotMap.hatchGrabberPiston, RobotMap.hatchEjectPistons);
    // cargo = new Cargo(RobotMap.cargoRoller, RobotMap.pdp, RobotMap.cargoPDPPort);
    intake = new Intake(RobotMap.intakeWristMotor, RobotMap.intakeTopMotor, RobotMap.intakeSideMotor, RobotMap.intakePiston);
    lift = new Lift(RobotMap.liftMotor, RobotMap.liftMotor2);
    climber = new Climber(RobotMap.climberMotor, RobotMap.climberEncoder, RobotMap.ahrs, RobotMap.climberPistons);
    lights = new Lights(RobotMap.lights);
    oi = new OI(dt, hp, intake, lift, climber, lights, RobotMap.driveCamera, RobotMap.hatchCamera, RobotMap.cameraServer);

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

    dt.setDefaultCommand(new TeleopDrive(dt, oi.leftJoy, oi.rightJoy));
    SmartDashboard.putNumber("Max Acceleration", dt.getMaxSpeed() / 1.0);

    putNumberArray("Lift Up PIDF", Lift.PIDF.UP);
    putNumberArray("Lift Down PIDF", Lift.PIDF.DOWN);
    putNumberArray("Lift Keep PIDF", Lift.PIDF.KEEP);
    putNumberArray("Wrist PIDF", Intake.PIDF.WRIST);

    lift.setDefaultCommand(new KeepLift(lift, intake, oi.manipulator));
    intake.setDefaultCommand(new KeepWrist(intake));

    SmartDashboard.putBoolean("Outreach Mode", false);
    timey = new Timer();
  }

  public static void checkSparkFaults(CANSparkMax spark) {
    //System.out.println("Checking Faults for Spark: " + spark.getDeviceId());
    for(CANSparkMax.FaultID fault: CANSparkMax.FaultID.values()) {
      boolean fo = spark.getFault(fault);
      if(fo)
        System.err.println("Spark Fault: " + spark.getDeviceId() + " Fault: " + fault.name() + " Value: " + fo);
    }
    for(CANSparkMax.FaultID fault: CANSparkMax.FaultID.values()) {
      boolean fo = spark.getStickyFault(fault);
      if(fo)
        System.err.println("Spark Sticky Fault: " + spark.getDeviceId() + " Fault: " + fault.name() + " Value: " + fo);
    }
    spark.clearFaults();
  }

  public void putNumberArray(String keyBase, double[] value) {
    for(int i = 0; i < value.length; i++) {
      SmartDashboard.putNumber(keyBase + "[" + i + "]", value[i]);
    }
  }

  public static double[] getNumberArray(String keyBase, double[] defaultValue) {
    double[] out = new double[defaultValue.length];
    for(int i = 0; i < defaultValue.length; i++) {
      out[i] = SmartDashboard.getNumber(keyBase + "[" + i + "]", defaultValue[i]);
    }
    return out;
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
    SmartDashboard.putNumber("TestingEncoder1",RobotMap.liftMotor.getEncoder().getPosition());
    SmartDashboard.putNumber("TestingEncoder2",RobotMap.liftMotor2.getEncoder().getPosition());
    checkSparkFaults(RobotMap.liftMotor);
    checkSparkFaults(RobotMap.liftMotor2);
    
  }

  @Override
  public void disabledInit() {
    // cargo.stopIntake();
    SmartDashboard.putNumber("Lift Test Speed", 0.0);
    lift.setGoalPosition(Lift.BOTTOM_HEIGHT);
    RobotMap.liftMotor.set(0);
  }

  @Override
  public void disabledPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void autonomousInit() {
    timey.reset();
    timey.start();
    Scheduler.getInstance().add(new LeaveLiftStartingPos(lift, intake));
    osp = true;
    // hp.grab();
  }

  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();
    debug(RobotMap.liftMotor);
    debug(RobotMap.liftMotor2);
    SmartDashboard.putNumber("Time left", (int) (15 - timey.get() + 1));
  }

  public void debug(CANSparkMax spark) {
    int id = spark.getDeviceId();
    SmartDashboard.putNumber("Spark: " + id + " Applied Output", spark.getAppliedOutput());
    SmartDashboard.putNumber("Spark: " + id + " Output Current", spark.getOutputCurrent());
    SmartDashboard.putNumber("Spark: " + id + " Bus Voltage", spark.getBusVoltage());
  }

  @Override
  public void teleopInit() {
    timey.reset();
    timey.start();
    if(!osp) {
      Scheduler.getInstance().add(new LeaveLiftStartingPos(lift, intake));
      osp = true;
    }
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
