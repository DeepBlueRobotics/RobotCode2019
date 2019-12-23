package frc.robot.lib.logging;

import static frc.robot.lib.logging.GlobalLogInfo.*;

final class LogUtils {

    static void checkInit() throws IllegalStateException {
        if(!isInit()) {
            throw new IllegalStateException("Logging code is not initialized");
        }
    }

    static void checkNotInit() throws IllegalStateException {
        if(GlobalLogInfo.isInit()) {
            throw new IllegalStateException("Logging code already initialized");
        }
    }

    static void handleLoggingError(boolean sector, String task, Exception error) {
        if(sector) {
            System.err.println("Error occured while " + task + ". Logging will continue to run with event logging disabled.");
            if(error != null) {
                error.printStackTrace(System.err);
            }
            disableEvents();
        } else {
            System.err.println("Error occured while " + task + " logging will continue to run with data logging disabled.");
            if(error != null) {
                error.printStackTrace(System.err);
            }
            error.printStackTrace(System.err);
            disableData();
        }
    }

    static void handleLoggingApiDisableError(String task, Exception error) {
        System.err.println("Error occured while " + task + ". Logging will be disabled.");
        if(error != null) {
            error.printStackTrace(System.err);
        }
        disableEvents();
        disableData();
    }

    private LogUtils() {};

}