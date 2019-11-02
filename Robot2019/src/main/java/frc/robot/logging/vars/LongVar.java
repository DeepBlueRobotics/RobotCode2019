package frc.robot.logging.vars;

public class LongVar extends Var<Long> {

    public LongVar(String id, Long data) {
        super(id, data, Type.LONG);
    }

    public static LongVar create(String id, long data) {
        return new LongVar(id, data);
    }

}