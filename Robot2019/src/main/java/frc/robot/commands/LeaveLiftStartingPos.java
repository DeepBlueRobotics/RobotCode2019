package frc.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.robot.subsystems.Lift;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Intake;

public class LeaveLiftStartingPos extends CommandGroup {
    public LeaveLiftStartingPos(Lift lift, Intake intake) {
        System.out.println("Leaving starting pos");
        addSequential(new IntakeHatch2(intake));
        addSequential(new MoveLift(lift, 37));
        addSequential(new MoveIntakeAtStart(intake));
        addSequential(new MoveWrist(intake, Intake.WristPosition.DEFAULT));
        addSequential(new WaitTime(1));
        addSequential(new MoveLift(lift, Lift.Position.HATCH_1));
    }
}