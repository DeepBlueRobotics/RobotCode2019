package frc.robot.logging.vars;

public class DoubleVar extends Var<Double> {

    public DoubleVar(String id, Double data) {
        super(id, data, Type.DOUBLE);
    }

    public static DoubleVar create(String id, double data) {
        return new DoubleVar(id, data);
    }

}