package frc.robot.lib.logging;

import java.io.File;
import java.time.format.DateTimeFormatter;

import org.apache.commons.csv.CSVPrinter;

/**
 * Stores global information about the state of the logging code
 */
public final class GlobalLogInfo {

    private static boolean isInit, areEventsDisabled, isDataDisabled;
    private static File eventFile, dataFile;
    private static CSVPrinter dataPrinter;
    public static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    /**
     * @return Whether the logging code has been initialized
     */
    public static boolean isInit() {
        return isInit;
    }

    /**
     * @return Whether event logging code has been disabled
     */
    public static boolean areEventsDisabled() {
        return areEventsDisabled;
    }

    /**
     * @return Whether data logging code has been disabled
     */
    public static boolean isDataDisabled() {
        return isDataDisabled;
    }

    /**
     * @return The {@link File} to which events are being logged
     */
    public static File getEventFile() {
        return eventFile;
    }

    /**
     * @return The {@link File} to which data is being logged
     */
    public static File getDataFile() {
        return dataFile;
    }

    /**
     * @return The {@link CSVPrinter} being used to log data
     */
    static CSVPrinter getDataPrinter() {
        return dataPrinter;
    }

    /**
     * Disables event logging
     */
    static void disableEvents() {
        areEventsDisabled = true;
    }

    /**
     * Disables data logging
     */
    static void disableData() {
        isDataDisabled = true;
    }

    /**
     * Initializes the required global data as well as {@EventLog} and {@link DataLog} or returns if it has already been initialized
     * @param eventFile The {@link File} to which events will be logged
     * @param dataFile The {@link File} to which data will be logged
     * @param dataPrinter The {@link CSVPrinter} to use to log data
     */
    static void init(File eventFile, File dataFile, CSVPrinter dataPrinter) {
        if(isInit()) {
            return;
        }
        GlobalLogInfo.eventFile = eventFile;
        GlobalLogInfo.dataFile = dataFile;
        GlobalLogInfo.dataPrinter = dataPrinter;
        isInit = true;
    }

    private GlobalLogInfo() {}

}