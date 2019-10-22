package frc.robot.commands;

import frc.robot.commands.DrivetrainCharCommand;
import frc.robot.subsystems.Drivetrain;

/* Command which supplies the motors with a desired voltage that increases stepwise.
*/
public class IncreaseVoltageStepwise extends DrivetrainCharCommand {
    String direction;

    public IncreaseVoltageStepwise(Drivetrain dt, double voltStep, double stepwiseVoltage, String filename, String dir) {
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
        suppliedVoltage = stepwiseVoltage;
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        if (dt.getEncRate(Drivetrain.Side.LEFT) >= 0.75 * dt.getMaxSpeed() || dt.getEncRate(Drivetrain.Side.RIGHT) >= 0.75 * dt.getMaxSpeed()) {
            return true;
        } else {
            return false;
        }
    }
}