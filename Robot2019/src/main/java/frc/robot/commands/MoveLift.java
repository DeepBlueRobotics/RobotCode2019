package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.Lift;

public class MoveLift extends Command {
    private Lift lift;
    private double goal;

    public MoveLift(Lift lift, double goal) {
        this.lift = lift;
        requires(lift);
        this.goal = goal;
    }
    
    protected void initialize() {

    }

    protected boolean isFinished() {
        return false;
    }
}