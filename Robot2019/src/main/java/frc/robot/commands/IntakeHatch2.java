package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Lift;

public class IntakeHatch2 extends Command {
    Timer timer;
    Intake intake;
    Lift lift;
    boolean overdraw;

    public IntakeHatch2(Intake intake, Lift lift) {
        requires(intake);
        requires(lift);
        this.intake = intake;
        this.lift = lift;
        timer = new Timer();
        overdraw = false;
    }

    @Override
    protected void initialize() {
        timer.reset();
        lift.setGoalPosition(Lift.Position.HATCH_1);
        intake.prepareHatch();
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