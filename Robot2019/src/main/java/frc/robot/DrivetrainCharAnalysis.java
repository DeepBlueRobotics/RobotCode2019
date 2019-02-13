package frc.robot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVFormat;

public class DrivetrainCharAnalysis {
    public static void main(String[] args) {
        String file1 = "/home/lvuser/drive_char_linear.csv";
        String file2 = "/home/lvuser/drive_char_stepwise.csv";
        String outfile = "/home/lvuser/drive_char_params.csv";
        //System.out.println("Simple Regression: ");
        //simpleRegression(file1, file2, outfile);
        System.out.println("Ordinary Least Squares: ");
        ordinaryLeastSquares(file1, file2, outfile);
    }

    // Ordinary Least Squares approach (multi-variable approach)
    public static void ordinaryLeastSquares(String file1, String file2, String outfile) {
        double kv = 0.0;
        double ka = 0.0;
        double voltageIntercept = 0.0;
        int spread = 30;

        double[][] returns;
        double[] params, velocities, voltages, accelerations;

        returns = parseCSV(file1, spread);
        velocities = returns[0];
        voltages = returns[1];
        accelerations = returns[2];

        returns = parseCSV(file2, spread);
        velocities = ArrayUtils.addAll(velocities, returns[0]);
        voltages = ArrayUtils.addAll(voltages, returns[1]);
        accelerations = ArrayUtils.addAll(accelerations, returns[2]);

        double[][] xs = new double[voltages.length][2];
        double[] ys = new double[voltages.length];
        for (int i = 0; i < voltages.length; i++) {
            xs[i][0] = velocities[i];
            xs[i][1] = accelerations[i];
            ys[i] = voltages[i];
        }

        OLSMultipleLinearRegression algorithm = new OLSMultipleLinearRegression();
        algorithm.newSampleData(ys, xs);
        params = algorithm.estimateRegressionParameters();
        // System.out.println(params.length);
        kv = params[1];
        ka = params[2];
        voltageIntercept = params[0];

        System.out.print("Velocity Constant is " + Double.toString(12 * kv) /* Change inches to feet in printout */);
        System.out.print(" and the Acceleration Constant is " + Double.toString(12 * ka));
        System.out.print(" with a voltage intercept of " + Double.toString(voltageIntercept) + "\n");

        FileWriter f;
        try {
            f = new FileWriter(new File(outfile));
            f.append(kv + "," + ka + "," + voltageIntercept);
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Simplistic approach to the problem.
    public static void simpleRegression(String file1, String file2, String outfile) {
        // Argument 1 should be the filepath of the linear increase CSV file.
        // Argument 2 should be the filepath of the stepwise increase CSV file.
        double kv = 0.0; // The parameter for velocity in the drivetrain characterization formula
        double ka = 0.0; // The parameter for acceleration in the drivetrain characterization formula
        double voltageIntercept = 0.0;
        int spread = 18; // How much data should our difference quotient cover?

        double[][] returns;
        double[] params, velocities, voltages, accelerations, stepwise_x;

        returns = parseCSV(file1, spread);
        velocities = returns[0];
        voltages = returns[1];
        accelerations = returns[2];

        // System.out.println(linearVelocities);
        params = simpleRegressionFormula(voltages, velocities);
        kv = 1 / params[1]; // Voltseconds / inches
        voltageIntercept = -params[0] / params[1]; // Think of this as -(b/m) in y = mx + b

        returns = parseCSV(file2, spread);
        velocities = returns[0];
        voltages = returns[1];
        accelerations = returns[2];
        stepwise_x = velocities;    // To define the size of stepwise_x

        for (int i = 0; i < velocities.length; i++) {
            stepwise_x[i] = voltages[i] - (kv * velocities[i] + voltageIntercept);
        }

        params = simpleRegressionFormula(stepwise_x, accelerations);
        ka = 1 / params[1]; // Volt * (seconds^2) / inches.

        System.out.print("Velocity Constant is " + Double.toString(12 * kv) /* Change inches to feet in printout */);
        System.out.print(" and the Acceleration Constant is " + Double.toString(12 * ka));
        System.out.print(" with a voltage intercept of " + Double.toString(voltageIntercept) + "\n");

        FileWriter f;
        try {
            f = new FileWriter(new File(outfile));
            f.append(kv + "," + ka + "," + voltageIntercept);
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double[] simpleRegressionFormula(double[] xs, double[] ys) {
        double sx, sxx, sy, sxy, a, b;
        sx = sxx = sy = sxy = a = b = 0.0;
        int n = xs.length;

        for (int i = 0; i < n; i++) {
            sx += xs[i];
            sxx += xs[i] * xs[i];
            sxy += xs[i] * ys[i];
            sy += ys[i];
        }

        b = (n * sxy - sx * sy) / (n * sxx - sx * sx);
        a = (sy - b * sx) / n;

        double[] params = { a, b };
        return params;
    }

    public static double[][] parseCSV(String filename, int spread) {
        Reader in = new FileReader(filename);
        Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(in);

        double[] leftVelocities, rightVelocities, velocities, voltages, accelerations = new double[records.size() - spread];

        for (CSVRecord record : records) {
            if (!record.get(0).equals("Timestamp (s)")) {
                double v1 = Double.valueOf(record.get(2));
                double v2 = Double.valueOf(record.get(3));
                double v3 = 0.5 * (v1 - v2);
                int n = (int) record.getRecordNumber();
                velocities[n] = v3;
                voltages[n] = Double.valueOf(record.get(1));

                if (leftVelocities.length >= spread) {
                    double a1 = (v1 - leftVelocities[leftVelocities.length - spread]) / (spread * 0.02);
                    double a2 = (v2 - rightVelocities[rightVelocities.length - spread]) / (spread * 0.02); // right
                                                                                                                // velocity
                    accelerations[n] = Math.abs(a1) + Math.abs(a2);
                }

                if (leftVelocities.length < records.size() - spread) {
                    leftVelocities[n] = v1;
                    rightVelocities[n] = v2;
                }
            }
        }

        double[][] returns = {velocities, voltages, accelerations};
        return returns;
    }
}