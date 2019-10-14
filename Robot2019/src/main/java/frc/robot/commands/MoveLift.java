package frc.robot.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot;
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
        if (goal > lift.getPosition()) {
            lift.setPIDF(Robot.getNumberArray("Lift Up PIDF", Lift.PIDF.UP));
        } else {
            lift.setPIDF(Robot.getNumberArray("Lift Down PIDF", Lift.PIDF.DOWN));
        }
        lift.setGoalPosition(goal);
        SmartDashboard.putNumber("Lift Goal (in)", goal);
        SmartDashboard.putNumber("Lift Height (in)", lift.getPosition());
    }
}