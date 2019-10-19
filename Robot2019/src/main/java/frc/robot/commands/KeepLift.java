package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.robot.Robot;
import frc.robot.commands.MoveLift;
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
    private MoveLift cargo1, hatch1, cargo2, hatch2, cargo3, hatch3, cargoShip;
    private double lastPOV;

    public KeepLift(Lift lift, Intake intake, Joystick manip) {
        this.lift = lift;
        this.intake = intake;
        this.manip = manip;
        requires(lift); // doesn't require intake 
        cargo1 = new MoveLift(lift, Lift.Position.CARGO_1);
        hatch1 = new MoveLift(lift, Lift.Position.HATCH_1);
        cargo2 = new MoveLift(lift, Lift.Position.CARGO_2);
        hatch2 = new MoveLift(lift, Lift.Position.HATCH_2);
        cargo3 = new MoveLift(lift, Lift.Position.CARGO_3);
        hatch3 = new MoveLift(lift, Lift.Position.HATCH_3);
        cargoShip = new MoveLift(lift, Lift.Position.CARGO_SHIP);
    }

    @Override
    protected void execute() {
        this.position = lift.getCurrentGoal();
        int pov = manip.getPOV();
        boolean cargo = intake.getState() == State.CARGO;
        switch (pov) {
            case -1: case 45: case 135: case 225: case 315:
                if (lastPOV == 0 || lastPOV == 90 || lastPOV == 180 || lastPOV == 270)
                    lift.setPIDF(Robot.getNumberArray("Lift Keep PIDF", Lift.PIDF.KEEP));
                lift.setGoalPosition(position);
                SmartDashboard.putNumber("Lift Height (in)", lift.getPosition());
                break;
            case 0: // top (2)
                setWristDefault();
                if (lastPOV == 0) {
                    break;
                }
                if (cargo) {
                    Scheduler.getInstance().add(cargo2);
                } else {
                    Scheduler.getInstance().add(hatch2);
                }
                break;
            case 90: // right (3)
                if (lastPOV == 90) {
                    break;
                }
                if (cargo) {
                    Scheduler.getInstance().add(cargo3);
                    intake.setWristGoal(Intake.WristPosition.TOP);
                } else {
                    Scheduler.getInstance().add(hatch3);
                }
                break;
            case 180: // bottom (cargo ship)
                setWristDefault();
                if (lastPOV == 180) {
                    break;
                }
                Scheduler.getInstance().add(cargoShip);
                break;
            case 270: // left (1)
                setWristDefault();
                if (lastPOV == 270) {
                    break;
                }
                if (cargo) {
                    Scheduler.getInstance().add(cargo1);
                } else {
                    Scheduler.getInstance().add(hatch1);
                }
                break;
        }
        lastPOV = pov;
    }

    private void setWristDefault() {
        if(intake.getWristGoal() == Intake.WristPosition.TOP) {
            intake.setWristGoal(Intake.WristPosition.DEFAULT);
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}