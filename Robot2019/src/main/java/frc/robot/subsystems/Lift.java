package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.ControlType;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.commands.KeepLift;

public class Lift extends Subsystem {
    private CANSparkMax motor;
    private CANEncoder enc;
    private CANPIDController controller;
    private final double ARB_FF = 0; // TODO: set to correct value 
    private static final double BOTTOM_HEIGHT = 0; // TODO: set to correct value 
    private double currentGoal;
    
    public Lift(CANSparkMax motor) {
        this.motor = motor;
        enc = motor.getEncoder();
        enc.setPositionConversionFactor(0.4 * Math.PI); // inches 
        enc.setPosition(BOTTOM_HEIGHT);
        controller = new CANPIDController(motor);
    }

    public void setGoalPosition(double position) {
        controller.setReference(position, ControlType.kPosition, 0, ARB_FF);
        currentGoal = position;
    }

    public void setSpeed(double speed) {
        motor.set(speed);
    }

    public double getPosition() {
        return enc.getPosition();
    }

    public double getCurrentGoal() {
        return currentGoal;
    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new KeepLift(this));
    }

    public class Position {
        static final double HATCH_1 = 19, HATCH_2 = 47, HATCH_3 = 75, CARGO_GROUND = BOTTOM_HEIGHT, 
                CARGO_1 = 27.5, CARGO_2 = 55.5, CARGO_3 = 83.5, CARGO_SHIP = 39;
    }
}