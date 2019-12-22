package frc.robot.lib.logging;

import java.io.File;

public final class GlobalLogInfo {

    private static boolean isInit, areEventsDisabled, isDataDisabled;
    private static File eventFile, dataFile;

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

    static void disableEvents() {
        areEventsDisabled = true;
    }

    static void disableData() {
        isDataDisabled = true;
    }

    static void init(File eventFile, File dataFile) {
        GlobalLogInfo.eventFile = eventFile;
        GlobalLogInfo.dataFile = dataFile;
        isInit = true;
    }

}