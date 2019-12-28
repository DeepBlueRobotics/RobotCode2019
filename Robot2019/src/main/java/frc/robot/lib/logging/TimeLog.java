package frc.robot.lib.logging;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.RobotController;

/**
 * Keeps track of the time spent on different sections of the logging code
 */
public final class TimeLog {

    private static long eventStart, dataFetchStart, dataLogStart;
    private static double eventMax, eventAvg, dataFetchMax, dataFetchAvg, dataLogMax, dataLogAvg;

    static {
        eventStart = -1;
        dataFetchStart = -1;
        dataLogStart = -1;
        eventMax = -1;
        eventAvg = -1;
        dataFetchMax = -1;
        dataFetchAvg = -1;
        dataLogMax = -1;
        dataLogAvg = -1;
    }

    private static long getMillis() {
        return RobotController.getFPGATime()/1000;
    }

    private static double calculateAvg(double original, long newData) {
        if(original == -1) {
            return newData;
        }
        return (original+newData)/2D;
    }

    /**
     * Resets the event log timer
     */
    static void startEventLogCycle() {
        eventStart = getMillis();
    }

    /**
     * Takes the current value of the event log timer and uses it to update the max and avg cycle times
     */
    static void endEventLogCycle() {
        long elapsedTime = getMillis() - eventStart;
        eventMax = Math.max(eventMax, elapsedTime);
        eventAvg = calculateAvg(eventAvg, elapsedTime);
    }

    /**
     * Resets the data fetch timer
     */
    static void startDataFetchCycle() {
        dataFetchStart = getMillis();
    }

    /**
     * Takes the current value of the data fetch timer and uses it to update the max and avg cycle times
     */
    static void endDataFetchCycle() {
        long elapsedTime = getMillis() - dataFetchStart;
        dataFetchMax = Math.max(dataFetchMax, elapsedTime);
        dataFetchAvg = calculateAvg(dataFetchAvg, elapsedTime);
    }

    /**
     * Resets the data log timer
     */
    static void startDataLogCycle() {
        dataLogStart = getMillis();
    }

    /**
     * Takes the current value of the data log timer and uses it to update the max and avg cycle times
     */
    static void endDataLogCycle() {
        long elapsedTime = getMillis() - dataLogStart;
        dataLogMax = Math.max(dataLogMax, elapsedTime);
        dataLogAvg = calculateAvg(dataLogAvg, elapsedTime);
    }

    /**
     * @return The max time spent logging events
     */
    public static double getMaxEventLogCycleTime() {
        return eventMax;
    }

    /**
     * @return The average time spent logging events
     */
    public static double getAvgEventLogCycleTime() {
        return eventAvg;
    }

    /**
     * @return The max time spent fetching data
     */
    public static double getMaxDataFetchCycleTime() {
        return dataFetchMax;
    }

    /**
     * @return The average time spent fetching data
     */
    public static double getAvgDataFetchCycleTime() {
        return dataFetchAvg;
    }

    /**
     * @return The max time spent logging data
     */
    public static double getMaxDataLogCycleTime() {
        return dataLogMax;
    }

    /**
     * @return The average time spent logging data
     */
    public static double getAvgDataLogCycleTime() {
        return dataLogAvg;
    }

    /**
     * Sets up the data logging code to log the max and avg cycle times
     * @throws IllegalStateException If the data logging code is already initialized or the required variable ids are already allocated
     */
    public static void pushDataToCSV() throws IllegalStateException {
        LogUtils.checkNotInit();
        ArrayList<String> varIds = Log.getVarIds();
        if(varIds.contains("Max Event Log Cycle") || varIds.contains("Avg Event Log Cycle") ||
            varIds.contains("Max Data Fetch Cycle") || varIds.contains("Avg Data Fetch Cycle") ||
            varIds.contains("Max Data Log Cycle") || varIds.contains("Avg Data Log Cycle")) {
                throw new IllegalStateException("Not all variable ids could be allocated");
        }
        Log.registerDoubleVar("Max Event Log Cycle", TimeLog::getMaxEventLogCycleTime);
        Log.registerDoubleVar("Avg Event Log Cycle", TimeLog::getAvgEventLogCycleTime);
        Log.registerDoubleVar("Max Data Fetch Cycle", TimeLog::getMaxDataFetchCycleTime);
        Log.registerDoubleVar("Avg Data Fetch Cycle", TimeLog::getAvgDataFetchCycleTime);
        Log.registerDoubleVar("Max Data Log Cycle", TimeLog::getMaxDataLogCycleTime);
        Log.registerDoubleVar("Avg Data Log Cycle", TimeLog::getAvgDataLogCycleTime);
    }

    private TimeLog() {}

}