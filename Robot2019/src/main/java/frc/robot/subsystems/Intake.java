package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

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
    private State state;

    public Intake(CANSparkMax wrist, CANSparkMax topRoller, CANSparkMax sideRollers, DoubleSolenoid piston) {
        this.wrist = wrist;
        this.topRoller = topRoller;
        this.sideRollers = sideRollers;
        this.piston = piston;
        state = State.NONE;
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
        wrist.getPIDController().setReference(pos, ControlType.kPosition);
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

    @Override
    public void initDefaultCommand() {
    }

    public class WristPosition {
        static final double START = 0.75, GROUND = -0.2, DEFAULT = 0, TOP = 0.2; // TODO: set to correct values (rotations)
    }

    public enum State {
        CARGO, 
        HATCH, 
        NONE
    }
}