package frc.robot.lib.logging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;
import java.util.logging.Level;

import org.apache.commons.csv.CSVFormat;

public final class Log {

    private static int step = 0, frequency = 1;

    public static void init() throws IllegalStateException {
        init(CSVFormat.DEFAULT);
    }

    public static void init(CSVFormat dataFormat) throws IllegalStateException {
        LogUtils.checkNotInit();
        LogFiles.init(dataFormat);
    }

    //Event Logging Code
    public static void logException(Throwable cause) {
        logException(null, cause);
    }

    public static void logException(String message, Throwable cause) {
        if(GlobalLogInfo.areEventsDisabled()) {
            return;
        }
        EventLog.logException(message, cause);
    }

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

    //Data Logging Code
    private static Supplier<Object> createSupplier(Supplier<?> supplier) {
        return () -> supplier.get();
    }

    public static void registerBooleanVar(String id, Supplier<Boolean> supplier) throws IllegalArgumentException, IllegalStateException {
        LogUtils.checkNotInit();
        DataLog.registerVar(VarType.BOOLEAN, id, createSupplier(supplier));
    }

    public static void registerIntegerVar(String id, Supplier<Integer> supplier) throws IllegalArgumentException, IllegalStateException {
        LogUtils.checkNotInit();
        DataLog.registerVar(VarType.INTEGER, id, createSupplier(supplier));
    }

    public static void registerDoubleVar(String id, Supplier<Double> supplier) throws IllegalArgumentException, IllegalStateException {
        LogUtils.checkNotInit();
        DataLog.registerVar(VarType.DOUBLE, id, createSupplier(supplier));
    }

    public static void registerStringVar(String id, Supplier<String> supplier) throws IllegalArgumentException, IllegalStateException {
        LogUtils.checkNotInit();
        DataLog.registerVar(VarType.STRING, id, createSupplier(supplier));
    }

    public static void fetchData() throws IllegalStateException {
        LogUtils.checkInit();
        DataLog.fetchData();
    }

    public static ArrayList<String> getVarIds() throws IllegalStateException {
        LogUtils.checkInit();
        return DataLog.getVarIds();
    }

    public static HashMap<String, VarType> getVarTypes() throws IllegalStateException {
        LogUtils.checkInit();
        return DataLog.getTypes();
    }

    public static HashMap<String, Object> getData() throws IllegalStateException {
        LogUtils.checkInit();
        return DataLog.getData();
    }

    public static void putDataToSmartDashboard() throws IllegalStateException {
        LogUtils.checkInit();
        DataLog.putSmartDashboardData();
    }

    public static int getDataLogFrequency() {
        return frequency;
    }

    public static void setDataLogFrequency(int frequency) {
        Log.frequency = (frequency < 1 ? 1 : frequency);
    }

    public static void logData() throws IllegalStateException {
        LogUtils.checkInit();
        if(step == 0) {
            DataLog.logData();
        }
        step++;
        step = step == frequency ? 0 : step;
    }

    private Log() {}

}
