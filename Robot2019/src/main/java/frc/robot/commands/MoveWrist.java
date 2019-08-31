package frc.robot.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.subsystems.Intake;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class MoveWrist extends InstantCommand {
    private Intake intake;
    private double goal;

    public MoveWrist(Intake intake, double goal) {
        this.intake = intake;
        requires(intake);
        this.goal = goal;
    }

    protected void initialize() {
        intake.setWristPosition(goal); // arb ff included in method
        SmartDashboard.putNumber("Wrist Goal", goal);
        SmartDashboard.putNumber("Wrist Position", intake.getWristPosition());
    }
}