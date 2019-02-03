/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class ToggleCamera extends InstantCommand {
  boolean toggle = false;

  UsbCamera driveCamera, hatchCamera;
  VideoSink cameraServer;

  /**
   * Toggles between the two cameras.
   */
  public ToggleCamera(UsbCamera driveCamera, UsbCamera hatchCamera, VideoSink cameraServer) {
    super();
    this.driveCamera = driveCamera;
    this.hatchCamera = hatchCamera;
    this.cameraServer = cameraServer;
  }

  // Called once when the command executes
  @Override
  protected void initialize() {
    if (toggle) {
      cameraServer.setSource(driveCamera);
    } else {
      cameraServer.setSource(hatchCamera);
    }
    toggle = !toggle;
  }

}
