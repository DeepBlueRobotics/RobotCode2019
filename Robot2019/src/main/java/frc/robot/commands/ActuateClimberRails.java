package frc.robot.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.subsystems.Climber;

public class ActuateClimberRails extends InstantCommand {
  private Climber climber;

  public ActuateClimberRails(Climber cl) {
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