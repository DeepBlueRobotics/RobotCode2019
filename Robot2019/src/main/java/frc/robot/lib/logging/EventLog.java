package frc.robot.lib.logging;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.logging.ErrorManager;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Handles event logging code
 */
final class EventLog {

    private static Logger logger;
    private static Handler handler;
    private static ErrorManager errorManager;

    static {
        logger = Logger.getLogger("RobotLogger");
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.INFO);
        errorManager = new ErrorManager() {
            @Override
            public synchronized void error(String msg, Exception ex, int code) {
                EventLog.error(msg, ex, code);
            }
        };
    }

    /**
     * Initializes the event logging code or returns if it has already been initialized
     */
    static void init() {
        if(!GlobalLogInfo.isInit()) {
            return;
        }
        try {
            handler = new FileHandler(GlobalLogInfo.getEventFile().getAbsolutePath(), false);
            handler.setErrorManager(errorManager);
            handler.setFormatter(new LogFormatter());
            logger.addHandler(handler);
        } catch(IOException e) {
            LogUtils.handleLoggingError(true, "setting up event log handler", e);
        }
    }

    /**
     * Sets the lowest {@link Level} of message to log
     * @param level The {@link Level} to set as the minumum
     */
    static void setLoggingLevel(Level level) {
        logger.setLevel(level);
    }

    /**
     * Logs a message to the log file
     * @param message The message to log
     * @param level The level at which to log the message
     * @throws IllegalStateException If the event logging code is not yet initialized
     */
    static void log(String message, Level level) throws IllegalStateException {
        LogUtils.checkInit();
        TimeLog.startEventLogCycle();
        logger.log(level, message);
        TimeLog.endEventLogCycle();
    }

    /**
     * Logs an {@link Throwable} to the log file
     * @param message The message to log with the {@link Throwable}. This will be replaced with {@link Throwable#getMessage()} if <code>null</code> or <code>""</code>
     * @param error The {@link Throwable} to log
     * @throws IllegalStateException If the event logging code is not yet initialized
     */
    static void logException(String message, Throwable error) throws IllegalStateException {
        LogUtils.checkInit();
        message = message == null || message.isEmpty() ? error.getMessage() : message;
        TimeLog.startEventLogCycle();
        logger.log(Level.SEVERE, message, error);
        TimeLog.endEventLogCycle();
    }

    private static synchronized void error(String msg, Exception ex, int code) {
        String task;
        switch(code) {
            case ErrorManager.OPEN_FAILURE:
                task = "while opening an output stream";
                break;
            case ErrorManager.CLOSE_FAILURE:
                task = "while closing an output stream";
                break;
            case ErrorManager.FLUSH_FAILURE:
                task = "while flushing an output stream";
                break;
            case ErrorManager.FORMAT_FAILURE:
                task = "while formatting a message";
                break;
            case ErrorManager.GENERIC_FAILURE:
            default:
                task = "of type GENERIC_FAILURE";
                break;
        }
        String message = task + (msg != null ? " with message " + msg : "");
        LogUtils.handleLoggingError(true, message, ex);
    }

    private EventLog() {}

    private static final class LogFormatter extends SimpleFormatter {

        private final String format = "%1$tH:%1$tM:%1$tS.%1$tL %4$s: %5$s%6$s%n";

        @Override
        public String format(LogRecord record) {
            ZonedDateTime zdt = ZonedDateTime.ofInstant(
                    record.getInstant(), ZoneId.systemDefault());
            String source;
            if (record.getSourceClassName() != null) {
                source = record.getSourceClassName();
                if (record.getSourceMethodName() != null) {
                   source += " " + record.getSourceMethodName();
                }
            } else {
                source = record.getLoggerName();
            }
            String message = formatMessage(record);
            String throwable = "";
            if (record.getThrown() != null) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                pw.println();
                record.getThrown().printStackTrace(pw);
                pw.close();
                throwable = sw.toString();
            }
            return String.format(format,
                                 zdt,
                                 source,
                                 record.getLoggerName(),
                                 record.getLevel().getLocalizedName(),
                                 message,
                                 throwable);
        }

    }

}