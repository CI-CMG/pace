package edu.colorado.cires.pace.cli.command.soundSource;

import edu.colorado.cires.pace.cli.command.common.BaseCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.object.SoundSource;
import picocli.CommandLine.Command;

@Command(name = "sound-source", description = "Manage sound sources", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
public class SoundSourceCommand extends BaseCommand<SoundSource> {

  SoundSourceCommand() {
    super(SoundSource.class, SoundSourceRepositoryFactory::createRepository);
  }

  @Override
  public void run() {
    
  }
}
