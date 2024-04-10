package edu.colorado.cires.pace.cli.command.translation.csv;

import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.FindAllCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.object.CSVTranslator;
import picocli.CommandLine.Command;

@Command(name = "list", description = "List all CSV translators", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class FindAllCSVTranslatorsCommand extends FindAllCommand<CSVTranslator> {

  @Override
  protected RepositoryFactory<CSVTranslator> getRepositoryFactory() {
    return CSVTranslatorRepositoryFactory::createRepository;
  }
}
