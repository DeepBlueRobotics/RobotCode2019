package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake extends Subsystem {
    private CANSparkMax wrist;
    private CANSparkMax topRoller;
    private CANSparkMax sideRollers;
    private DoubleSolenoid piston;
    private final double TOP_IN_SPEED = 0.5; // TODO: set all to correct values 
    private final double SIDE_IN_SPEED = 0.5;
    private final double HATCH_IN_SPEED = 0.5;
    private final double CARGO_CURRENT_THRESHOLD = 6.0;
    private final double HATCH_CURRENT_THRESHOLD = 6.0;
    private double wristArbFF;
    private State state;
    private double wristGoal;

    public Intake(CANSparkMax wrist, CANSparkMax topRoller, CANSparkMax sideRollers, DoubleSolenoid piston) {
        this.wrist = wrist;
        this.topRoller = topRoller;
        this.sideRollers = sideRollers;
        this.piston = piston;

        wrist.enableSoftLimit(SoftLimitDirection.kForward, true);
        wrist.setSoftLimit(SoftLimitDirection.kForward, (float) 0.165); // not necessary at competition, only for testing
        wrist.getEncoder().setPositionConversionFactor(36.0/605.0);

        setWristArbFF();
        wrist.getEncoder().setPosition(WristPosition.START);
        state = State.NONE;
        prepareSmartDashboard();
    }

    public void setTopPIDF(double[] pidf) {
        topRoller.getPIDController().setP(pidf[0]);
        topRoller.getPIDController().setI(pidf[1]);
        topRoller.getPIDController().setD(pidf[2]);
        topRoller.getPIDController().setFF(pidf[3]);
    }

    public void setSidePIDF(double[] pidf) {
        sideRollers.getPIDController().setP(pidf[0]);
        sideRollers.getPIDController().setI(pidf[1]);
        sideRollers.getPIDController().setD(pidf[2]);
        sideRollers.getPIDController().setFF(pidf[3]);
    }

    public void setWristPIDF(double[] pidf) {
        wrist.getPIDController().setP(pidf[0]);
        wrist.getPIDController().setI(pidf[1]);
        wrist.getPIDController().setD(pidf[2]);
    }

    public void intakeCargo() {
        topRoller.set(TOP_IN_SPEED);
        sideRollers.set(SIDE_IN_SPEED);
    }

    public boolean hasCargo() {
        return sideRollers.getOutputCurrent() > CARGO_CURRENT_THRESHOLD;
    }

    public void keepCargo() {
        topRoller.getPIDController().setReference(topRoller.getEncoder().getPosition(), ControlType.kPosition);
        sideRollers.getPIDController().setReference(sideRollers.getEncoder().getPosition(), ControlType.kPosition);
    }

    public void ejectCargo() {
        topRoller.set(TOP_IN_SPEED * -1);
        sideRollers.set(SIDE_IN_SPEED * -1);
    }

    public void intakeHatch() {
        sideRollers.set(HATCH_IN_SPEED);
    }

    public boolean hasHatch() {
        return sideRollers.getOutputCurrent() > HATCH_CURRENT_THRESHOLD;
    }

    public void keepHatch() {
        sideRollers.getPIDController().setReference(sideRollers.getEncoder().getPosition(), ControlType.kPosition);
    }

    public void ejectHatch() {
        sideRollers.set(HATCH_IN_SPEED * -1);
    }

    public void stopRollers() {
        topRoller.set(0);
        sideRollers.set(0);
    }

    public void setWristPosition(double pos) {
        setWristArbFF();
        wrist.getPIDController().setReference(pos, ControlType.kPosition, 2, wristArbFF); // TODO: Set pidSlot to correct value 
        wristGoal = pos;
    }

    public double getWristGoal() {
        return wristGoal;
    }

    public void setWristArbFF() {
        double angle = getWristPosition() * 2 * Math.PI;
        wristArbFF = Math.cos(angle) * SmartDashboard.getNumber("Wrist Arbitrary FF", PIDF.WRIST_FF);
    }

    public double getWristPosition() {
        return wrist.getEncoder().getPosition();
    }

    public void prepareCargo() {
        piston.set(DoubleSolenoid.Value.kForward);
        setWristPosition(WristPosition.GROUND);
        state = State.CARGO;
    }

    public void prepareHatch() {
        piston.set(DoubleSolenoid.Value.kReverse);
        setWristPosition(WristPosition.DEFAULT);
        state = State.HATCH;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void prepareSmartDashboard() {
        if (!SmartDashboard.containsKey("Hatch Side Roller PIDF")) {
            SmartDashboard.putNumberArray("Hatch Side Roller PIDF", PIDF.HATCH_SIDE);
        }
        if (!SmartDashboard.containsKey("Hatch Top Roller PIDF")) {
            SmartDashboard.putNumberArray("Hatch Top Roller PIDF", PIDF.HATCH_TOP);
        }
        if (!SmartDashboard.containsKey("Cargo Side Roller PIDF")) {
            SmartDashboard.putNumberArray("Cargo Side Roller PIDF", PIDF.CARGO_SIDE);
        }
        if (!SmartDashboard.containsKey("Cargo Top Roller PIDF")) {
            SmartDashboard.putNumberArray("Cargo Top Roller PIDF", PIDF.CARGO_TOP);
        }
        if (!SmartDashboard.containsKey("Wrist Arbitrary FF")) {
            SmartDashboard.putNumber("Wrist Arbitrary FF", PIDF.WRIST_FF);
        }
    }

    @Override
    public void initDefaultCommand() {
    }

    public class WristPosition {
        public static final double START = /*0.25 (need to change to actual value)*/-0.09, GROUND = -0.09, DEFAULT = 0, TOP = 0.17; // TODO: set to correct values (rotations)
    }

    public static class PIDF {
        public static final double[] HATCH_SIDE = {0, 0, 0, 0};
        public static final double[] HATCH_TOP = {0, 0, 0, 0};
        public static final double[] CARGO_SIDE = {0, 0, 0, 0};
        public static final double[] CARGO_TOP = {0, 0, 0, 0};
        public static final double WRIST_FF = 1.8; // volts
        // TODO: Set all to reasonable/correct numbers
    }

    public enum State {
        CARGO, 
        HATCH, 
        NONE
    }
}