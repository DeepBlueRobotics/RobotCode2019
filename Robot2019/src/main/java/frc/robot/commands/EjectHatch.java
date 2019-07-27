package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Intake;

public class EjectHatch extends Command {
    Timer timer;
    Intake intake;
    final double DURATION = 0.5;

    public EjectHatch(Intake intake) {
        requires(intake);
        this.intake = intake;
        timer = new Timer();
    }

    @Override
    protected void initialize() {
        timer.reset();
        timer.start();
    }

    @Override
    protected void execute() {
        intake.ejectHatch();
    }

    @Override
    protected boolean isFinished() {
        return timer.get() > DURATION;
    }

    @Override
    protected void end() {
        intake.stopRollers();
        intake.setState(Intake.State.NONE);
        if (isFinished()) {
            SmartDashboard.putBoolean("Has Hatch", false);
        }
    }

    @Override
    protected void interrupted() {
        end();
    }
}