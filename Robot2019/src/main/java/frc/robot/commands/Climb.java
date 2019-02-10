/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.subsystems.Climber;
import frc.robot.commands.RaiseClimber;
import frc.robot.commands.LowerClimber;

public class Climb extends CommandGroup {
  public Climb(Climber climber) {
    requires(climber);
    // First Phase
    addSequential(new RaiseClimber(climber));
    addSequential(new LowerClimber(climber));
    // Second Phase
    addSequential(new RaiseClimber(climber));
    addSequential(new LowerClimber(climber));
    // Third Phase
    addSequential(new RaiseClimber(climber));
    addSequential(new LowerClimber(climber));
    // Final (Fourth) Phase
    addSequential(new RaiseClimber(climber));
    addSequential(new LowerClimber(climber));
    // After the fourth phase, the driver should be able to drive across the platform.
  }
}