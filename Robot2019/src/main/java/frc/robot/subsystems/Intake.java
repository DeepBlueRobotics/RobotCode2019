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
    private final double HATCH_IN_SPEED = -0.5;
    private final double HATCH_OUT_SPEED = 1;
    private final double CARGO_CURRENT_THRESHOLD = 15.0;
    private final double HATCH_CURRENT_THRESHOLD = 15.0;
    private double wristArbFF;
    private State state;
    private double wristGoal;

    public Intake(CANSparkMax wrist, CANSparkMax topRoller, CANSparkMax sideRollers, DoubleSolenoid piston) {
        this.wrist = wrist;
        this.topRoller = topRoller;
        this.sideRollers = sideRollers;
        this.piston = piston;

        sideRollers.setInverted(true);
        //sideRollers.setSmartCurrentLimit(20);
        wrist.enableSoftLimit(SoftLimitDirection.kForward, false);
        wrist.enableSoftLimit(SoftLimitDirection.kReverse, false);
        //wrist.enableSoftLimit(SoftLimitDirection.kForward, true);
        //wrist.setSoftLimit(SoftLimitDirection.kForward, (float) 0.165); // not necessary at competition, only for testing
        wrist.getEncoder().setPositionConversionFactor(36.0/605.0);
        setWristPIDF(PIDF.WRIST);
        double avg = (2.25-0.5)/24;
        double diff = (2.25+0.5)/24;
        boolean testing = false;
        if(testing) {
            wrist.getPIDController().setOutputRange(-0.5/12, 2.25/12);
        } else {
            wrist.getPIDController().setOutputRange(avg-2*diff, avg+2*diff);
        }
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
        SmartDashboard.putNumber("Intake Current", sideRollers.getOutputCurrent());
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
        SmartDashboard.putNumber("Intake Current", sideRollers.getOutputCurrent());
    }

    public boolean hasHatch() {
        return sideRollers.getOutputCurrent() > HATCH_CURRENT_THRESHOLD;
    }

    public void keepHatch() {
        sideRollers.getPIDController().setReference(sideRollers.getEncoder().getPosition(), ControlType.kPosition);
    }

    public void ejectHatch() {
        sideRollers.set(HATCH_OUT_SPEED);
    }

    public void stopRollers() {
        topRoller.set(0);
        sideRollers.set(0);
    }

    public void setWristGoal(double pos) {
        setWristArbFF();
        wrist.getPIDController().setReference(pos, ControlType.kPosition, 0, wristArbFF); // TODO: Set pidSlot to correct value 
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
        setWristGoal(WristPosition.GROUND);
        state = State.CARGO;
    }

    public void prepareHatch() {
        piston.set(DoubleSolenoid.Value.kReverse);
        setWristGoal(WristPosition.DEFAULT);
        state = State.HATCH;
    }

    public void setTopSpeed(double speed) {
        topRoller.set(speed);
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
        if (!SmartDashboard.containsKey("Wrist PIDF")) {
            SmartDashboard.putNumberArray("Wrist PIDF", PIDF.WRIST);
        }
    }

    @Override
    public void initDefaultCommand() {
    }

    public class WristPosition {
        public static final double START = /*-0.11*/0.23, GROUND = -0.11+25.0/360.0 /*25.0*/, DEFAULT = 0, TOP = 0.17; // TODO: set to correct values (rotations)
    }

    public static class PIDF {
        public static final double[] HATCH_SIDE = {0, 0, 0, 0};
        public static final double[] HATCH_TOP = {0, 0, 0, 0};
        public static final double[] CARGO_SIDE = {0, 0, 0, 0};
        public static final double[] CARGO_TOP = {0, 0, 0, 0};
        public static final double[] WRIST = {72, 0, 0, 0};
        public static final double WRIST_FF = 1.0; // volts
        // TODO: Set all to reasonable/correct numbers
    }

    public enum State {
        CARGO, 
        HATCH, 
        NONE
    }
}