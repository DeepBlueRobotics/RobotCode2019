package frc.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.robot.subsystems.Lift;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystem.Intake;

public class LeaveLiftStartingPos extends CommandGroup {
    public LeaveLiftStartingPos(Lift lift, Intake intake) {
        addSequential(new MoveIntakeAtStart(lift, intake));
        addSequential(new MoveWrist(intake, Intake.WristPosition.DEFAULT)); // TODO: add wait time before MoveLift 
        addSequential(new MoveLift(Lift.Position.HATCH_1));
    }
}