package frc.robot.logging.vars;

/**
 * A class representing a variable.
 * @param <T> The data type of the variable
 */
public class Var<T> {

    /**
     * A unique identifier for the {@link Var}.
     */
    public final String id;
    /**
     * The data contained in the {@link Var}.
     */
    public final T data;
    /**
     * The {@link Type} of the {@link Var}.
     */
    public final Type type;

    /**
     * Initializes a new {@link Var}.
     * @param id The {@link #id} of the {@link Var}
     * @param data The {@link #data} to store in the {@link Var}
     * @param type The {@link Type} of the {@link Var}
     * @throws IllegalArgumentException If the {@link #data} is not valid for the provided {@link Type}
     */
    Var(String id, T data, Type type) throws IllegalArgumentException {
        if(!type.type.isAssignableFrom(data.getClass())) {
            throw new IllegalArgumentException("Invalid Type");
        }
        this.id = id;
        this.type = type;
        this.data = data;
    }

    /**
     * A convenience method for {@link BooleanVar#create(String, boolean)}.
     * @param id The {@link #id} of the {@link BooleanVar}
     * @param data The {@link #data} to store in the {@link BooleanVar}
     * @return A new {@link BooleanVar} with the specified data
     */
    public static BooleanVar create(String id, boolean data) {
        return BooleanVar.create(id, data);
    }

    /**
     * A convenience method for {@link IntegerVar#create(String, int)}.
     * @param id The {@link #id} of the {@link IntegerVar}
     * @param data The {@link #data} to store in the {@link IntegerVar}
     * @return A new {@link IntegerVar} with the specified data
     */
    public static IntegerVar create(String id, int data) {
        return IntegerVar.create(id, data);
    }

    /**
     * A convenience method for {@link DoubleVar#create(String, double)}.
     * @param id The {@link #id} of the {@link DoubleVar}
     * @param data The {@link #data} to store in the {@link DoubleVar}
     * @return A new {@link DoubleVar} with the specified data
     */
    public static DoubleVar create(String id, double data) {
        return DoubleVar.create(id, data);
    }

    /**
     * An enum representing the different types of {@link Var} are supported by the logging API.
     */
    public enum Type {
        /**
         * A boolean variable.
         */
        BOOLEAN("boo", Boolean.class),
        /**
         * An integer variable.
         */
        INTEGER("int", Integer.class),
        /**
         * A double variable.
         */
        DOUBLE("dec", Double.class);
        
        /**
         * A unique tag for each {@link Type}.
         */
        public final String tag;
        /**
         * The class representing this variable type.
         */
        public final Class<?> type;

        private Type(String tag, Class<?> type) {
            this.tag = tag;
            this.type = type;
        }

    }

}