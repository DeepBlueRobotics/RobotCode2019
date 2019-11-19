package frc.robot.logging.vars;

/**
 * A class representing a boolean {@link Var}.
 */
public class BooleanVar extends Var<Boolean> {

    /**
     * Initializes a new {@link BooleanVar}.
     * @param id The {@link Var#id} of the {@link BooleanVar}
     * @param data The {@link Var#data} to store in the {@link BooleanVar}
     */
    public BooleanVar(String id, Boolean data) {
        super(id, data, Type.BOOLEAN);
    }

    /**
     * A convenience method for {@link #BooleanVar(String, Boolean)}.
     * @param id The {@link Var#id} of the {@link BooleanVar}
     * @param data The {@link Var#data} to store in the {@link BooleanVar}
     * @return A new {@link BooleanVar} with the specified data
     */
    public static BooleanVar create(String id, boolean data) {
        return new BooleanVar(id, data);
    }

}