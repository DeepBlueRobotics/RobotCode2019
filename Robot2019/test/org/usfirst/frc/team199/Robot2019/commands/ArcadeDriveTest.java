package org.usfirst.frc.team199.Robot2018.commands;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import static org.mockito.Mockito.*;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.robot.commands.TeleopDrive;
import frc.robot.subsystems.Drivetrain;

class ArcadeDriveTest {
    public test() {
        Joystick leftJoy = new Joystick(0);
        Joystick rightJoy = new Joystick(1);

        WPI_TalonSRX leftTalon = new WPI_TalonSRX(0);
        WPI_VictorSPX leftVictor1 = new WPI_VictorSPX(1);
        WPI_VictorSPX leftVictor2 = new WPI_VictorSPX(2);
        
        WPI_TalonSRX rightTalon = new WPI_TalonSRX(3);
        WPI_VictorSPX rightVictor1 = new WPI_VictorSPX(4);
        WPI_VictorSPX rightVictor2 = new WPI_VictorSPX(5);

        Drivetrain dt = Drivetrain(leftTalon, leftVictor1, leftVictor2, rightTalon, rightVictor1, rightVictor2, leftJoy, rightJoy, null, null, null);
        Command teledriveMock = mock(TeleopDrive);
        Command teledrive = new TeleopDrive(dt);

        when().thenReturn();
    }
}