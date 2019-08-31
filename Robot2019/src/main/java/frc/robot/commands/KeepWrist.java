package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.Intake;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class KeepWrist extends Command {
    private Intake intake;
    private double position;

    public KeepWrist(Intake intake) {
        this.intake = intake;
        requires(intake);
    }

    @Override
    protected void execute() {
        position = intake.getWristGoal();
        intake.setWristPosition(position);
        SmartDashboard.putNumber("Wrist Position", intake.getWristPosition());
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}