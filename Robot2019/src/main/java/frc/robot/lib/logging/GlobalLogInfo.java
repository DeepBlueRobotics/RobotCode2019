package frc.robot.lib.logging;

import java.io.File;

import org.apache.commons.csv.CSVPrinter;

public final class GlobalLogInfo {

    private static boolean isInit, areEventsDisabled, isDataDisabled;
    private static File eventFile, dataFile;
    private static CSVPrinter dataPrinter;

    public static boolean isInit() {
        return isInit;
    }

    public static boolean areEventsDisabled() {
        return areEventsDisabled;
    }

    public static boolean isDataDisabled() {
        return isDataDisabled;
    }

    public static File getEventFile() {
        return eventFile;
    }

    public static File getDataFile() {
        return dataFile;
    }

    static CSVPrinter getDataPriter() {
        return dataPrinter;
    }

    static void disableEvents() {
        areEventsDisabled = true;
    }

    static void disableData() {
        isDataDisabled = true;
    }

    static void init(File eventFile, File dataFile, CSVPrinter dataPrinter) {
        if(isInit()) {
            return;
        }
        GlobalLogInfo.eventFile = eventFile;
        GlobalLogInfo.dataFile = dataFile;
        GlobalLogInfo.dataPrinter = dataPrinter;
        isInit = true;
        EventLog.init();
        DataLog.init();
    }

    private GlobalLogInfo() {}

}