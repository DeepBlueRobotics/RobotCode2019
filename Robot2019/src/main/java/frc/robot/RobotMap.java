/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.VictorSP;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
  static WPI_TalonSRX leftMaster, rightMaster;
  static BaseMotorController leftSlave1, leftSlave2, rightSlave1, rightSlave2;
  static VictorSP climberMotor;
  static Encoder climberEncoder;
  static DoubleSolenoid climberPistons;
  static DoubleSolenoid hatchPistons;
  static VictorSP cargoRoller;
  static Encoder leftEnc, rightEnc;
  static String driveMode;
  static AHRS ahrs;
  static PowerDistributionPanel pdp;
  static UsbCamera driveCamera, hatchCamera;
  static VideoSink cameraServer;

  final static int cargoPDPPort;
  final static int climberPDPPort;

  static {
    // Initialize motors on the left side of the drivetrain.
    leftMaster = createConfiguredTalon(8);
    leftSlave1 = createConfiguredMotorController(9);
    leftSlave2 = createConfiguredMotorController(10);

    // Initialize motors on the right side of the drivetrain.
    rightMaster = createConfiguredTalon(5);
    rightSlave1 = createConfiguredMotorController(6);
    rightSlave2 = createConfiguredMotorController(7);

    // Initialize motors on the climbing mech
    climberMotor = new VictorSP(1);
    climberEncoder = new Encoder(new DigitalInput(4), new DigitalInput(5));
    climberPistons = new DoubleSolenoid(6, 1);

    // Initialize motors on the cargo mech
    cargoRoller = new VictorSP(0);

    // Initialize solenoid on hatch panel mech
    hatchPistons = new DoubleSolenoid(7, 0); // 7 is A/Forward, 0 is B/Reverse

    leftEnc = new Encoder(new DigitalInput(0), new DigitalInput(1));
    rightEnc = new Encoder(new DigitalInput(2), new DigitalInput(3));

    ahrs = new AHRS(SPI.Port.kMXP);
    pdp = new PowerDistributionPanel();

    // Initialize cameras
    driveCamera = configureCamera(0);
    hatchCamera = configureCamera(1);
    cameraServer = CameraServer.getInstance().getServer();
    cameraServer.setSource(driveCamera);

    cargoPDPPort = 5; // TODO: set ports to actual cargo motor port in pdp
    climberPDPPort = 3;
  }

  private static BaseMotorController createConfiguredMotorController(int port) {
    BaseMotorController mc = new WPI_VictorSPX(port);

    // Put all configurations for the talon motor controllers in here.
    // All values are from last year's code.
    ErrorCode e = mc.configNominalOutputForward(0, 10);
    if (e == ErrorCode.OK) {
      mc = createConfiguredVictor(port);
    } else {
      mc = createConfiguredTalon(port);
    }

    return mc;
  }

  private static WPI_TalonSRX createConfiguredTalon(int port) {
    WPI_TalonSRX tsrx = new WPI_TalonSRX(port);
    // Put all configurations for the talon motor controllers in here.
    // All values are from last year's code.
    catchError(tsrx.configNominalOutputForward(0, 10));
    catchError(tsrx.configNominalOutputReverse(0, 10));
    catchError(tsrx.configPeakOutputForward(1, 10));
    catchError(tsrx.configPeakOutputReverse(-1, 10));
    catchError(tsrx.configPeakCurrentLimit(0, 0));
    catchError(tsrx.configPeakCurrentDuration(0, 0));
    // 40 Amps is the amp limit of a CIM. lThe PDP has 40 amp circuit breakers,
    catchError(tsrx.configContinuousCurrentLimit(40, 0));
    tsrx.enableCurrentLimit(true);
    catchError(tsrx.configNeutralDeadband(0.001, 10));
    tsrx.setNeutralMode(NeutralMode.Brake);

    return tsrx;
  }

  private static WPI_VictorSPX createConfiguredVictor(int port) {
    WPI_VictorSPX vspx = new WPI_VictorSPX(port);

    // Put all configurations for the victor motor controllers in here.
    catchError(vspx.configNominalOutputForward(0, 10));
    catchError(vspx.configNominalOutputReverse(0, 10));
    catchError(vspx.configPeakOutputForward(1, 10));
    catchError(vspx.configPeakOutputReverse(-1, 10));
    catchError(vspx.configNeutralDeadband(0.001, 10));
    vspx.setNeutralMode(NeutralMode.Brake);

    return vspx;
  }

  private static UsbCamera configureCamera(int port) {
    UsbCamera camera = CameraServer.getInstance().startAutomaticCapture(port);
    camera.setConnectionStrategy(VideoSource.ConnectionStrategy.kKeepOpen);
    return camera;
  }

  /**
   * Checks an error code and prints it to standard out if it is not ok
   * @param ec The error code to check
   */
  private static void catchError(ErrorCode ec) {
    if(ec != ErrorCode.OK) System.out.println("Error configuring in RobotMap.java at line: " + new Throwable().getStackTrace()[1].getLineNumber());
  }
}
