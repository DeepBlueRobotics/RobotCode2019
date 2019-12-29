package frc.robot.lib.logging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;
import java.util.logging.Level;

import org.apache.commons.csv.CSVFormat;

/**
 * Provides an interface through which to access the logging code
 */
public final class Log {

    private static int step = 0, interval = 1;

    /**
     * Initializes the logging code to log data with {@link CSVFormat#DEFAULT}. This is the same as calling <code>Log.init(CSVFormat.DEFAULT)</code>
     * @see #init(CSVFormat)
     */
    public static void init() {
        init(CSVFormat.DEFAULT);
    }

    /**
     * Initializes the logging code
     * @param dataFormat The {@link CSVFormat} to use when logging data
     * @throws IllegalStateException If the logging code is already initialized
     * @see #init()
     */
    public static void init(CSVFormat dataFormat) throws IllegalStateException {
        try {
            LogUtils.checkNotInit();
            LogFiles.init(dataFormat);
            EventLog.init();
            DataLog.init();
        } catch(IllegalStateException e) {
            LogUtils.handleIllegalState(e);
        }
    }

    //Event Logging Code
    /**
     * Logs an {@link Throwable} to the log file. This is the same as <code>Log.logException(null, cause)</code>
     * @param cause The {@link Throwable} to log
     * @see #logException(String, Throwable)
     */
    public static void logException(Throwable cause) {
        if(GlobalLogInfo.areEventsDisabled()) {
            return;
        }
        try {
            LogUtils.checkInit();
            logException(null, cause);
        } catch(IllegalStateException e) {
            LogUtils.handleIllegalState(e);
        }
    }

    /**
     * Logs an {@link Throwable} to the log file
     * @param message The message to log with the {@link Throwable}. This will be replaced with {@link Throwable#getMessage()} if <code>null</code> or <code>""</code>
     * @param cause The {@link Throwable} to log
     * @see #logException(Throwable)
     */
    public static void logException(String message, Throwable cause) {
        if(GlobalLogInfo.areEventsDisabled()) {
            return;
        }
        try {
            LogUtils.checkInit();
            EventLog.logException(message, cause);
        } catch(IllegalStateException e) {
            LogUtils.handleIllegalState(e);
        }
    }

    /**
     * Logs an error message to the log file
     * @param message The error message to log
     * @see #logWarningMessage(String)
     * @see #logInfoMessage(String)
     * @see #logConfigMessage(String)
     */
    public static void logErrorMessage(String message) {
        if(GlobalLogInfo.areEventsDisabled()) {
            return;
        }
        try {
            LogUtils.checkInit();
            EventLog.log(message, Level.SEVERE);
        } catch(IllegalStateException e) {
            LogUtils.handleIllegalState(e);
        }
    }

    /**
     * Logs a warning message to the log file
     * @param message The warning message to log
     * @see #logErrorMessage(String)
     * @see #logInfoMessage(String)
     * @see #logConfigMessage(String)
     */
    public static void logWarningMessage(String message) {
        if(GlobalLogInfo.areEventsDisabled()) {
            return;
        }
        try {
            LogUtils.checkInit();
            EventLog.log(message, Level.WARNING);
        } catch(IllegalStateException e) {
            LogUtils.handleIllegalState(e);
        }
    }

    /**
     * Logs a info message to the log file
     * @param message The info message to log
     * @see #logErrorMessage(String)
     * @see #logWarningMessage(String)
     * @see #logConfigMessage(String)
     */
    public static void logInfoMessage(String message) {
        if(GlobalLogInfo.areEventsDisabled()) {
            return;
        }
        try {
            LogUtils.checkInit();
            EventLog.log(message, Level.INFO);
        } catch(IllegalStateException e) {
            LogUtils.handleIllegalState(e);
        }
    }

    /**
     * Logs a config message to the log file
     * @param message The config message to log
     * @see #logErrorMessage(String)
     * @see #logWarningMessage(String)
     * @see #logInfoMessage(String)
     */
    public static void logConfigMessage(String message) {
        if(GlobalLogInfo.areEventsDisabled()) {
            return;
        }
        try {
            LogUtils.checkInit();
            EventLog.log(message, Level.CONFIG);
        } catch(IllegalStateException e) {
            LogUtils.handleIllegalState(e);
        }
    }

    /**
     * Sets the lowest {@link Level} of message to log
     * @param level The {@link Level} to set as the minumum
     */
    public static void setLoggingLevel(Level level) {
        if(GlobalLogInfo.areEventsDisabled()) {
            return;
        }
        EventLog.setLoggingLevel(level);
    }

    /**
     * Turns off event logging temporarily to be restored with an appropriate call to {@link #setLoggingLevel(Level)}. This is the same as calling <code>Log.setLoggingLevel(Level.OFF)</code>
     */
    public static void disableEventLogging() {
        if(GlobalLogInfo.areEventsDisabled()) {
            return;
        }
        setLoggingLevel(Level.OFF);
    }

    //Data Logging Code
    private static Supplier<Object> createSupplier(Supplier<?> supplier) {
        return () -> supplier.get();
    }

