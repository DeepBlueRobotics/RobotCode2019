package frc.robot.logging;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import frc.robot.logging.vars.Var;

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

    public static int getWaitStates() {
        return waitStates;
    }

    public static void setWaitStates(int waitStates) {
        RobotLogger.waitStates = waitStates < 0 ? 0 : waitStates;
    }

    public static int getCurrentState() {
        return currentState;
    }

    public static void nextState() {
        if(currentState == waitStates) {
            currentState = 0;
        } else {
            currentState++;
        }
    }

    public static void init() {
        LoggerInterface.init();
    }

    public static void logMessage(String message) {
        LoggerMessageEncoder.logMessage(message);;
    }

    public static void logErrorMessage(String message) {
        LoggerMessageEncoder.logErrorMessage(message);;
    }

    public static void registerVariableMapping(String id, String name) {
        LoggerMessageEncoder.mapId(id, name);
    }

    public static void addBooleanVar(String id, Supplier<Boolean> function) {
        booleanFunctions.put(id, function);
    }

    public static void addIntegerVar(String id, Supplier<Integer> function) {
        integerFunctions.put(id, function);
    }

    public static void addDoubleVar(String id, Supplier<Double> function) {
        doubleFunctions.put(id, function);
    }

    public static void removeBooleanVar(String id) {
        booleanFunctions.remove(id);
    }

    public static void removeIntegerVar(String id) {
        integerFunctions.remove(id);
    }

    public static void removeDoubleVar(String id) {
        doubleFunctions.remove(id);
    }

    public static HashSet<String> getBooleanKeys() {
        return new HashSet<String>(booleanFunctions.keySet());
    }

    public static HashSet<String> getIntegerKeys() {
        return new HashSet<String>(integerFunctions.keySet());
    }

    public static HashSet<String> getDoubleKeys() {
        return new HashSet<String>(doubleFunctions.keySet());
    }

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

    public static void logMode(Mode mode) {
        LoggerMessageEncoder.logMode(mode);
    }

    public static void writeHeaders() {
        LocalDateTime time = LocalDateTime.now();
        int m = time.get(ChronoField.MONTH_OF_YEAR);
        int d = time.get(ChronoField.DAY_OF_MONTH);
        int y = time.get(ChronoField.YEAR);
        logMessage("Current Date is: " + m + "/" + d + "/" + y);
    }

}