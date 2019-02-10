package frc.robot.commands;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.Drivetrain;

/* Command which supplies the motors with a desired voltage that increases linearly.
*/
public class IncreaseVoltageLinear extends Command {
    private Drivetrain dt;
    private FileWriter fw;
    private String filename;

    public double suppliedVoltage;
    public double volt_step;

    public IncreaseVoltageLinear(Drivetrain dt, double volt_step, String filename) {
        requires(dt);
        this.dt = dt;
        this.volt_step = volt_step;
        this.filename = filename;
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
        if (dt.suppliedVoltage + volt_step <= dt.maxVoltage) {
            dt.suppliedVoltage += volt_step;
            double throttle = dt.suppliedVoltage / dt.maxVoltage;
            dt.drive(throttle, throttle);
            dt.writeMeasuredVelocity(fw);
            dt.suppliedVoltage += volt_step;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        if (dt.suppliedVoltage >= dt.maxVoltage) {
            return true;
        } else {
            return false;
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
}