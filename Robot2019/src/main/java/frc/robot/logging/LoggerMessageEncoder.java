package frc.robot.logging;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.Collection;

import static frc.robot.logging.LoggerInterface.closeStream;
import frc.robot.logging.vars.Var;

public class LoggerMessageEncoder {
    
    public static final boolean allowMultiThreading;
    public static final String perR = "%0";
    public static final String mStr = "%1";
    public static final String mEnd = "%2";
    public static final String pSep = "%3";
    public static final String vMrk = "%4";
    public static final String mMrk = "%5";
    public static final String tMrk = "%6";

    static {
        allowMultiThreading = true;
    }

    public static void logMessage(String message) {
        logMessage(message, true);
    }

    public static void logMessage(String message, boolean includeTime) {
        thread(() -> {
            PrintStream ps = LoggerInterface.getStream();
            ps.print(mStr);
            if(includeTime) {
                String time = getTimestamp();
                ps.print(tMrk + time + pSep);
            }
            ps.print(format(message));
            ps.print(mEnd);
            ps.flush();
            closeStream(ps);
        });
    }

    public static void logVars(Var<?>... vars) {
        logVars(Arrays.asList(vars));
    }

    public static void logVars(Collection<Var<?>> vars) {
        thread(() -> {
            PrintStream ps = LoggerInterface.getStream();
            ps.print(mStr);
            ps.print(tMrk);
            ps.print(getTimestamp());
            ps.print(pSep);
            ps.print(vMrk);
            for(Var<?> var: vars) {
                ps.print(pSep);
                ps.print(format(var.type.tag));
                ps.print(pSep);
                ps.print(format(var.id));
                ps.print(pSep);
                ps.print(var.data.toString());
                ps.print(pSep);
            }
            ps.print(mEnd);
            ps.flush();
            closeStream(ps);
        });
    }

    public static void mapId(String id, String name) {
        thread(() -> {
            PrintStream ps = LoggerInterface.getStream();
            ps.print(mStr);
            ps.print(tMrk);
            ps.print(getTimestamp());
            ps.print(pSep);
            ps.print(mMrk);
            ps.print(format(id));
            ps.print(pSep);
            ps.print(format(name));
            ps.print(mEnd);
            ps.flush();
            closeStream(ps);
        });
    }

    public static String format(String message) {
        message = message.replaceAll("%", "%0");
        return message;
    }

    public static String getTimestamp() {
        LocalDateTime ldt = LocalDateTime.now();
        int hour = ldt.get(ChronoField.HOUR_OF_DAY);
        int minute = ldt.get(ChronoField.MINUTE_OF_HOUR);
        int second = ldt.get(ChronoField.SECOND_OF_MINUTE);
        int milisecond = ldt.get(ChronoField.MILLI_OF_SECOND);
        String c = ":";
        return hour + c + minute + c + second + "." + milisecond;
    }
    
    private static void thread(Runnable newThread) {
        if(allowMultiThreading) {
            new Thread(newThread).start();
        } else {
            newThread.run();
        }
    }

    private LoggerMessageEncoder() {}

}