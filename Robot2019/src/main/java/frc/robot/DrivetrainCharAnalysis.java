package frc.robot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

public class DrivetrainCharAnalysis {
    public static void main(String[] args) {
        String file1 = "/home/lvuser/drive_char_linear.csv";
        String file2 = "/home/lvuser/drive_char_stepwise.csv";
        String outfile = "/home/lvuser/drive_char_params.csv";
        System.out.println("Simple Regression: ");
        simpleRegression(file1, file2, outfile);
        System.out.println("Ordinary Least Squares: ");
        ordinaryLeastSquares(file1, file2, outfile);
    }

    // Ordinary Least Squares approach (multi-variable approach)
    public static void ordinaryLeastSquares(String file1, String file2, String outfile) {
        double appliedVoltage = 6.0;
        double kv = 0.0;
        double ka = 0.0;
        double voltageIntercept = 0.0;
        int spread = 30;

        ArrayList<Double> voltages = new ArrayList<Double>();
        ArrayList<Double> velocities = new ArrayList<Double>();
        ArrayList<Double> accelerations = new ArrayList<Double>();
        accelerations.add(0.0);

        ArrayList<Double> leftVelocities = new ArrayList<Double>();
        ArrayList<Double> rightVelocities = new ArrayList<Double>();
        try {
            Scanner filereader1 = new Scanner(new File(file1));
            while (filereader1.hasNext()) {
                String line1 = filereader1.nextLine();
                // If the line is no the first line
                if (!(line1.equals(
                        "Timestamp (s),Voltage (V),LeftMotorVelocity (inches / s),RightMotorVelocity (inches / s)"))) {
                    double v1 = Double.valueOf(line1.split(",")[2]);
                    double v2 = Double.valueOf(line1.split(",")[3]);
                    double v3 = 0.5 * (v1 - v2);
                    voltages.add(Double.valueOf(line1.split(",")[1])); // Append voltage
                    velocities.add(v3); // Append average of the left and right motor velocities

                    if (leftVelocities.size() >= spread) {
                        double a1 = (v1 - leftVelocities.get(leftVelocities.size() - spread)) / (spread * 0.02);
                        double a2 = (v2 - rightVelocities.get(rightVelocities.size() - spread)) / (spread * 0.02); // right
                                                                                                                   // velocity
                        accelerations.add((Math.abs(a1) + Math.abs(a2)));
                    }

                    leftVelocities.add(v1);
                    rightVelocities.add(v2);
                }
            }
            filereader1.close();
        } catch (FileNotFoundException e) {
            System.out.println("The file being referenced may not exist. Error: " + e.toString());
        }

        int amtToRemove = velocities.size() - accelerations.size();
        for (int i = 0; i < amtToRemove; i++) {
            velocities.remove(velocities.size() - 1);
            voltages.remove(voltages.size() - 1);
        }

        // Do the same thing for filereader2 as filereader1
        int previousVelSize = leftVelocities.size();
        try {
            Scanner filereader2 = new Scanner(new File(file2));
            while (filereader2.hasNext()) {
                String line2 = filereader2.nextLine();
                if (!(line2.equals(
                        "Timestamp (s),Voltage (V),LeftMotorVelocity (inches / s),RightMotorVelocity (inches / s)"))) {
                    double v1 = Double.valueOf(line2.split(",")[2]);
                    double v2 = Double.valueOf(line2.split(",")[3]);
                    double v3 = 0.5 * (v1 - v2);
                    velocities.add(v3);

                    if ((leftVelocities.size() - previousVelSize) >= spread) {
                        double a1 = (v1 - leftVelocities.get(leftVelocities.size() - spread)) / (spread * 0.02);
                        double a2 = (v2 - rightVelocities.get(rightVelocities.size() - spread)) / (spread * 0.02);
                        accelerations.add((Math.abs(a1) + Math.abs(a2)));
                    }

                    leftVelocities.add(v1);
                    rightVelocities.add(v2);
                    voltages.add(appliedVoltage);
                }
            }
            filereader2.close();
        } catch (FileNotFoundException e) {
            System.out.println("The file being referenced may not exist. Error: " + e.toString());
        }

        amtToRemove = velocities.size() - accelerations.size();
        for (int i = 0; i < amtToRemove; i++) {
            velocities.remove(velocities.size() - 1);
            voltages.remove(voltages.size() - 1);
        }

        // Merge all the data into 2d-array data.
        double[][] xs = new double[voltages.size()][2];
        double[] ys = new double[voltages.size()];
        for (int i = 0; i < voltages.size(); i++) {
            xs[i][0] = velocities.get(i);
            xs[i][1] = accelerations.get(i);
            ys[i] = voltages.get(i);
        }

        // Remove unnecessary arraylists.
        voltages = null;
        velocities = null;
        accelerations = null;
        leftVelocities = null;
        rightVelocities = null;
        System.gc(); // Force the garbage collector to run.

        OLSMultipleLinearRegression algorithm = new OLSMultipleLinearRegression();
        algorithm.newSampleData(ys, xs);
        double[] params = algorithm.estimateRegressionParameters();
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
        double appliedVoltage = 6.0; // The voltage expected to be applied during the Stepwise Testing.
        double kv = 0.0; // The parameter for velocity in the drivetrain characterization formula
        double ka = 0.0; // The parameter for acceleration in the drivetrain characterization formula
        double voltageIntercept = 0.0;
        int spread = 18; // How much data should our difference quotient cover?

        ArrayList<Double> linearVelocities = new ArrayList<Double>();
        ArrayList<Double> linearVoltages = new ArrayList<Double>();
        ArrayList<Double> stepwise_x = new ArrayList<Double>();
        ArrayList<Double> stepwise_acceleration = new ArrayList<Double>();
        double[] params = new double[2];

        try {
            Scanner filereader1 = new Scanner(new File(file1));
            while (filereader1.hasNext()) {
                String line1 = filereader1.nextLine();
                // If the line is not the first line
                if (!(line1.equals(
                        "Timestamp (s),Voltage (V),LeftMotorVelocity (inches / s),RightMotorVelocity (inches / s)"))) {
                    linearVoltages.add(Double.valueOf(line1.split(",")[1])); // Append voltage
                    // Append the average of the left and right velocities.
                    linearVelocities
                            .add(0.5 * (Double.valueOf(line1.split(",")[2]) - Double.valueOf(line1.split(",")[3])));
                }
            }
            filereader1.close();
        } catch (FileNotFoundException e) {
            System.out.println("The file being referenced may not exist. Error: " + e.toString());
        }

        /*
         * double[] a =
         * {1.47,1.50,1.52,1.55,1.57,1.60,1.63,1.65,1.68,1.70,1.73,1.75,1.78,1.80,1.83};
         * double[] b =
         * {52.21,53.12,54.48,55.84,57.20,58.57,59.93,61.29,63.11,64.47,66.28,68.10,69.
         * 92,72.19,74.46}; ArrayList<Double> A = new ArrayList<Double>(a.length);
         * ArrayList<Double> B = new ArrayList<Double>(a.length); for(int i = 0; i <
         * a.length; i++) { A.add(a[i]); B.add(b[i]); }
         * 
         * System.out.println(DrivetrainCharacterization.simpleRegression(A, B)[0]);
         */

        // System.out.println(linearVelocities);
        params = simpleRegressionFormula(linearVoltages, linearVelocities);
        kv = 1 / params[1]; // Voltseconds / inches
        voltageIntercept = -params[0] / params[1]; // Think of this as -(b/m) in y = mx + b

        try {
            Scanner filereader2 = new Scanner(new File(file2));
            ArrayList<Double> leftVelocities = new ArrayList<Double>();
            ArrayList<Double> rightVelocities = new ArrayList<Double>();
            while (filereader2.hasNext()) {
                String line2 = filereader2.nextLine();
                if (!(line2.equals(
                        "Timestamp (s),Voltage (V),LeftMotorVelocity (inches / s),RightMotorVelocity (inches / s)"))) {
                    double v1 = Double.valueOf(line2.split(",")[2]);
                    double v2 = Double.valueOf(line2.split(",")[3]);
                    double v3 = 0.5 * (v1 - v2);
                    // System.out.println(v1 + "," + v2);
                    // System.out.println(kv * v3 + voltageIntercept);
                    stepwise_x.add(appliedVoltage - (kv * v3 + voltageIntercept));

                    if (leftVelocities.size() >= spread) {
                        double a1 = (v1 - leftVelocities.get(leftVelocities.size() - spread)) / (spread * 0.02);
                        double a2 = (v2 - rightVelocities.get(rightVelocities.size() - spread)) / (spread * 0.02);
                        // System.out.println(a1 + "," + a2);
                        stepwise_acceleration.add((Math.abs(a1) + Math.abs(a2)));
                    }

                    leftVelocities.add(v1);
                    rightVelocities.add(v2);
                }
            }
            filereader2.close();

            // Remove unnecessary arraylists.
            leftVelocities = null;
            rightVelocities = null;
            System.gc(); // Force the garbage collector to run.

        } catch (FileNotFoundException e) {
            System.out.println("The file being referenced may not exist. Error: " + e.toString());
        }

        for (int i = 0; i < spread; i++) {
            stepwise_x.remove(stepwise_x.size() - 1);
        }

        // System.out.println(stepwise_x);
        // System.out.println(stepwise_acceleration);
        params = simpleRegressionFormula(stepwise_x, stepwise_acceleration);
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

    public static double[] simpleRegressionFormula(ArrayList<Double> xs, ArrayList<Double> ys) {
        double sx, sxx, sy, sxy, a, b;
        sx = sxx = sy = sxy = a = b = 0.0;
        int n = xs.size();

        for (int i = 0; i < n; i++) {
            sx += xs.get(i);
            sxx += xs.get(i) * xs.get(i);
            sxy += xs.get(i) * ys.get(i);
            sy += ys.get(i);
        }

        b = (n * sxy - sx * sy) / (n * sxx - sx * sx);
        a = (sy - b * sx) / n;

        double[] params = { a, b };
        return params;
    }
}