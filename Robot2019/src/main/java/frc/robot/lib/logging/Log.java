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

    private static int step = 0, frequency = 1;

    /**
     * Initializes the logging code to log data with {@link CSVFormat#DEFAULT}. This is the same as calling <code>Log.init(CSVFormat.DEFAULT)</code>
     * @throws IllegalStateException If the logging code is already initialized
     * @see #init(CSVFormat)
     */
    public static void init() throws IllegalStateException {
        init(CSVFormat.DEFAULT);
    }

    /**
     * Initializes the logging code
     * @param dataFormat The {@link CSVFormat} to use when logging data
     * @throws IllegalStateException If the logging code is already initialized
     * @see #init()
     */
    public static void init(CSVFormat dataFormat) throws IllegalStateException {
        LogUtils.checkNotInit();
        LogFiles.init(dataFormat);
    }

    //Event Logging Code
    /**
     * Logs an {@link Throwable} to the log file. This is the same as <code>Log.logException(null, cause)</code>
     * @param cause The {@link Throwable} to log
     * @throws IllegalStateException If the event logging code is not yet initialized
     * @see #logException(String, Throwable)
     */
    public static void logException(Throwable cause) throws IllegalStateException {
        if(GlobalLogInfo.areEventsDisabled()) {
            return;
        }
        LogUtils.checkInit();
        logException(null, cause);
    }

    /**
     * Logs an {@link Throwable} to the log file
     * @param message The message to log with the {@link Throwable}. This will be replaced with {@link Throwable#getMessage()} if <code>null</code> or <code>""</code>
     * @param cause The {@link Throwable} to log
     * @throws IllegalStateException If the event logging code is not yet initialized
     * @see #logException(Throwable)
     */
    public static void logException(String message, Throwable cause) throws IllegalStateException {
        if(GlobalLogInfo.areEventsDisabled()) {
            return;
        }
        LogUtils.checkInit();
        EventLog.logException(message, cause);
    }

    /**
     * Logs an error message to the log file
     * @param message The error message to log
     * @throws IllegalStateException If the event logging code is not yet initialized
     * @see #logWarningMessage(String)
     * @see #logInfoMessage(String)
     * @see #logConfigMessage(String)
     */
    public static void logErrorMessage(String message) throws IllegalStateException {
        if(GlobalLogInfo.areEventsDisabled()) {
            return;
        }
        LogUtils.checkInit();
        EventLog.log(message, Level.SEVERE);
    }

    /**
     * Logs a warning message to the log file
     * @param message The warning message to log
     * @throws IllegalStateException If the event logging code is not yet initialized
     * @see #logErrorMessage(String)
     * @see #logInfoMessage(String)
     * @see #logConfigMessage(String)
     */
    public static void logWarningMessage(String message) throws IllegalStateException {
        if(GlobalLogInfo.areEventsDisabled()) {
            return;
        }
        LogUtils.checkInit();
        EventLog.log(message, Level.WARNING);
    }

    /**
     * Logs a info message to the log file
     * @param message The info message to log
     * @throws IllegalStateException If the event logging code is not yet initialized
     * @see #logErrorMessage(String)
     * @see #logWarningMessage(String)
     * @see #logConfigMessage(String)
     */
    public static void logInfoMessage(String message) throws IllegalStateException {
        if(GlobalLogInfo.areEventsDisabled()) {
            return;
        }
        LogUtils.checkInit();
        EventLog.log(message, Level.INFO);
    }

    /**
     * Logs a config message to the log file
     * @param message The config message to log
     * @throws IllegalStateException If the event logging code is not yet initialized
     * @see #logErrorMessage(String)
     * @see #logWarningMessage(String)
     * @see #logInfoMessage(String)
     */
    public static void logConfigMessage(String message) throws IllegalStateException {
        if(GlobalLogInfo.areEventsDisabled()) {
            return;
        }
        LogUtils.checkInit();
        EventLog.log(message, Level.CONFIG);
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
     * @throws IllegalArgumentException If the provided variable id has already been registered
     * @throws IllegalStateException If the data logging code has already been initialized
     * @see #registerIntegerVar(String, Supplier)
     * @see #registerDoubleVar(String, Supplier)
     * @see #registerStringVar(String, Supplier)
     */
    public static void registerBooleanVar(String id, Supplier<Boolean> supplier) throws IllegalArgumentException, IllegalStateException {
        if(GlobalLogInfo.isDataDisabled()) {
            return;
        }
        LogUtils.checkNotInit();
        DataLog.registerVar(VarType.BOOLEAN, id, createSupplier(supplier));
    }

    /**
     * Registers an integer variable to be logged
     * @param id The id to associate with the variable
     * @param supplier The {@link Supplier} that will be used to load the variable data when {@link DataLog#fetchData()} is called
     * @throws IllegalArgumentException If the provided variable id has already been registered
     * @throws IllegalStateException If the data logging code has already been initialized
     * @see #registerBooleanVar(String, Supplier)
     * @see #registerDoubleVar(String, Supplier)
     * @see #registerStringVar(String, Supplier)
     */
    public static void registerIntegerVar(String id, Supplier<Integer> supplier) throws IllegalArgumentException, IllegalStateException {
        if(GlobalLogInfo.isDataDisabled()) {
            return;
        }
        LogUtils.checkNotInit();
        DataLog.registerVar(VarType.INTEGER, id, createSupplier(supplier));
    }

    /**
     * Registers a double variable to be logged
     * @param id The id to associate with the variable
     * @param supplier The {@link Supplier} that will be used to load the variable data when {@link DataLog#fetchData()} is called
     * @throws IllegalArgumentException If the provided variable id has already been registered
     * @throws IllegalStateException If the data logging code has already been initialized
     * @see #registerBooleanVar(String, Supplier)
     * @see #registerIntegerVar(String, Supplier)
     * @see #registerStringVar(String, Supplier)
     */
    public static void registerDoubleVar(String id, Supplier<Double> supplier) throws IllegalArgumentException, IllegalStateException {
        if(GlobalLogInfo.isDataDisabled()) {
            return;
        }
        LogUtils.checkNotInit();
        DataLog.registerVar(VarType.DOUBLE, id, createSupplier(supplier));
    }

    /**
     * Registers a string variable to be logged
     * @param id The id to associate with the variable
     * @param supplier The {@link Supplier} that will be used to load the variable data when {@link #fetchData()} is called
     * @throws IllegalArgumentException If the provided variable id has already been registered
     * @throws IllegalStateException If the data logging code has already been initialized
     * @see #registerBooleanVar(String, Supplier)
     * @see #registerIntegerVar(String, Supplier)
     * @see #registerDoubleVar(String, Supplier)
     */
    public static void registerStringVar(String id, Supplier<String> supplier) throws IllegalArgumentException, IllegalStateException {
        if(GlobalLogInfo.isDataDisabled()) {
            return;
        }
        LogUtils.checkNotInit();
        DataLog.registerVar(VarType.STRING, id, createSupplier(supplier));
    }

    /**
     * Fetches data from the {@link Supplier}s and puts it into a {@link HashMap} accessable from {@link #getData()}
     * @throws IllegalStateException If the data logging code is not yet initialized
     */
    public static void fetchData() throws IllegalStateException {
        if(GlobalLogInfo.isDataDisabled()) {
            return;
        }
        LogUtils.checkInit();
        DataLog.fetchData();
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
     * @throws IllegalStateException If new data has to be fetched and the data logging api has not been initialized
     */
    public static void putDataToSmartDashboard() throws IllegalStateException {
        if(GlobalLogInfo.isDataDisabled()) {
            return;
        }
        LogUtils.checkInit();
        DataLog.putSmartDashboardData();
    }

    /**
     * @return How often data is logged
     * @see #setDataLogFrequency(int)
     * @see #logData()
     */
    public static int getDataLogFrequency() {
        return frequency;
    }

    /**
     * Sets how often data should be logged
     * @param frequency The frequency with which data should be logged
     * @see #getDataLogFrequency()
     * @see #logData()
     */
    public static void setDataLogFrequency(int frequency) {
        Log.frequency = (frequency < 1 ? 1 : frequency);
    }

    /**
     * Logs data to the data file or pauses if determined by the data logging frequency
     * @throws IllegalStateException If the data logging code is not yet initialized
     * @see #getDataLogFrequency()
     * @see #setDataLogFrequency(int)
     */
    public static void logData() throws IllegalStateException {
        if(GlobalLogInfo.isDataDisabled()) {
            return;
        }
        LogUtils.checkInit();
        if(step == 0) {
            DataLog.logData();
        }
        step++;
        step = step >= frequency ? 0 : step;
    }

    private Log() {}

}
