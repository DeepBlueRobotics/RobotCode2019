package frc.robot.logging.vars;

public class BooleanVar extends Var<Boolean> {

    public BooleanVar(String id, Boolean data) {
        super(id, data, Type.BOOLEAN);
    }

    public static BooleanVar create(String id, boolean data) {
        return new BooleanVar(id, data);
    }

}