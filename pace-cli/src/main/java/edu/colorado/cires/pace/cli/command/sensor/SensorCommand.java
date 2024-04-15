package edu.colorado.cires.pace.cli.command.sensor;

import edu.colorado.cires.pace.cli.command.common.BaseCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.object.Sensor;
import picocli.CommandLine.Command;

@Command(name = "sensor", description = "Manage sensors", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
public class SensorCommand extends BaseCommand<Sensor> {

  protected SensorCommand() {
    super(Sensor.class, SensorRepositoryFactory::createRepository);
  }

  @Override
  public void run() {
    
  }
}
