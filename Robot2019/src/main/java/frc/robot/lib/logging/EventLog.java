package frc.robot.lib.logging;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

final class EventLog {

    private static Logger logger;
    private static Handler handler;

    static {
        logger = Logger.getLogger("RobotLogger");
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);
    }

    void init() {
        if(!GlobalLogInfo.isInit()) {
            return;
        }
        try {
            handler = new FileHandler(GlobalLogInfo.getEventFile().getAbsolutePath(), false);
            System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tc \n\t %4$: %5$");
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
        } catch(IOException e) {
            GlobalLogInfo.disableEvents();
            System.err.println("Event logging setup failed robot will continue to run with event logging disabled");
        }
    }

    void setLoggingLevel(Level level) {
        logger.setLevel(level);
    }

    void log(String message, Level level) {
        logger.log(level, message);
    }

}