    /**
     * Registers a boolean variable to be logged
     * @param id The id to associate with the variable
     * @param supplier The {@link Supplier} that will be used to load the variable data when {@link DataLog#fetchData()} is called
     * @see #registerIntegerVar(String, Supplier)
     * @see #registerDoubleVar(String, Supplier)
     * @see #registerStringVar(String, Supplier)
     */
    public static void registerBooleanVar(String id, Supplier<Boolean> supplier) {
        if(GlobalLogInfo.isDataDisabled()) {
            return;
        }
        try {
            LogUtils.checkNotInit();
            DataLog.registerVar(VarType.BOOLEAN, id, createSupplier(supplier));
        } catch(IllegalStateException e) {
            LogUtils.handleIllegalState(e);
        } catch(IllegalArgumentException e) {
            DataLog.handleIllegalArgumentException(e);
        }
    }

    /**
     * Registers an integer variable to be logged
     * @param id The id to associate with the variable
     * @param supplier The {@link Supplier} that will be used to load the variable data when {@link DataLog#fetchData()} is called
     * @see #registerBooleanVar(String, Supplier)
     * @see #registerDoubleVar(String, Supplier)
     * @see #registerStringVar(String, Supplier)
     */
    public static void registerIntegerVar(String id, Supplier<Integer> supplier) {
        if(GlobalLogInfo.isDataDisabled()) {
            return;
        }
        try {
            LogUtils.checkNotInit();
            DataLog.registerVar(VarType.INTEGER, id, createSupplier(supplier));
        } catch(IllegalStateException e) {
            LogUtils.handleIllegalState(e);
        } catch(IllegalArgumentException e) {
            DataLog.handleIllegalArgumentException(e);
        }
    }

    /**
     * Registers a double variable to be logged
     * @param id The id to associate with the variable
     * @param supplier The {@link Supplier} that will be used to load the variable data when {@link DataLog#fetchData()} is called
     * @see #registerBooleanVar(String, Supplier)
     * @see #registerIntegerVar(String, Supplier)
     * @see #registerStringVar(String, Supplier)
     */
    public static void registerDoubleVar(String id, Supplier<Double> supplier) {
        if(GlobalLogInfo.isDataDisabled()) {
            return;
        }
        try {
            LogUtils.checkNotInit();
            DataLog.registerVar(VarType.DOUBLE, id, createSupplier(supplier));
        } catch(IllegalStateException e) {
            LogUtils.handleIllegalState(e);
        } catch(IllegalArgumentException e) {
            DataLog.handleIllegalArgumentException(e);
        }
    }

    /**
     * Registers a string variable to be logged
     * @param id The id to associate with the variable
     * @param supplier The {@link Supplier} that will be used to load the variable data when {@link #fetchData()} is called
     * @see #registerBooleanVar(String, Supplier)
     * @see #registerIntegerVar(String, Supplier)
     * @see #registerDoubleVar(String, Supplier)
     */
    public static void registerStringVar(String id, Supplier<String> supplier) {
        if(GlobalLogInfo.isDataDisabled()) {
            return;
        }
        try {
            LogUtils.checkNotInit();
            DataLog.registerVar(VarType.STRING, id, createSupplier(supplier));
        } catch(IllegalStateException e) {
            LogUtils.handleIllegalState(e);
        } catch(IllegalArgumentException e) {
            DataLog.handleIllegalArgumentException(e);
        }
    }

    /**
     * Fetches data from the {@link Supplier}s and puts it into a {@link HashMap} accessable from {@link #getData()}
     * @throws IllegalStateException If the data logging code is not yet initialized
     */
    public static void fetchData() {
        if(GlobalLogInfo.isDataDisabled()) {
            return;
        }
        try {
            LogUtils.checkInit();
            DataLog.fetchData();
        } catch(IllegalStateException e) {
            LogUtils.handleIllegalState(e);
        }
    }

    /**
     * @return An {@link ArrayList} containing the ids of all registered variabless
     */
    public static ArrayList<String> getVarIds() {
        return DataLog.getVarIds();
    }

    /**
     * @return A {@link HashMap} mapping all registered variable ids to their respective {@link VarType}
     */
    public static HashMap<String, VarType> getVarTypes() {
        return DataLog.getTypes();
    }

    /**
     * @return A {@link HashMap} mapping all registered variable ids to their last fetched value or an empty map if data has not yet been fetched
     */
    public static HashMap<String, Object> getData() {
        return DataLog.getData();
    }

    /**
     * Puts the last set of fetched data to {@link SmartDashboard} or fetches new data if none has been fetched yet
     */
    public static void putDataToSmartDashboard() {
        if(GlobalLogInfo.isDataDisabled()) {
            return;
        }
        try {
            LogUtils.checkInit();
            DataLog.putSmartDashboardData();
        } catch(IllegalStateException e) {
            LogUtils.handleIllegalState(e);
        }
    }

    /**
     * @return The data logging interval
     * @see #setDataLogInterval(int)
     * @see #logData()
     */
    public static int getDataLogInterval() {
        return interval;
    }

    /**
     * Sets how often data should be logged
     * @param interval The interval with which data should be logged
     * @see #getDataLogInterval()
     * @see #logData()
     */
    public static void setDataLogInterval(int interval) {
        Log.interval = (interval < 1 ? 1 : interval);
    }

    /**
     * Logs data to the data file or returns if determined by the data logging interval where each interval unit represents one logData call
     * @see #getDataLoginterval()
     * @see #setDataLoginterval(int)
     */
    public static void logData() {
        if(GlobalLogInfo.isDataDisabled()) {
            return;
        }
        try {
            LogUtils.checkInit();
            if(step == 0) {
                DataLog.logData();
            }
            step++;
            step = step >= interval ? 0 : step;
        } catch(IllegalStateException e) {
            LogUtils.handleIllegalState(e);
        }
    }

    private Log() {}

}
