package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANEncoder;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Lift extends Subsystem {
    private CANSparkMax motor;
    private CANEncoder enc;
    
    public Lift(CANSparkMax motor) {
        this.motor = motor;
        enc = motor.getEncoder();
        enc.setPositionConversionFactor(1); // TODO: set to correct value 
        enc.setPosition(0);
    }

    public void setSpeed(double speed) {
        motor.set(speed);
    }

    public double getHeight() {
        return enc.getPosition();
    }

    @Override
    public void initDefaultCommand() {
    }
}