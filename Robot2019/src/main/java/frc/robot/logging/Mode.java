package frc.robot.logging;

/**
 * An enum containing the possable modes in which the robot can be.
 */
public enum Mode {

    /**
     * Disabled Mode
     */
    DISABLED('D'),
    /**
     * Autonomous Mode
     */
    AUTONOMOUS('A'),
    /**
     * Teleop Mode
     */
    TELEOP('T'),
    /**
     * Test Mode
     */
    TEST('E');

    /**
     * A unique id character for the {@link Mode}
     */
    public final char id;

    private Mode(char id) {
        this.id = id;
    }
    
    /**
     * Retrieves a {@link Mode} from its {@link #id}.
     * @param id The {@link #id} for which to check
     * @return The {@link Mode} with the corresponding {@link #id} if it can be found
     * @throws IllegalArgumentException If no {@link Mode} with a corresponding {@link #id} can be found
     */
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