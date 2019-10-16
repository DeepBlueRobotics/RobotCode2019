package frc.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.robot.subsystems.Lift;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Intake;

public class LeaveLiftStartingPos extends CommandGroup {
    public LeaveLiftStartingPos(Lift lift, Intake intake) {
        addSequential(new MoveIntakeAtStart(lift, intake));
        addSequential(new MoveWrist(intake, Intake.WristPosition.DEFAULT));
        addSequential(new WaitTime(3));
        addSequential(new MoveLift(lift, Lift.Position.HATCH_1));
    }
}