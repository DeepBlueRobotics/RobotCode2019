package frc.robot.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.subsystems.Lift;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class KeepLift extends InstantCommand {
    private Lift lift;
    private double position;

    public KeepLift(Lift lift) {
        this.lift = lift;
        requires(lift);
        this.position = lift.getLastGoal();
    }

    @Override
    protected void initialize() {
        lift.setGoalPosition(position);
        SmartDashboard.putNumber("Lift Height (in)", lift.getPosition());
    }
}