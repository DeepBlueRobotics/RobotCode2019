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
import frc.robot.OI;
import frc.robot.commands.TeleopDrive;

public class Drivetrain extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  public OI oi;

  private WPI_TalonSRX leftMaster, rightMaster;
  private WPI_VictorSPX leftSlave1, leftSlave2, rightSlave1, rightSlave2;
  
  // Remember to restrict speedConstants between -1 and 1.
  private double leftK, rightK;

  public Drivetrain(OI oi, WPI_TalonSRX l1, WPI_VictorSPX l2, WPI_VictorSPX l3, WPI_TalonSRX r1, WPI_VictorSPX r2, WPI_VictorSPX r3) {
    this.oi = oi;
    leftMaster = l1;
    leftSlave1 = l2;
    leftSlave2 = l3;
    rightMaster = r1;
    rightSlave1 = r2;
    rightSlave2 = r3;

    leftK = 1.0;
    rightK = 1.0;
  }

  public void TeleopDrive(double left_speed, double right_speed) {
    // Assumed to be in arcade drive
    leftMaster.set(Math.signum(left_speed) * leftK * left_speed * left_speed);
    rightMaster.set(Math.signum(right_speed) * rightK * right_speed * right_speed);
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new TeleopDrive(this));
  }

}
