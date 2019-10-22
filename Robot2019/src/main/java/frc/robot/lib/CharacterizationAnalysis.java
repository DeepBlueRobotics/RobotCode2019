package frc.robot.lib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

public class CharacterizationAnalysis {
    // Should gather values and perform analysis
    public static void characterize(String[] inFiles, String outFile) {
        // Gather values and run analysis
        double[][] data1, data2;
        double[] voltages, firstVelocities, secondVelocities, firstAccelerations, secondAccelerations;
        double[] params;
        FileWriter f;
        try {
            f = new FileWriter(new File(outFile), true);

            // Forward Characterization
            data1 = parseCSV(inFiles[0]);
            data2 = parseCSV(inFiles[1]);
            voltages = mergeArray(data1[0], data2[0]);
            firstVelocities = mergeArray(data1[1], data2[1]);
            firstAccelerations = mergeArray(data1[2], data2[2]);
            params = ordinaryLeastSquares(voltages, firstVelocities, firstAccelerations);
            f.append(params[0] + "," + params[1] + "," + params[2] + "\r\n");
            if (data1.length == 5) {
                secondVelocities = mergeArray(data1[3], data2[3]);
                secondAccelerations = mergeArray(data1[4], data2[4]);
                params = ordinaryLeastSquares(voltages, secondVelocities, secondAccelerations);
                f.append(params[0] + "," + params[1] + "," + params[2] + "\r\n");
            }

            // Backward Characterization
            data1 = parseCSV(inFiles[3]);
            data2 = parseCSV(inFiles[4]);
            voltages = mergeArray(data1[0], data2[0]);
            firstVelocities = mergeArray(data1[1], data2[1]);
            firstAccelerations = mergeArray(data1[2], data2[2]);
            params = ordinaryLeastSquares(voltages, firstVelocities, firstAccelerations);
            f.append(params[0] + "," + params[1] + "," + params[2] + "\r\n");
            if (data1.length == 5) {
                secondVelocities = mergeArray(data1[3], data2[3]);
                secondAccelerations = mergeArray(data1[4], data2[4]);
                params = ordinaryLeastSquares(voltages, secondVelocities, secondAccelerations);
                f.append(params[0] + "," + params[1] + "," + params[2] + "\r\n");
            }

            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static double[] ordinaryLeastSquares(double[] voltages, double[] velocities, double[] accelerations) {
        /* 
        Parameters:
            voltages - A list of the current supplied voltage to the motor controllers.
            velocities - A matrix of current velocities (rows, columns) = (# samples, 2)
            accelerations - A matrix of calculated acclerations (rows, columns) = (# samples, 2)
        Returns:
            params - an array consisting of kV, kA, and voltageIntercept values for charDrive()
        */
        double[] params = new double[6];
        double[] returns;

        // Gather parameters for the motors.
        double[][] xs = new double[voltages.length][2];
        double[] ys = new double[voltages.length];
        for (int i = 0; i < voltages.length; i++) {
            xs[i][0] = velocities[i];
            xs[i][1] = accelerations[i];
            ys[i] = voltages[i];
        }

        // Calculate drivetrain values.
        OLSMultipleLinearRegression algorithm = new OLSMultipleLinearRegression();
        algorithm.newSampleData(ys, xs);
        returns = algorithm.estimateRegressionParameters();
        params[0] = returns[1];
        params[1] = returns[2];
        params[2] = returns[0];
        return params;
    }
    public static double[][] parseCSV(String filename) {
        int spread = 30;
        try {
            Reader in = new FileReader(filename);
            CSVParser csvParser = new CSVParser(in, CSVFormat.DEFAULT);

            List<CSVRecord> records = csvParser.getRecords();
            int numValues = records.size();
            int numEntries = records.get(0).size();
            int arrLen;
            if (numEntries == 4) {
                arrLen = 5;
            } else {
                arrLen = 3;
            }
    
            double[][] returns = new double[numValues][arrLen];
            double vel1, vel2, a1, a2, volt;
            int i = 0;
            int j = 0;
            for (int n = 0; n < numValues; n++) {
                CSVRecord record = records.get(n);
                if (!record.get(0).equals("Timestamp (s)")) {
                    volt = Double.valueOf(record.get(1));
                    vel1 = Math.abs(Double.valueOf(record.get(2)));
                    if (i >= spread) {
                        a1 = (vel1 - returns[i - spread][1]) / (spread * 0.02);
                        returns[n][2] = a1;
                    }
                    if (i < numValues - spread) {
                        returns[n][0] = volt;
                        returns[n][1] = vel1;
                        i += 1;
                    }
                    if (numEntries == 4) {
                        vel2 = Math.abs(Double.valueOf(record.get(3)));
                        if (j >= spread) {
                            a2 = (vel2 - returns[i - spread][3]) / (spread * 0.02);
                            returns[n][4] = a2;
                        }
                        if (j < numValues - spread) {
                            returns[n][3] = vel2;
                            j += 1;
                        }
                    }
                }
            }
            csvParser.close();
            return returns;
        } catch (FileNotFoundException fnf) {
            fnf.printStackTrace();
            return null;
        } catch (IOException io) {
            io.printStackTrace();
            return null;
        }
    }
    public static double[] mergeArray(double[] arr1, double[] arr2) {
        int n = arr1.length;
        int m = arr2.length;
        double[] merged = new double[n + m];
        for (int i = 0; i < n; i++) {
            merged[i] = arr1[i];
        }
        for (int i = 0; i < m; i++) {
            merged[n + i] = arr2[i];
        }
        return merged;
    }
}