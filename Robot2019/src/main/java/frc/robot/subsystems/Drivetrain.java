/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.commands.TeleopDrive;

public class Drivetrain extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  private WPI_TalonSRX leftMaster, rightMaster;
  private WPI_VictorSPX leftSlave1, leftSlave2, rightSlave1, rightSlave2;

  // NOTE: Change these names for both Arcade and Tank Drive
  private double speed;
  private double rotation;

  public Drivetrain(WPI_TalonSRX l1, WPI_VictorSPX l2, WPI_VictorSPX l3, WPI_TalonSRX r1, WPI_VictorSPX r2,
      WPI_VictorSPX r3) {
    leftMaster = l1;
    rightMaster = r1;
    leftSlave1 = l2;
    leftSlave2 = l3;
    rightSlave1 = r2;
    rightSlave2 = r3;
  }

  public void TeleopDrive() {
    // NOTE: The problem with this code is that it relies on the driver
    // to not touch the right joystick if we want the robot to go forward
    if (rotation == 0.0) {

    } else if (rotation > 0.0) {
    } else if (rotation < 0.0) {
    }
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new TeleopDrive(this));
  }

}
