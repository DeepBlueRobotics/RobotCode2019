package frc.robot.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.subsystems.Lift;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class MoveLift extends InstantCommand {
    private Lift lift;
    private double goal;

    public MoveLift(Lift lift, double goal) {
        this.lift = lift;
        requires(lift);
        this.goal = goal;
    }
    
    protected void initialize() {
        lift.setGoalPosition(goal);
        SmartDashboard.putNumber("Lift Goal (in)", goal);
    }
}