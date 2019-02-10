package frc.robot.commands;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.Drivetrain;

public class IncreaseVoltageStepwise extends Command {
    private Drivetrain dt;
    private FileWriter fw;
    private String filename;
    private double volt_step;

    public IncreaseVoltageStepwise(Drivetrain dt, double volt_step, String filename) {
        requires(dt);
        this.dt = dt;
        this.filename = filename;
        this.volt_step = volt_step;
    }

    @Override
	protected void initialize() {
        File f = new File(filename);
        try {
            f.createNewFile();
            fw = new FileWriter(f);
            fw.write("Timestamp (s),");
            fw.write("Voltage (V),");
            fw.write("LeftMotorVelocity (inches / s),");
            fw.write("RightMotorVelocity (inches / s)\n");
        } catch (IOException e) {
            System.out.println("Error caught creating FileWriter object: " + e);
        }
    }

    @Override
	protected void execute() {
        double throttle = dt.suppliedVoltage / dt.maxVoltage;
        dt.drive(throttle, throttle);
        dt.writeMeasuredVelocity(fw);
        dt.suppliedVoltage = volt_step;
    }

    @Override
    // Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
        if (dt.getEncRate(dt.getSideValue("LEFT")) >= 0.75 * dt.getMaxSpeed() || dt.getEncRate(dt.getSideValue("RIGHT")) >= 0.75 * dt.getMaxSpeed()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
	protected void end() {
        try {
            fw.close();
        } catch (IOException e) {
            System.out.println("Cannot close FileWriter");
        }
    }

    @Override
	protected void interrupted() {
        end();
    }
}