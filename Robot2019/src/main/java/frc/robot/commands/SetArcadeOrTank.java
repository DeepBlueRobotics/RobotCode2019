package frc.robot.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SetArcadeOrTank extends InstantCommand {
    public SetArcadeOrTank() {

    }

    protected void initialize() {
        if (SmartDashboard.getBoolean("Arcade Drive", true)) {
            SmartDashboard.putBoolean("Arcade Drive", false);
        } else {
            SmartDashboard.putBoolean("Arcade Drive", true);
        }

    }
}