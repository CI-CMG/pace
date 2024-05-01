package edu.colorado.cires.pace.cli.command.platform;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.command.common.BaseCommand;
import edu.colorado.cires.pace.data.object.Platform;
import picocli.CommandLine.Command;

@Command(name = "platform", description = "Manage platforms")
public class PlatformCommand extends BaseCommand<Platform> {

  PlatformCommand() {
    super(Platform.class, PlatformRepositoryFactory::createJsonRepository, new TypeReference<>() {});
  }

  @Override
  public void run() {
    
  }
}
