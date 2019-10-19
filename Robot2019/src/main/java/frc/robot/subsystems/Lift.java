package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.KeepLift;

public class Lift extends Subsystem {
    private CANSparkMax motor;
    private CANSparkMax motor2;
    private CANEncoder enc;
    private CANPIDController controller;
    private final double ARB_FF_UP = 0.0305*12;
    private final double ARB_FF_DOWN = 0.0305*12;
    public static final double BOTTOM_HEIGHT = 14; // TODO: set to correct value 
    private double currentGoal;
    private final double ERROR = 1; // TODO: set to correct value

    public Lift(CANSparkMax motor, CANSparkMax motor2) {
        this.motor = motor;
        this.motor2 = motor2;
        motor.setInverted(true);
        motor2.follow(motor);
        enc = motor.getEncoder();
        enc.setPositionConversionFactor(0.4 * Math.PI); // inches 
        resetPosition();
        motor.enableSoftLimit(SoftLimitDirection.kForward, true);
        motor.setSoftLimit(SoftLimitDirection.kForward, (float) 72.5);
        motor.enableSoftLimit(SoftLimitDirection.kReverse, true);
        motor.setSoftLimit(SoftLimitDirection.kReverse, (float) 13);
        controller = motor.getPIDController();
        double avg = (0.084-0.03)/2;
        double hlfdiff = (0.084+0.03)/2;
        boolean isTesting = false;
        if(isTesting) {
            controller.setOutputRange(-0.03, 0.084);
        } else {
            controller.setOutputRange(avg-(8+16.0/3.0)*hlfdiff, avg+(8+16.0/3.0)*hlfdiff); //-0.03, 0.084
        }
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
        SmartDashboard.putNumber("Lift Applied Output", motor.getAppliedOutput());
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
        SmartDashboard.putNumberArray("Lift Up PIDF", PIDF.UP);
        SmartDashboard.putNumberArray("Lift Down PIDF", PIDF.DOWN);
        SmartDashboard.putNumberArray("Lift Keep PIDF", PIDF.KEEP);
        // only called when robot code starts up
    }
    public void resetPosition(){
        enc.setPosition(33.5);
    }

    @Override
    public void initDefaultCommand() {
    } // default command set to KeepLift in Robot.java

    public class Position {
        public static final double HATCH_1 = 19, HATCH_2 = 47, HATCH_3 = 72.25, CARGO_GROUND = BOTTOM_HEIGHT, 
                CARGO_1 = 27.5, CARGO_2 = 55.5, CARGO_3 = 72.25, CARGO_SHIP = 39;
        // hatch 3 is actually 75; cargo 3 is actually 83.5 
    }

    public static class PIDF {
        public static final double[] KEEP = {0.1, 0, 0, 0};
        public static final double[] UP = KEEP;
        public static final double[] DOWN = KEEP;
        // TODO: Set all to reasonable/correct numbers
    }
}