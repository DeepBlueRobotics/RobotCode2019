package frc.robot.commands;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.Drivetrain;

/* Command which supplies the motors with a desired voltage that increases linearly.
*/
public class DrivetrainCharacterization extends Command {
    public enum Mode {
        LINEAR, STEP;
    }

    private Mode mode;
    private Drivetrain dt;
    private FileWriter fw;
    private String filename;
    public double suppliedVoltage, voltStep, stepwiseVoltage, voltageRuntime, maxVoltage;

    public DrivetrainCharacterization(Mode mode, Drivetrain dt, double voltStep, double stepwiseVoltage, String filename) {
        requires(dt);
        this.mode = mode;
        this.dt = dt;
        this.voltStep = voltStep;
        this.stepwiseVoltage = stepwiseVoltage;
        this.filename = filename;
        suppliedVoltage = 0.0;
        voltageRuntime = 0.0;
        maxVoltage = 12.0;
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        File f = new File(filename);
        try {
            f.createNewFile();
            fw = new FileWriter(f);
            fw.write("Timestamp (s),");
            fw.write("Voltage (V),");
            fw.write("LeftMotorVelocity (inches / s),");
            fw.write("RightMotorVelocity (inches / s)\r\n");
        } catch (IOException e) {
            System.out.println("Error caught creating FileWriter object: " + e);
        }

        dt.setVoltageCompensation(9.0);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        double throttle = suppliedVoltage / maxVoltage;
        dt.drive(throttle, throttle);
        writeMeasuredVelocity(fw);

        if (mode == Mode.LINEAR) {
            if (suppliedVoltage + voltStep <= maxVoltage) {
                suppliedVoltage += voltStep;
            }
        } else {
            suppliedVoltage = voltStep;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        if (mode == Mode.LINEAR) {
            if (suppliedVoltage == maxVoltage) {
                return true;
            } else {
                return false;
            }
        } else {
            if (dt.getEncRate(Drivetrain.Side.LEFT) >= 0.75 * dt.getMaxSpeed() || dt.getEncRate(Drivetrain.Side.RIGHT) >= 0.75 * dt.getMaxSpeed()) {
                return true;
            } else {
                return false;
            }
        }
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        try {
            fw.close();
        } catch (IOException e) {
            System.out.println("Cannot close FileWriter");
        }

        dt.disableVoltageCompensation();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }

    public void writeMeasuredVelocity(FileWriter fw) {
        double leftMotorVelocity, rightMotorVelocity;
        StringBuilder sb = new StringBuilder();
    
        leftMotorVelocity = dt.getEncRate(Drivetrain.Side.LEFT);
        rightMotorVelocity = dt.getEncRate(Drivetrain.Side.RIGHT);
    
        voltageRuntime += 0.02; // IncreaseVoltage occurs every 1/50 of a second
        sb.append(String.valueOf(voltageRuntime) + ",");
        sb.append(String.valueOf(suppliedVoltage) + ",");
        sb.append(String.valueOf(leftMotorVelocity) + ",");
        sb.append(String.valueOf(rightMotorVelocity) + "\r\n");
    
        try {
          fw.write(sb.toString());
        } catch (IOException e) {
          System.out.println("FileWriter object cannot write StringBuilder object: " + e);
        }
    }
}