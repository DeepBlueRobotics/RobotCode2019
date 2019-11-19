package frc.robot.logging.vars;

/**
 * A class representing a double {@link Var}.
 */
public class DoubleVar extends Var<Double> {

    /**
     * Initializes a new {@link DoubleVar}
     * @param id The {@link Var#id} of the {@link DoubleVar}
     * @param data The {@link Var#data} to store in the {@link DoubleVar}
     */
    public DoubleVar(String id, Double data) {
        super(id, data, Type.DOUBLE);
    }

    /**
     * A convenience method for {@link #DoubleVar(String, Double)}.
     * @param id The {@link Var#id} of the {@link DoubleVar}
     * @param data The {@link Var#data} to store in the {@link DoubleVar}
     * @return A new {@link DoubleVar} with the specified data}
     */
    public static DoubleVar create(String id, double data) {
        return new DoubleVar(id, data);
    }

}