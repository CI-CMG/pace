package edu.colorado.cires.pace.cli.command.sea;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.command.common.BaseCommand;
import edu.colorado.cires.pace.data.object.Sea;
import java.util.List;
import picocli.CommandLine.Command;

@Command(name = "sea", description = "Manage seas")
public class SeaCommand extends BaseCommand<Sea> {

  SeaCommand() {
    super(Sea.class, SeaRepositoryFactory::createRepository, new TypeReference<>() {});
  }

  @Override
  public void run() {
    
  }
}
