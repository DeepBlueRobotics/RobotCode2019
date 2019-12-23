package frc.robot.lib.logging;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;

import org.apache.commons.csv.CSVPrinter;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

final class DataLog {

    private static ArrayList<String> varIds = new ArrayList<>();
    private static HashMap<String, VarType> types = new HashMap<>();
    private static HashMap<String, Object> data = new HashMap<>();
    private static HashMap<String, Supplier<Object>> dataSuppliers = new HashMap<>();

    static {
        registerVar(VarType.STRING, "Timestamp", () -> LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS")));
    }

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

    static void registerVar(VarType type, String id, Supplier<Object> supplier) throws IllegalArgumentException, IllegalStateException {
        LogUtils.checkNotInit();
        if(varIds.contains(id)) {
            throw new IllegalArgumentException("Variable is already registered");
        }
        varIds.add(id);
        types.put(id, type);
        dataSuppliers.put(id, supplier);
    }

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

    static ArrayList<String> getVarIds() {
        return new ArrayList<>(varIds);
    }

    static HashMap<String, VarType> getTypes() {
        return new HashMap<>(types);
    }

    static HashMap<String, Object> getData() {
        return new HashMap<>(data);
    }

    private DataLog() {}

}