package edu.colorado.cires.pace.cli.command.translation.excel;

import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.FindAllCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.object.ExcelTranslator;
import picocli.CommandLine.Command;

@Command(name = "list", description = "List all excel translators", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class FindAllExcelTranslatorsCommand extends FindAllCommand<ExcelTranslator> {

  @Override
  protected RepositoryFactory<ExcelTranslator> getRepositoryFactory() {
    return ExcelTranslatorRepositoryFactory::createRepository;
  }
}
