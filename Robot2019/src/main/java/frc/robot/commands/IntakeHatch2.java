package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Intake;

public class IntakeHatch2 extends Command {
    Timer timer;
    Intake intake;
    boolean overdraw;

    public IntakeHatch2(Intake intake) {
        requires(intake);
        this.intake = intake;
        timer = new Timer();
        overdraw = false;
    }

    @Override
    protected void initialize() {
        timer.reset();
    }

    @Override
    protected void execute() {
        intake.intakeHatch();
        if (intake.hasHatch()) {
            if (!overdraw) {
                overdraw = true;
                timer.start();
            }
        } else {
            overdraw = false;
            timer.stop();
            timer.reset();
        }
    }

    @Override
    protected boolean isFinished() {
        return (timer.get() > 0.5);
    }

    @Override
    protected void end() {
        if (isFinished()) {
            intake.keepHatch();
            SmartDashboard.putBoolean("Has Hatch", true);
        } else {
            intake.stopRollers();
        }
    }

    @Override
    protected void interrupted() {
        end();
    }
}