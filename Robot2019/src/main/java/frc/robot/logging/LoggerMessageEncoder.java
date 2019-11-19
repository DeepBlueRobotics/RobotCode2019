package frc.robot.logging;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.Collection;

import static frc.robot.logging.LoggerInterface.closeStream;
import frc.robot.logging.vars.Var;

/**
 * Encodes messages to streams provided by the {@link LoggerInterface} into a format compatable with an interpriter
 */
public class LoggerMessageEncoder {
    
    /**
     * Whether multiple threads should be used when writing data to the log file.
     */
    public static final boolean allowMultiThreading;
    /**
     * The escape {@link String} for a '%'.
     */
    public static final String perR = "%0";
    /**
     * The {@link String} denoting the start of a message.
     */
    public static final String mStr = "%1";
    /**
     * The {@link String} denoting the end of a message.
     */
    public static final String mEnd = "%2";
    /**
     * The {@link String} denoting the seperation of parts of a message.
     */
    public static final String pSep = "%3";
    /**
     * TThe {@link String} denoting a variable list message.
     */
    public static final String vMrk = "%4";
    /**
     * The {@link String} denoting a variable name mapping message.
     */
    public static final String mMrk = "%5";
    /**
     * The {@link String} denoting a timestamp.
     */
    public static final String tMrk = "%6";
    /**
     * The {@link String} denoting an error.
     */
    public static final String eMrk = "%7";
    /**
     * The {@link String} denoting a mode change message.
     */
    public static final String sMrk = "%8";

    static {
        allowMultiThreading = false;
    }

    /**
     * Logs an error message to the log file.
     * @param message The error message to log
     * @see #logErrorMessage(String, boolean)
     */
    public static void logErrorMessage(String message) {
        logErrorMessage(message, true);
    }

    /**
     * Logs an error message to the log file.
     * @param message The error message to log
     * @param includeTime Whether a timestamp should be included in the log entry
     * @see #logErrorMessage(String)
     */
    public static void logErrorMessage(String message, boolean includeTime) {
        logMessage(message, includeTime, true);
    }

    /**
     * Logs a message to the log file.
     * @param message The message to log
     * @see #logMessage(String)
     */
    public static void logMessage(String message) {
        logMessage(message, true);
    }

    /**
     * Logs a message to the log file.
     * @param message The message to log
     * @param includeTime Whether a timestamp should be included in the log entry
     * @see #logMessage(String)
     */
    public static void logMessage(String message, boolean includeTime) {
        logMessage(message, includeTime, false);
    }

    /**
     * Logs a message to to the log file
     * @param message The message to log
     * @param includeTime Whether a timestamp should be included in the log entry
     * @param isError Whether the message is an error
     * @see #logMessage(String)
     * @see #logMessage(String, boolean)
     * @see #logErrorMessage(String)
     * @see #logErrorMessage(String, boolean)
     */
    public static void logMessage(String message, boolean includeTime, boolean isError) {
        thread(() -> {
            PrintStream ps = LoggerInterface.getStream();
            ps.print(mStr);
            if(includeTime) {
                String time = getTimestamp();
                ps.print(tMrk + time + pSep);
            }
            if(isError) {
                ps.print(eMrk);
            }
            ps.print(format(message));
            ps.print(mEnd);
            ps.flush();
            closeStream(ps);
        });
    }

    /**
     * Logs an array of vars to the log file. This is equivalent to the call <code>logVars(Arrays.asList(vars))</code>.
     * @param vars The vars to log
     * @see #logVars(Collection)
     */
    public static void logVars(Var<?>... vars) {
        logVars(Arrays.asList(vars));
    }

    /**
     * Logs a {@link Collection} of vars to the log file.
     * @param vars The vars to log
     */
    public static void logVars(Collection<Var<?>> vars) {
        thread(() -> {
            PrintStream ps = LoggerInterface.getStream();
            ps.print(mStr);
            ps.print(tMrk);
            ps.print(getTimestamp());
            ps.print(pSep);
            ps.print(vMrk);
            for(Var<?> var: vars) {
                ps.print(pSep);
                ps.print(format(var.type.tag));
                ps.print(pSep);
                ps.print(format(var.id));
                ps.print(pSep);
                ps.print(var.data.toString());
                ps.print(pSep);
            }
            ps.print(mEnd);
            ps.flush();
            closeStream(ps);
        });
    }

    /**
     * Logs a variable name mapping to the log file
     * @param id The id of the variable to map
     * @param name The display name of the variable
     */
    public static void mapId(String id, String name) {
        thread(() -> {
            PrintStream ps = LoggerInterface.getStream();
            ps.print(mStr);
            ps.print(tMrk);
            ps.print(getTimestamp());
            ps.print(pSep);
            ps.print(mMrk);
            ps.print(format(id));
            ps.print(pSep);
            ps.print(format(name));
            ps.print(mEnd);
            ps.flush();
            closeStream(ps);
        });
    }

    /**
     * Logs a change in the mode of the robot to the log file
     * @param mode The {@link Mode} to log
     */
	public static void logMode(Mode mode) {
        thread(() -> {
            PrintStream ps = LoggerInterface.getStream();
            ps.print(mStr);
            ps.print(tMrk);
            ps.print(getTimestamp());
            ps.print(pSep);
            ps.print(sMrk);
            ps.print(mode.id);
            ps.print(mEnd);
            ps.flush();
            closeStream(ps);
        });
	}

    /**
     * Replaces all '%'s in a message with <code>{@link #perR}</code>.
     * @param message The message to filter
     * @return The filtered message
     */
    public static String format(String message) {
        message = message.replaceAll("%", "%0");
        return message;
    }

    /**
     * Gets the time and returns it as a commonly formatted timestamp
     * @return The formatted time
     */
    public static String getTimestamp() {
        LocalDateTime ldt = LocalDateTime.now();
        int hour = ldt.get(ChronoField.HOUR_OF_DAY);
        int minute = ldt.get(ChronoField.MINUTE_OF_HOUR);
        int second = ldt.get(ChronoField.SECOND_OF_MINUTE);
        int milisecond = ldt.get(ChronoField.MILLI_OF_SECOND);
        String c = ":";
        return hour + c + minute + c + second + "." + milisecond;
    }
    
    private static void thread(Runnable newThread) {
        if(allowMultiThreading) {
            new Thread(newThread).start();
        } else {
            newThread.run();
        }
    }

    private LoggerMessageEncoder() {}

}