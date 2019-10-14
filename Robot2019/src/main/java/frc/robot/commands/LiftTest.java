package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotMap;
import frc.robot.subsystems.Lift;

public class LiftTest extends Command {
    Lift lift;
    Joystick joy;

    public LiftTest(Lift lift, Joystick joy) {
        this.lift = lift;
        requires(lift);
        this.joy = joy;
    }

    protected void initialize() {
        SmartDashboard.putBoolean("Lift Testing", true);
        SmartDashboard.putNumber("Lift Test Speed", 0.0);
        lift.resetPosition();
    }

    protected void execute() {
        //lift.setSpeed(joy.getY() * 0.1);
        SmartDashboard.putNumber("Lift Position", lift.getPosition());
        double liftSpeed = SmartDashboard.getNumber("Lift Test Speed", 0);
        lift.setSpeed(liftSpeed);
    }

    protected boolean isFinished() {
        return false;
    }
}