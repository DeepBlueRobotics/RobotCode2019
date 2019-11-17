package frc.robot.logging;

public enum Mode {

    DISABLED('D'), AUTONOMOUS('A'), TELEOP('T'), TEST('E');

    public final char id;

    private Mode(char id) {
        this.id = id;
    }
    
    public static Mode parse(char id) throws IllegalArgumentException {
        for(Mode mode: values()) {
            if(mode.id == id) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Invalid Id");
    }

    @Override
    public String toString() {
        return name().charAt(0) + name().toLowerCase().substring(1);
    }

}