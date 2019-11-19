package frc.robot.logging;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import frc.robot.logging.vars.Var;

/**
 * A top level interface for interfacing with the logging API
 */
public class RobotLogger {
    
    private static HashMap<String, Supplier<Boolean>> booleanFunctions;
    private static HashMap<String, Supplier<Integer>> integerFunctions;
    private static HashMap<String, Supplier<Double>> doubleFunctions;
    private static int currentState, waitStates;

    static {
        booleanFunctions = new HashMap<>();
        integerFunctions = new HashMap<>();
        doubleFunctions = new HashMap<>();
        currentState = 0;
        waitStates = 0;
    }

    /**
     * @return The number of method calls to ignore when logging variables
     * @see #setWaitStates(int)
     * @see #logVars()
     */
    public static int getWaitStates() {
        return waitStates;
    }

    /**
     * Sets the number of method calls to ignore when logging variables
     * @param waitStates The new number of method calls to ignore when logging variables'
     * @see #getWaitStates()
     * @see #logVars()
     */
    public static void setWaitStates(int waitStates) {
        RobotLogger.waitStates = waitStates < 0 ? 0 : waitStates;
    }

    /**
     * @return The current value of the variable method counter
     * @see #nextState()
     * @see #logVars()
     */
    public static int getCurrentState() {
        return currentState;
    }

    /**
     * Increments the variable method counter
     * @see #getCurrentState()
     * @see #logVars()
     */
    public static void nextState() {
        if(currentState == waitStates) {
            currentState = 0;
        } else {
            currentState++;
        }
    }

    /**
     * A convenience method for {@link LoggerInterface#init()}
     */
    public static void init() {
        LoggerInterface.init();
    }

    /**
     * A convenience method for {@link LoggerMessageEncoder#logMessage(String)}
     * @param message The message to log
     */
    public static void logMessage(String message) {
        LoggerMessageEncoder.logMessage(message);
    }

    /**
     * A convenience method for {@link LoggerMessageEncoder#logErrorMessage(String)}
     * @param message The error message to log
     */
    public static void logErrorMessage(String message) {
        LoggerMessageEncoder.logErrorMessage(message);
    }

    /**
     * A convenience method for {@link LoggerMessageEncoder#mapId(String, String))}
     * @param id The id of the variable to map
     * @param name The display name of the variable
     */
    public static void registerVariableMapping(String id, String name) {
        LoggerMessageEncoder.mapId(id, name);
    }

    /**
     * Regiters a boolean variable to be logged each variable message cycle
     * @param id The id of the variable to log
     * @param function The function providing the values to log
     * @see #addIntegerVar(String, Supplier)
     * @see #addDoubleVar(String, Supplier)
     * @see #removeBooleanVar(String)
     * @see #getBooleanKeys()
     * @see #logVars()
     */
    public static void addBooleanVar(String id, Supplier<Boolean> function) {
        booleanFunctions.put(id, function);
    }

    /**
     * Regiters an integer variable to be logged each variable message cycle
     * @param id The id of the variable to log
     * @param function The function providing the values to log
     * @see #addBooleanVar(String, Supplier)
     * @see #addDoubleVar(String, Supplier)
     * @see #removeIntegerVar(String)
     * @see #getIntegerKeys()
     * @see #logVars()
     */
    public static void addIntegerVar(String id, Supplier<Integer> function) {
        integerFunctions.put(id, function);
    }

    /**
     * Regiters a double variable to be logged each variable message cycle
     * @param id The id of the variable to log
     * @param function The function providing the values to log
     * @see #addBooleanVar(String, Supplier)
     * @see #addIntegerVar(String, Supplier)
     * @see #removeDoubleVar(String)
     * @see #getDoubleKeys()
     * @see #logVars()
     */
    public static void addDoubleVar(String id, Supplier<Double> function) {
        doubleFunctions.put(id, function);
    }

    /**
     * Removes a boolean variable from being logged
     * @param id The id of the variable to remove
     * @see #addBooleanVar(String, Supplier)
     * @see #removeBooleanVar(String)
     * @see #removeIntegerVar(String)
     * @see #removeDoubleVar(String)
     * @see #getBooleanKeys()
     * @see #logVars()
     */
    public static void removeBooleanVar(String id) {
        booleanFunctions.remove(id);
    }

