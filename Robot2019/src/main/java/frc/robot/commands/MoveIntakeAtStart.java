package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.robot.subsystems.Lift;
import frc.robot.subsystems.Intake;
import edu.wpi.first.wpilibj.Timer;

public class MoveIntakeAtStart extends Command {
    Intake intake;
    Timer timer;

    public MoveIntakeAtStart(Intake intake) {
        this.intake = intake;
        requires(intake);
        timer = new Timer();
    }

    protected void initialize() {
        timer.start();
    }

    protected void execute() {
        intake.setTopSpeed(0.5);
    }

    protected boolean isFinished() {
        return timer.get() > 1;
    }

    protected void end() {
        intake.setTopSpeed(0);
    }
}