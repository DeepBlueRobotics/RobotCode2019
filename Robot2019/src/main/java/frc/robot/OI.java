/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.ArrayList;
import java.util.Collection;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
  public Joystick leftJoy = new Joystick(getPort("LeftJoystick"));
  public Joystick rightJoy = new Joystick(getPort("RightJoystick"));
  public ArrayList<Joystick> joysticks = new ArrayList<Joystick>() {{ add(leftJoy); add(rightJoy); }};

  private int getPort(String name) {
    int port;
    port = (int)(SmartDashboard.getNumber("Port/" + name, -1));
    
    if (port == -1) {
      System.out.println("Port name" + name + "is not defined in SmartDashboard.");
      return -1;
    } else {
      return port;
    }
  }
}
