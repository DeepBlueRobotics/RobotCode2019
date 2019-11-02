package frc.robot.logging;

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

    static {
        booleanFunctions = new HashMap<>();
        integerFunctions = new HashMap<>();
        doubleFunctions = new HashMap<>();
    }

    public static void logMessage(String message) {
        LoggerMessageEncoder.logMessage(message);;
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
        ArrayList<Var<?>> vars = new ArrayList<>();
        vars.addAll(getVars(booleanFunctions, Var::create));
        vars.addAll(getVars(integerFunctions, Var::create));
        vars.addAll(getVars(doubleFunctions, Var::create));
        LoggerMessageEncoder.logVars(vars);
    }

    private static <T> ArrayList<Var<T>> getVars(HashMap<String, Supplier<T>> map, BiFunction<String, T, Var<T>> creationFunction) {
        ArrayList<Var<T>> out = new ArrayList<>();
        for(String id: map.keySet()) {
            out.add(creationFunction.apply(id, map.get(id).get()));
        }
        return out;
    }

}