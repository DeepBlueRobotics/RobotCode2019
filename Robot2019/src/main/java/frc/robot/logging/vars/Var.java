package frc.robot.logging.vars;

public class Var<T> {

    public final String id;
    public final T data;
    public final Type type;

    Var(String id, T data, Type type) throws IllegalArgumentException {
        if(!type.type.isAssignableFrom(data.getClass())) {
            throw new IllegalArgumentException("Invalid Type");
        }
        this.id = id;
        this.type = type;
        this.data = data;
    }

    public static BooleanVar create(String id, boolean data) {
        return BooleanVar.create(id, data);
    }

    public static IntegerVar create(String id, int data) {
        return IntegerVar.create(id, data);
    }

    public static DoubleVar create(String id, double data) {
        return DoubleVar.create(id, data);
    }

    public enum Type {
        BOOLEAN("boo", Boolean.class), INTEGER("int", Integer.class), DOUBLE("dec", Double.class);
        
        public final String tag;
        public final Class<?> type;

        private Type(String tag, Class<?> type) {
            this.tag = tag;
            this.type = type;
        }

    }

}