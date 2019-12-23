package frc.robot.lib.logging;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;

import org.apache.commons.csv.CSVPrinter;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Handles data logging code
 */
final class DataLog {

    private static ArrayList<String> varIds = new ArrayList<>();
    private static HashMap<String, VarType> types = new HashMap<>();
    private static HashMap<String, Object> data = new HashMap<>();
    private static HashMap<String, Supplier<Object>> dataSuppliers = new HashMap<>();

    static {
        registerVar(VarType.STRING, "Timestamp", () -> LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS")));
    }

    /**
     * Initializes the data logging code and prints variable ids to the csv file or returns if it has already been initialized
     */
    static void init() {
        if(!GlobalLogInfo.isInit()) {
            return;
        }
        try {
            CSVPrinter printer = GlobalLogInfo.getDataPriter();
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
        try {
            CSVPrinter printer = GlobalLogInfo.getDataPriter();
            printer.printRecord((Object[])exportData());
            printer.flush();
        } catch(IOException e) {
            LogUtils.handleLoggingError(false, "writing data", e);
        }
        putSmartDashboardData();
    }

    /**
     * Fetches data from the {@link Supplier}s and puts it into a {@link HashMap} accessable from {@link #getData()}
     * @throws IllegalStateException If the data logging code is not yet initialized
     */
    static void fetchData() throws IllegalStateException {
        LogUtils.checkInit();
        for(String id: varIds) {
            data.put(id, dataSuppliers.get(id).get());
        }
    }

    private static Object[] exportData() {
        Object[] out = new Object[varIds.size()];
        for(int i = 0; i < varIds.size(); i++) {
            out[i] = data.get(varIds.get(i));
        }
        return out;
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

    private DataLog() {}

}