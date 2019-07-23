package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.Lift;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class KeepLift extends Command {
    private Lift lift;
    private double position;

    public KeepLift(Lift lift) {
        this.lift = lift;
        requires(lift);
        this.position = lift.getCurrentGoal();
    }

    @Override
    protected void execute() {
        lift.setGoalPosition(position);
        SmartDashboard.putNumber("Lift Height (in)", lift.getPosition());
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}