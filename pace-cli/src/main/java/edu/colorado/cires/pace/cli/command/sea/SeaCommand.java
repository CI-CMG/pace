package edu.colorado.cires.pace.cli.command.sea;

import edu.colorado.cires.pace.cli.command.common.BaseCommand;
import edu.colorado.cires.pace.data.object.Sea;
import picocli.CommandLine.Command;

@Command(name = "sea", description = "Manage seas")
public class SeaCommand extends BaseCommand<Sea> {

  SeaCommand() {
    super(Sea.class, SeaRepositoryFactory::createRepository);
  }

  @Override
  public void run() {
    
  }
}
