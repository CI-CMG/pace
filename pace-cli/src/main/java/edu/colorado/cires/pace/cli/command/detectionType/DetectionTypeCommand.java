package edu.colorado.cires.pace.cli.command.detectionType;

import edu.colorado.cires.pace.cli.command.common.BaseCommand;
import edu.colorado.cires.pace.data.object.DetectionType;
import picocli.CommandLine.Command;

@Command(name = "detection-type", description = "Manage detection types")
public class DetectionTypeCommand extends BaseCommand<DetectionType> {

  DetectionTypeCommand() {
    super(DetectionType.class, DetectionTypeRepositoryFactory::createRepository);
  }

  @Override
  public void run() {
    
  }
}
