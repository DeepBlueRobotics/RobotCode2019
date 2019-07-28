package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.robot.subsystems.Lift;
import frc.robot.subsystems.Intake.State;
import frc.robot.subsystems.Intake;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;

public class KeepLift extends Command {
    private Lift lift;
    private Intake intake;
    private double position;
    private Joystick manip;

    public KeepLift(Lift lift, Intake intake, Joystick manip) {
        this.lift = lift;
        this.intake = intake;
        this.manip = manip;
        requires(lift); // doesn't require intake 
        this.position = lift.getCurrentGoal();
    }

    @Override
    protected void execute() {
        int pov = manip.getPOV();
        boolean cargo = intake.getState() == State.CARGO;
        switch (pov) {
            case -1:
                lift.setGoalPosition(position);
                SmartDashboard.putNumber("Lift Height (in)", lift.getPosition());
                break;
            case 0: // top (2)
                if (cargo) {
                    Scheduler.getInstance().add(new MoveLift(lift, Lift.Position.CARGO_2));
                } else {
                    Scheduler.getInstance().add(new MoveLift(lift, Lift.Position.HATCH_2));
                }
                break;
            case 90: // right (3)
                if (cargo) {
                    Scheduler.getInstance().add(new MoveLift(lift, Lift.Position.CARGO_3));
                } else {
                    Scheduler.getInstance().add(new MoveLift(lift, Lift.Position.HATCH_3));
                }
                break;
            case 180: // bottom (cargo ship)
                Scheduler.getInstance().add(new MoveLift(lift, Lift.Position.CARGO_SHIP));
                break;
            case 270: // left (1)
                if (cargo) {
                    Scheduler.getInstance().add(new MoveLift(lift, Lift.Position.CARGO_1));
                } else {
                    Scheduler.getInstance().add(new MoveLift(lift, Lift.Position.HATCH_1));
                }
                break;
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}