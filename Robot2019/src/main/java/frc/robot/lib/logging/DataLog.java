package frc.robot.lib.logging;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;

import org.apache.commons.csv.CSVPrinter;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Handles data logging code
 */
final class DataLog {

    private static Object[] dataExportBuffer = null;
    private static ArrayList<String> varIds = new ArrayList<>();
    private static HashMap<String, VarType> types = new HashMap<>();
    private static HashMap<String, Object> data = new HashMap<>();
    private static HashMap<String, Supplier<Object>> dataSuppliers = new HashMap<>();
    private static LocalDateTime refLocalDateTime;
    private static long refFGATime;

    static {
        refLocalDateTime = LocalDateTime.now();
        refFGATime = RobotController.getFPGATime();
        registerVar(VarType.STRING, "Timestamp", () -> refLocalDateTime.plusNanos(1000*(RobotController.getFPGATime()-refFGATime)).format(GlobalLogInfo.dateTimeFormat));
    }

    /**
     * Initializes the data logging code and prints variable ids to the csv file or returns if it has already been initialized
     */
    static void init() {
        if(!GlobalLogInfo.isInit()) {
            return;
        }
        try {
            CSVPrinter printer = GlobalLogInfo.getDataPrinter();
            printer.printRecord(varIds.toArray());
            printer.flush();
        } catch(IOException e) {
            LogUtils.handleLoggingError(false, "printing csv headers", e);
        }
    }

    /**
     * Registers a variable to be logged whenever {@link #logData()} is called. Must be called before {@link #init()}
     * @param type The {@link VarType} of the variable
     * @param id The id to associate with the variable
     * @param supplier The {@link Supplier} that will be used to load the variable data when {@link #fetchData()} is called
     * @throws IllegalArgumentException If the provided variable id has already been registered
     * @throws IllegalStateException If the data logging code has already been initialized
     */
    static void registerVar(VarType type, String id, Supplier<Object> supplier) throws IllegalArgumentException, IllegalStateException {
        LogUtils.checkNotInit();
        if(varIds.contains(id)) {
            throw new IllegalArgumentException("Variable is already registered");
        }
        varIds.add(id);
        types.put(id, type);
        dataSuppliers.put(id, supplier);
    }

    /**
     * Fetches variable data and then prints it to the csv file and {@link SmartDashboard}
     * @throws IllegalStateException If the data logging code is not initialized
     */
    static void logData() throws IllegalStateException {
        LogUtils.checkInit();
        fetchData();
        TimeLog.startDataLogCycle();
        try {
            CSVPrinter printer = GlobalLogInfo.getDataPrinter();
            printer.printRecord((Object[])exportData());
        } catch(IOException e) {
            LogUtils.handleLoggingError(false, "writing data", e);
        }
        TimeLog.endDataLogCycle();
        putSmartDashboardData();
    }

    /**
     * Fetches data from the {@link Supplier}s and puts it into a {@link HashMap} accessable from {@link #getData()}
     * @throws IllegalStateException If the data logging code is not yet initialized
     */
    static void fetchData() throws IllegalStateException {
        LogUtils.checkInit();
        TimeLog.startDataFetchCycle();
        for(String id: varIds) {
            data.put(id, dataSuppliers.get(id).get());
        }
        TimeLog.endDataFetchCycle();
    }

    private static Object[] exportData() {
        if(dataExportBuffer == null) {
            dataExportBuffer = new Object[varIds.size()];
        }
        for(int i = 0; i < varIds.size(); i++) {
            dataExportBuffer[i] = data.get(varIds.get(i));
        }
        return dataExportBuffer;
    }

    /**
     * Puts the last set of fetched data to {@link SmartDashboard} or fetches new data if none has been fetched yet
     * @throws IllegalStateException If new data has to be fetched and the data logging api has not been initialized
     */
    static void putSmartDashboardData() throws IllegalStateException {
        if(data.size() != varIds.size()) {
            fetchData();
        }
        for(String id: varIds) {
            try {
                switch(types.get(id)) {
                    case BOOLEAN:
                        SmartDashboard.putBoolean(id, (Boolean)data.get(id));
                        break;
                    case INTEGER:
                        SmartDashboard.putNumber(id, (Integer)data.get(id));
                        break;
                    case DOUBLE:
                        SmartDashboard.putNumber(id, (Double)data.get(id));
                        break;
                    case STRING:
                        SmartDashboard.putString(id, (String)data.get(id));
                        break;
                }
            } catch(Exception e) {}
        }
    }

    /**
     * @return An {@link ArrayList} containing the ids of all registered variabless
     */
    static ArrayList<String> getVarIds() {
        return new ArrayList<>(varIds);
    }

    /**
     * @return A {@link HashMap} mapping all registered variable ids to their respective {@link VarType}
     */
    static HashMap<String, VarType> getTypes() {
        return new HashMap<>(types);
    }

    /**
     * @return A {@link HashMap} mapping all registered variable ids to their last fetched value or an empty map if data has not yet been fetched
     */
    static HashMap<String, Object> getData() {
        return new HashMap<>(data);
    }

    static void handleIllegalArgumentException(IllegalArgumentException e) {
        if(e == null) {
            System.err.println("IllegalArgumentException occured in logging code.");
        }
        StackTraceElement error = e.getStackTrace()[0];
        System.err.println(e.getMessage() + "! Caused by: " + error.getClassName() + "." + error.getMethodName() + ":" + error.getLineNumber());
    }

    private DataLog() {}

}