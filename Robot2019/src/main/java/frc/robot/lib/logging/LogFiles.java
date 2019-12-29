package frc.robot.lib.logging;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import edu.wpi.first.wpilibj.DriverStation;

/**
 * Creates the required log files and initializes logging code
 */
final class LogFiles {
    
    private static int logId;
    private static ArrayList<Integer> existingLogIds;
    private static String dirString = System.getProperty("user.home") + "/logs";
    private static File dirFile, infoFile, infoBackupFile;

    /**
     * Initializes the logging code or returns if it is already initialized
     * @param dataFormat The {@link CSVFormat} to use when logging data
     */
    static void init(CSVFormat dataFormat) {
        if(GlobalLogInfo.isInit()) {
            return;
        }
        dirFile = new File(dirString);
        try {
            dirFile.createNewFile();
        } catch(IOException e) {
            LogUtils.handleLoggingApiDisableError("creating log directory", e);
            return;
        }
        infoFile = new File(dirString + "/info.txt");
        infoBackupFile = new File(dirString + "/infobkup.txt");
        try {
            infoFile.createNewFile();
            infoBackupFile.createNewFile();
        } catch(IOException e) {
            LogUtils.handleLoggingApiDisableError("creating info file", e);
            return;
        }
        existingLogIds = new ArrayList<>();
        try {
            backupInfoFile();
            findExistingLogIds();
            clearOldLogs();
            findLogId();
            createLogFiles(dataFormat);
        } catch(AbortException e) {
            LogUtils.handleLoggingApiDisableError(e.getMessage(), (Exception)e.getCause());
            return;
        }
    }

    private static void backupInfoFile() throws AbortException {
        try {
            Files.copy(infoFile.toPath(), infoBackupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch(IOException e) {
            throw new AbortException("copying info file", e);
        }
    }

    private static void findExistingLogIds() {
        existingLogIds.clear();
        File[] files = dirFile.listFiles();
        for(File file: files) {
            String name = file.getName();
            if(name.indexOf(".") == -1) {
                continue;
            }
            String idString = file.getName().substring(0, name.indexOf("."));
            try {
                int id = Integer.parseInt(idString);
                if(!existingLogIds.contains(id)) {
                    existingLogIds.add(id);
                }
            } catch(NumberFormatException e) {
                continue;
            }
        }
    }

    private static void clearOldLogs() throws AbortException {
        int lines = 0;
        ArrayList<String> linesD = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(infoFile))) {
            String line;
            while((line = br.readLine()) != null) {
                try {
                    int id = Integer.parseInt(line.substring(0, line.indexOf(" ")));
                    if(!existingLogIds.contains(id)) {
                        continue;
                    }
                    linesD.add(line);
                    lines++;
                } catch(Exception e) {}
            }
        } catch(IOException e) {
            throw new AbortException("reading info file", e);
        }
        int endIdx = lines - 100;
        if(lines > 100) {
            try(BufferedWriter bw = new BufferedWriter(new FileWriter(infoFile))) {
                for(int i = endIdx+1; i < lines; i++) {
                    bw.append(linesD.get(i));
                    bw.newLine();
                }
            } catch(IOException e) {
                throw new AbortException("writing info file", e);
            }
            for(int i = 0; i < endIdx+1; i++) {
                try {
                    int id = Integer.parseInt(linesD.get(i).substring(0, linesD.get(i).indexOf(" ")));
                    new File(dirString + "/" + id + ".txt").delete();
                    new File(dirString + "/" + id + ".csv").delete();
                    existingLogIds.remove(id);
                } catch(Exception e) {}
            }
        }
    }

    private static void findLogId() {
        for(int id = 0; id < Integer.MAX_VALUE; id++) {
            if(!existingLogIds.contains(id)) {
                logId = id;
                return;
            }
        }
        throw new IllegalStateException("No avaliable log id. If this error occures please verify your instalation as this should not be possible.");
    }

    private static void createLogFiles(CSVFormat dataFormat) throws AbortException {
        try {
            File txtFile = new File(dirString + "/" + logId + ".txt");
            File csvFile = new File(dirString + "/" + logId + ".csv");
            txtFile.createNewFile();
            csvFile.createNewFile();
            try(BufferedWriter bw = new BufferedWriter(new FileWriter(infoFile, true))) {
                bw.append(logId + " ");
                bw.append(getLogTitle());
                bw.newLine();
            }
            GlobalLogInfo.init(txtFile, csvFile, new CSVPrinter(new FileWriter(csvFile), dataFormat));
        } catch(IOException e) {
            throw new AbortException("creating log files", e);
        }
    }

    private static String getLogTitle() {
        DriverStation ds = DriverStation.getInstance();
        DriverStation.MatchType type = ds.getMatchType();
        String time = LocalDateTime.now().format(GlobalLogInfo.dateTimeFormat);
        if(type == DriverStation.MatchType.None) {
            return time;
        }
        return time + " " + type.toString() + " " + ds.getMatchNumber() + "-" + ds.getReplayNumber();
    }

    private LogFiles() {}

    private static final class AbortException extends Exception {

        private static final long serialVersionUID = 6982175248059420129L;

        public AbortException(String message, Exception cause) {
            super(message, cause);
        }

    }

}