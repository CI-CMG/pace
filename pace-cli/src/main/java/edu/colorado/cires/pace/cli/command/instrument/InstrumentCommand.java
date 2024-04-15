package edu.colorado.cires.pace.cli.command.instrument;

import edu.colorado.cires.pace.cli.command.common.BaseCommand;
import edu.colorado.cires.pace.data.object.Instrument;
import picocli.CommandLine.Command;

@Command(name = "instrument", description = "Manage instruments")
public class InstrumentCommand extends BaseCommand<Instrument> {

  InstrumentCommand() {
    super(Instrument.class, InstrumentRepositoryFactory::createRepository);
  }

  @Override
  public void run() {
    
  }
}
