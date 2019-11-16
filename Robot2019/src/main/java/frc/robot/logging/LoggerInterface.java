package frc.robot.logging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class LoggerInterface {

    public static final int backlog;
    public static final boolean useSingleStream;
    private static boolean init = false;
    private static String dirPath;
    private static File dir;
    private static File file;
    private static ArrayList<Integer> otherLogs = new ArrayList<>();
    private static int id;
    private static boolean disabled = false;
    private static PrintStream stream;

    static {
        backlog = 100;
        useSingleStream = true;
    }

    public static boolean isDisabled() {
        return disabled;
    }

    public static void init() throws IllegalStateException {
        try {
            if(init) {
                throw new IllegalStateException("Already Initialized");
            }
            String home = System.getProperty("user.home");
            dirPath = home + "/logs";
            dir = new File(dirPath);
            dir.mkdir();
            getFile();
            file.createNewFile();
            clearOldLogs();
            if(useSingleStream) {
                stream = getStreamN();
            }
        } catch(IOException e) {
            System.err.println("Error Occured Initializing Logger!"+
                " Logger will be disabled for the deration of this run.");
            e.printStackTrace(System.err);
            disabled = true;
        }
    }

    private static void getFile() {
        File[] existingLogs = dir.listFiles();
        id = 0;
        for(File log: existingLogs) {
            try {
                String name = log.getName();
                otherLogs.add(Integer.parseInt(name.substring(3, name.length()-7)));
            } catch(Exception e) {

            }
        }
        otherLogs.sort(Integer::compare);
        if(!otherLogs.isEmpty()) {
            id = otherLogs.get(otherLogs.size()-1)+1;
        } else {
            id = 0;
        }
        file = new File(dirPath + "/Log" + id + ".riolog");
    }

    private static void clearOldLogs() {
        int threshold = id-backlog;
        for(Integer logId: otherLogs) {
            if(logId < threshold) {
                new File(dir + "/Log" + logId + ".riolog").delete();
            }
        }
    }

    public static PrintStream getStream() {
        if(useSingleStream) {
            return stream;
        }
        return getStreamN();
    }

    private static PrintStream getStreamN() {
        try {
            return new PrintStream(new FileOutputStream(file, true));
        } catch(IOException e) {
            reportIOError("Getting Stream", e);
            return new PrintStream(new OutputStream(){ @Override
                public void write(int arg0) throws IOException {}});
        }
    }

    public static void reportIOError(String causeMessage, Throwable cause) {
        System.err.println("Error Occured " + causeMessage);
        cause.printStackTrace(System.err);
    }

    public static void closeStream(PrintStream stream) {
        if(!useSingleStream) {
            stream.close();
        }
    }

    private LoggerInterface() {}

}