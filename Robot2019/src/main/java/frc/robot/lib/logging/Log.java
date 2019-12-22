package frc.robot.lib.logging;

import java.util.logging.Level;

public final class Log {

    public static void logErrorMessage(String message) throws IllegalStateException {
        if(GlobalLogInfo.areEventsDisabled()) {
            return;
        }
        EventLog.log(message, Level.SEVERE);
    }

    public static void logWarningMessage(String message) throws IllegalStateException {
        if(GlobalLogInfo.areEventsDisabled()) {
            return;
        }
        EventLog.log(message, Level.WARNING);
    }

    public static void logInfoMessage(String message) throws IllegalStateException {
        if(GlobalLogInfo.areEventsDisabled()) {
            return;
        }
        EventLog.log(message, Level.INFO);
    }

    public static void logConfigMessage(String message) throws IllegalStateException {
        if(GlobalLogInfo.areEventsDisabled()) {
            return;
        }
        EventLog.log(message, Level.CONFIG);
    }

    public static void setLoggingLevel(Level level) {
        EventLog.setLoggingLevel(level);
    }

    public static void disableEventLogging() {
        setLoggingLevel(Level.OFF);
    }

    private Log() {}

}
