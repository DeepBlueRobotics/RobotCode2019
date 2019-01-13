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

  private WPI_TalonSRX left1, right1;
  private WPI_VictorSPX left2, left3, right2, right3;

  public Drivetrain(WPI_TalonSRX l1, WPI_VictorSPX l2, WPI_VictorSPX l3, WPI_TalonSRX r1, WPI_VictorSPX r2,
      WPI_VictorSPX r3) {
    left1 = l1;
    right1 = r1;
    left2 = l2;
    left3 = l3;
    right2 = r2;
    right3 = r3;
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new TeleopDrive(this));
  }

}
