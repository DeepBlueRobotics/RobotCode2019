package frc.robot.commands;

import java.io.FileWriter;

import frc.robot.commands.DrivetrainCharacterization;
import frc.robot.subsystems.Drivetrain;

/* Command which supplies the motors with a desired voltage that increases linearly.
*/
public class IncreaseVoltageLinear extends DrivetrainCharacterization {
    private Drivetrain dt;
    private FileWriter fw;
    public double suppliedVoltage, voltStep, stepwiseVoltage, voltageRuntime, maxVoltage;

    public IncreaseVoltageLinear(Drivetrain dt, double voltStep, double stepwiseVoltage, String filename) {
        super(dt, voltStep, stepwiseVoltage, filename);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        double throttle = suppliedVoltage / maxVoltage;
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