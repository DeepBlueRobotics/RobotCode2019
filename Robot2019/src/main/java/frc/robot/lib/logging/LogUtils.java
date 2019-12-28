package frc.robot.lib.logging;

import static frc.robot.lib.logging.GlobalLogInfo.*;

/**
 * Various utility methods utilized by the logging code
 */
final class LogUtils {

    /**
     * Checks that the logging api is initialized
     * @throws IllegalStateException If the logging code is not initialized
     * @see #checkNotInit()
     */
    static void checkInit() throws IllegalStateException {
        if(!isInit()) {
            throw new IllegalStateException("Logging code is not initialized");
        }
    }

    /**
     * Checks that the logging api is not initialized
     * @throws IllegalStateException If the logging code is already initialized
     * @see #checkInit()
     */
    static void checkNotInit() throws IllegalStateException {
        if(GlobalLogInfo.isInit()) {
            throw new IllegalStateException("Logging code already initialized");
        }
    }

    /**
     * Handles an {@link Exception} in the logging code by notifying the user and disabling the corresponding sector of code
     * @param sector The sector to disable. <code>true</code> for event logging, and <code>false</code> for data logging
     * @param task A string representing the task that caused the error to occur
     * @param error The {@link Exception} that occured or <code>null</code> if the exception could not be obtained
     * @see #handleLoggingApiDisableError(String, Exception)
     */
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

    /**
     * Handles an {@link Exception} in the logging code that causes the failure of all of the logging code by notifying the user and disabling the logging code
     * @param task A string representing the task that caused the error to occur
     * @param error The {@link Exception} that occured or <code>null</code> if the exception could not be obtained
     * @see #handleLoggingError(boolean, String, Exception)
     */
    static void handleLoggingApiDisableError(String task, Exception error) {
        System.err.println("Error occured while " + task + ". Logging will be disabled.");
        if(error != null) {
            error.printStackTrace(System.err);
        }
        disableEvents();
        disableData();
    }

    /**
     * Handles an {@link IllegalStateException} caused by an illegal state of the logging code by notifying the user
     * @param e The {@link IllegalStateException} to handle
     */
	static void handleIllegalState(IllegalStateException e) {
        if(e == null) {
            System.err.println("IllegalStateException occurred in logging code. IsInit=" + GlobalLogInfo.isInit());
        }
        StackTraceElement error = e.getStackTrace()[0];
        System.err.println(e.getMessage() + "! Caused by: " + error.getClassName() + "." + error.getMethodName() + ":" + error.getLineNumber());
	}

    private LogUtils() {}

}