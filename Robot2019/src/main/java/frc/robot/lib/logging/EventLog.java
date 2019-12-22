package frc.robot.lib.logging;

import java.io.IOException;
import java.util.logging.ErrorManager;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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

    static void init() {
        if(!GlobalLogInfo.isInit()) {
            return;
        }
        try {
            handler = new FileHandler(GlobalLogInfo.getEventFile().getAbsolutePath(), false);
            handler.setErrorManager(errorManager);
            System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tc \n\t %4$: %5$");
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
        } catch(IOException e) {
            LogUtils.handleLoggingError(true, "setting up event log handler", e);
        }
    }

    static void setLoggingLevel(Level level) {
        logger.setLevel(level);
    }

    static void log(String message, Level level) throws IllegalStateException {
        LogUtils.checkInit();
        logger.log(level, message);
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

}