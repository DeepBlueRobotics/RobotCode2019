package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.Intake;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class KeepWrist extends Command {
    private Intake intake;
    private double position;
    private double prevFF;

    public KeepWrist(Intake intake) {
        this.intake = intake;
        requires(intake);

        if (SmartDashboard.containsKey("Wrist Goal (rotations)")) {
            SmartDashboard.putNumber("Wrist Goal (rotations)", 0);
        }
    }

    @Override
    protected void execute() {
        position = intake.getWristGoal();
        //intake.setWristGoal(position);
        SmartDashboard.putNumber("Wrist Position", intake.getWristPosition());

        intake.setWristGoal(SmartDashboard.getNumber("Wrist Goal (rotations)", 0));
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}