package frc.robot.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.subsystems.ClimberSubsystem;

public class ActuateClimberRails extends InstantCommand {
  private ClimberSubsystem climber;

  public ActuateClimberRails(ClimberSubsystem cl) {
    super();
    requires(cl);

    climber = cl;
  }

  // Called once when the command executes
  @Override
  protected void initialize() {
    climber.actuateRails();
  }
}