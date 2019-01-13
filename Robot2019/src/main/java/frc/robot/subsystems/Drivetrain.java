/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.commands.TeleopDrive;

public class Drivetrain extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  private WPI_TalonSRX leftMaster, rightMaster;
  private WPI_VictorSPX leftSlave1, leftSlave2, rightSlave1, rightSlave2;

  // NOTE: Change this to a SmartDashboard Preference
  private String drive_mode;

  // NOTE: Change these names for both Arcade and Tank Drive
  private double lvalue, rvalue;
  private double leftK, rightK;

  private Joystick leftJoy, rightJoy;

  public Drivetrain(WPI_TalonSRX l1, WPI_VictorSPX l2, WPI_VictorSPX l3, WPI_TalonSRX r1, WPI_VictorSPX r2,
      WPI_VictorSPX r3, Joystick leftJoy, Joystick rightJoy) {
    leftMaster = l1;
    rightMaster = r1;
    leftSlave1 = l2;
    leftSlave2 = l3;
    rightSlave1 = r2;
    rightSlave2 = r3;

    this.leftJoy = leftJoy;
    this.rightJoy = rightJoy;

    drive_mode = "arcade";
    lvalue = 0.0;
    rvalue = 0.0;
    leftK = 1.0;
    rightK = 1.0;
  }

  public void TeleopDrive() {
    lvalue = leftJoy.getY();
    rvalue = rightJoy.getY();
    // NOTE: The problem with this code is that it relies on the driver
    // to not touch the right joystick if we want the robot to go forward
    if (drive_mode.equals("arcade")) {
      if (rvalue == 0.0) {          // Forward / Back
        leftMaster.set(Math.signum(lvalue) * leftK * lvalue * lvalue);
        rightMaster.set(Math.signum(lvalue) * rightK * lvalue * lvalue);
      } else if (rvalue > 0.0) {    // Turn Left
        leftMaster.set(Math.signum(lvalue) * leftK * lvalue * lvalue);
        rightMaster.set(0.0);
      } else if (rvalue < 0.0) {    // Turn Right
        leftMaster.set(0.0);
        rightMaster.set(Math.signum(lvalue) * rightK * lvalue * lvalue);
      }
    }
    else if (drive_mode.equals("tank")) {
        leftMaster.set(Math.signum(lvalue) * leftK * lvalue * lvalue);
        leftMaster.set(Math.signum(rvalue) * rightK * rvalue * rvalue);
    }
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new TeleopDrive(this));
  }

  public void setDriveMode(String drivemode) {
    drive_mode = drivemode;
  }

}
