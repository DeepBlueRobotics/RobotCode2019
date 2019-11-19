package frc.robot.logging.vars;

/**
 * A class representing an integer {@link Var}.
 */
public class IntegerVar extends Var<Integer> {

    /**
     * Initializes a new {@link IntegerVar}.
     * @param id The {@link Var#id} of the {@link IntegerVar}
     * @param data The {@link Var#data} to store in the {@link IntegerVar}
     */
    public IntegerVar(String id, Integer data) {
        super(id, data, Type.INTEGER);
    }

    /**
     * A convenience method for {@link #IntegerVar(String, Integer)}.
     * @param id The {@link Var#id} of the {@link IntegerVar}
     * @param data The {@link Var#data} to store in the {@link IntegerVar}
     * @return A new {@link IntegerVar} with the specified data}
     */
    public static IntegerVar create(String id, int data) {
        return new IntegerVar(id, data);
    }

}