    /**
     * Removes an integer variable from being logged
     * @param id The id of the variable to remove
     * @see #addIntegerVar(String, Supplier)
     * @see #removeBooleanVar(String)
     * @see #removeDoubleVar(String)
     * @see #getIntegerKeys()
     * @see #logVars()
     */
    public static void removeIntegerVar(String id) {
        integerFunctions.remove(id);
    }

    /**
     * Removes a double variable from being logged
     * @param id The id of the variable to remove
     * @see #addDoubleVar(String, Supplier)
     * @see #removeBooleanVar(String)
     * @see #removeIntegerVar(String)
     * @see #getDoubleKeys()
     * @see #logVars()
     */
    public static void removeDoubleVar(String id) {
        doubleFunctions.remove(id);
    }

    /**
     * @return The ids of all registered boolean variables
     * @see #addBooleanVar(String, Supplier)
     * @see #removeBooleanVar(String)
     * @see #getIntegerKeys()
     * @see #getDoubleKeys()
     * @see #logVars()
     */
    public static HashSet<String> getBooleanKeys() {
        return new HashSet<String>(booleanFunctions.keySet());
    }

    /**
     * @return The ids of all registered integer variables
     * @see #addIntegerVar(String, Supplier)
     * @see #removeIntegerVar(String)
     * @see #getBooleanKeys()
     * @see #getDoubleKeys()
     * @see #logVars()
     */
    public static HashSet<String> getIntegerKeys() {
        return new HashSet<String>(integerFunctions.keySet());
    }

    /**
     * @return The ids of all registered double variables
     * @see #addDoubleVar(String, Supplier)
     * @see #removeDoubleVar(String)
     * @see #getBooleanKeys()
     * @see #getIntegerKeys()
     * @see #logVars()
     */
    public static HashSet<String> getDoubleKeys() {
        return new HashSet<String>(doubleFunctions.keySet());
    }

    /**
     * Logs all registered vars to the log file unless {@link #getWaitStates()} is non-zero
     * @see #getWaitStates()
     * @see #getCurrentState()
     * @see #nextState()
     * @see #addBooleanVar(String, Supplier)
     * @see #addIntegerVar(String, Supplier)
     * @see #addDoubleVar(String, Supplier)
     * @see #removeBooleanVar(String) 
     * @see #removeIntegerVar(String)
     * @see #removeDoubleVar(String)
     * @see #getBooleanKeys()
     * @see #getIntegerKeys()
     * @see #getDoubleKeys()
     */
    public static void logVars() {
        if(currentState == 0) {
            ArrayList<Var<?>> vars = new ArrayList<>();
            vars.addAll(getVars(booleanFunctions, Var::create));
            vars.addAll(getVars(integerFunctions, Var::create));
            vars.addAll(getVars(doubleFunctions, Var::create));
            if(vars.size() == 0) {
                nextState();
                return;
            }
            LoggerMessageEncoder.logVars(vars);
        }
        nextState();
    }

    private static <T> ArrayList<Var<T>> getVars(HashMap<String, Supplier<T>> map, BiFunction<String, T, Var<T>> creationFunction) {
        ArrayList<Var<T>> out = new ArrayList<>();
        for(String id: map.keySet()) {
            out.add(creationFunction.apply(id, map.get(id).get()));
        }
        return out;
    }

    /**
     * A convenience method for {@link LoggerMessageEncoder#logMode(Mode)}
     * @param mode The {@link Mode} to log
     */
    public static void logMode(Mode mode) {
        LoggerMessageEncoder.logMode(mode);
    }

    /**
     * Writes default header data to the log file at the start of the program
     */
    public static void writeHeaders() {
        LocalDateTime time = LocalDateTime.now();
        int m = time.get(ChronoField.MONTH_OF_YEAR);
        int d = time.get(ChronoField.DAY_OF_MONTH);
        int y = time.get(ChronoField.YEAR);
        logMessage("Current Date is: " + m + "/" + d + "/" + y);
    }

}