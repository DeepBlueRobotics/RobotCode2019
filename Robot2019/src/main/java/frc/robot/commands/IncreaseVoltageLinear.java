package frc.robot.commands;

import frc.robot.commands.DrivetrainCharacterization;
import frc.robot.subsystems.Drivetrain;

/* Command which supplies the motors with a desired voltage that increases linearly.
*/
public class IncreaseVoltageLinear extends DrivetrainCharacterization {
    String direction;

    public IncreaseVoltageLinear(Drivetrain dt, double voltStep, double stepwiseVoltage, String filename, String dir) {
        super(dt, voltStep, stepwiseVoltage, filename);
        direction = dir;
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        double throttle = suppliedVoltage / maxVoltage;
        if (direction.equals("backward")) {
            throttle *= -1;
        }
        dt.drive(throttle, throttle);
        writeMeasuredVelocity(fw);

        if (suppliedVoltage + voltStep <= maxVoltage) {
            suppliedVoltage += voltStep;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        if (suppliedVoltage == maxVoltage) {
            return true;
        } else {
            return false;
        }
    }
}