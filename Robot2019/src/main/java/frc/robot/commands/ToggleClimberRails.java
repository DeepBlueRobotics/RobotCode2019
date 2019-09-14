package frc.robot.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.subsystems.Climber;

public class ToggleClimberRails extends InstantCommand {
  private Climber climber;

  public ToggleClimberRails(Climber cl) {
    super();
    requires(cl);
    climber = cl;
  }

  // Called once when the command executes
  @Override
  protected void initialize() {
    climber.toggleRails();
  }
}