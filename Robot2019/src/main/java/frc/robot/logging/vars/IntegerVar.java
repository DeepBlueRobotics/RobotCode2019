package frc.robot.logging.vars;

public class IntegerVar extends Var<Integer> {

    public IntegerVar(String id, Integer data) {
        super(id, data, Type.INTEGER);
    }

    public static IntegerVar create(String id, int data) {
        return new IntegerVar(id, data);
    }

}