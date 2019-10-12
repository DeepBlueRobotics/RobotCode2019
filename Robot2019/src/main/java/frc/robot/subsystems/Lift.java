package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.SoftLimitDirection;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.KeepLift;

public class Lift extends Subsystem {
    private CANSparkMax motor;
    private CANSparkMax motor2;
    private CANEncoder enc;
    private CANPIDController controller;
    private final double ARB_FF_UP = 0.12*12;
    private final double ARB_FF_DOWN = 0.12*12;
    private static final double BOTTOM_HEIGHT = 14; // TODO: set to correct value 
    private double currentGoal;
    private final double ERROR = 1; // TODO: set to correct value 
    
    public Lift(CANSparkMax motor, CANSparkMax motor2) {
        this.motor = motor;
        this.motor2 = motor2;
        motor.restoreFactoryDefaults();
        motor2.restoreFactoryDefaults();
        //motor.setInverted(true);
        motor2.follow(motor);
        //motor2.setInverted(true);
        enc = motor.getEncoder();
        enc.setPositionConversionFactor(0.4 * Math.PI); // inches 
        enc.setPosition(0);
        /*motor.enableSoftLimit(SoftLimitDirection.kForward, true);
        motor.setSoftLimit(SoftLimitDirection.kForward, 1);
        motor.enableSoftLimit(SoftLimitDirection.kReverse, true);
        motor.setSoftLimit(SoftLimitDirection.kReverse, -58);*/
        //motor.enableVoltageCompensation(12);
        controller = motor.getPIDController();
        prepareSmartDashboard();
    }

    public void setPIDF(double[] pidf) {
        motor.getPIDController().setP(pidf[0]);
        motor.getPIDController().setI(pidf[1]);
        motor.getPIDController().setD(pidf[2]);
        motor.getPIDController().setFF(pidf[3]);
    }

    public void setGoalPosition(double position) {
        if (position > getPosition()) {
            controller.setReference(position, ControlType.kPosition, 0, ARB_FF_UP);
        } else {
            controller.setReference(position, ControlType.kPosition, 0, ARB_FF_DOWN);
        }
        currentGoal = position;
    }

    public void setSpeed(double speed) {
        motor.set(speed);
        SmartDashboard.putNumber("Lift Voltage", motor.getBusVoltage());
    }

    public double getPosition() {
        return enc.getPosition();
    }

    public double getCurrentGoal() {
        return currentGoal;
    }

    public boolean liftInUse() {
        return Math.abs(getPosition() - currentGoal) > ERROR;
    }

    public void prepareSmartDashboard() {
        if (!SmartDashboard.containsKey("Lift Up PIDF")) {
            SmartDashboard.putNumberArray("Lift Up PIDF", PIDF.UP);
        }
        if (!SmartDashboard.containsKey("Lift Down PIDF")) {
            SmartDashboard.putNumberArray("Lift Down PIDF", PIDF.DOWN);
        }
        if (!SmartDashboard.containsKey("Lift Keep PIDF")) {
            SmartDashboard.putNumberArray("Lift Keep PIDF", PIDF.KEEP);
        }
    }

    @Override
    public void initDefaultCommand() {
    } // default command set to KeepLift in Robot.java 

    public class Position {
        public static final double HATCH_1 = 19, HATCH_2 = 47, HATCH_3 = 73.25, CARGO_GROUND = BOTTOM_HEIGHT, 
                CARGO_1 = 27.5, CARGO_2 = 55.5, CARGO_3 = 73.25, CARGO_SHIP = 39;
        // hatch 3 is actually 75; cargo 3 is actually 83.5 
    }

    public static class PIDF {
        public static final double[] UP = {0, 0, 0, 0};
        public static final double[] DOWN = {0, 0, 0, 0};
        public static final double[] KEEP = {0, 0, 0, 0};
        // TODO: Set all to reasonable/correct numbers
    }
}