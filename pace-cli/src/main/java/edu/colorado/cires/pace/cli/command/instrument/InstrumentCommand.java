package edu.colorado.cires.pace.cli.command.instrument;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.command.common.CreateCommand;
import edu.colorado.cires.pace.cli.command.common.DeleteCommand;
import edu.colorado.cires.pace.cli.command.common.FindAllCommand;
import edu.colorado.cires.pace.cli.command.common.GenerateTranslatorCommand;
import edu.colorado.cires.pace.cli.command.common.GetByUUIDCommand;
import edu.colorado.cires.pace.cli.command.common.GetByUniqueFieldCommand;
import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.TranslateCommand;
import edu.colorado.cires.pace.utilities.TranslationType;
import edu.colorado.cires.pace.cli.command.common.UpdateCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.cli.command.fileType.FileTypeRepositoryFactory;
import edu.colorado.cires.pace.cli.command.instrument.InstrumentCommand.Create;
import edu.colorado.cires.pace.cli.command.instrument.InstrumentCommand.Delete;
import edu.colorado.cires.pace.cli.command.instrument.InstrumentCommand.FindAll;
import edu.colorado.cires.pace.cli.command.instrument.InstrumentCommand.GenerateTranslator;
import edu.colorado.cires.pace.cli.command.instrument.InstrumentCommand.GetByName;
import edu.colorado.cires.pace.cli.command.instrument.InstrumentCommand.GetByUUID;
import edu.colorado.cires.pace.cli.command.instrument.InstrumentCommand.Translate;
import edu.colorado.cires.pace.cli.command.instrument.InstrumentCommand.Update;
import edu.colorado.cires.pace.data.object.Instrument;
import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "instrument", description = "Manage instruments", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class, subcommands = {
    Create.class,
    FindAll.class,
    GetByUUID.class,
    GetByName.class,
    Update.class,
    Delete.class,
    Translate.class,
    GenerateTranslator.class
})
public class InstrumentCommand implements Runnable {
  
  private static final RepositoryFactory<Instrument> repositoryFactory = InstrumentRepositoryFactory::createJsonRepository;
  private static final Class<Instrument> clazz = Instrument.class;

  @Override
  public void run() {}
  
  @Command(name = "create", description = "Create instrument", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Create extends CreateCommand<Instrument> {
    
    @Parameters(description = "File containing instrument (- for stdin)")
    private File file;

    @Override
    protected Supplier<File> getJsonBlobProvider() {
      return () -> file;
    }

    @Override
    protected Class<Instrument> getJsonClass() {
      return clazz;
    }

    @Override
    public TypeReference<List<Instrument>> getTypeReference() {
      return new TypeReference<>() {};
    }

    @Override
    protected RepositoryFactory<Instrument> getRepositoryFactory() {
      return repositoryFactory;
    }

    @Override
    protected Class<Instrument> getClazz() {
      return clazz;
    }
  }
  
  @Command(name = "list", description = "List instruments", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class FindAll extends FindAllCommand<Instrument> {

    @Override
    protected RepositoryFactory<Instrument> getRepositoryFactory() {
      return repositoryFactory;
    }

    @Override
    protected Class<Instrument> getClazz() {
      return clazz;
    }
  }
  
  @Command(name = "get-by-uuid", description = "Get instrument by uuid", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class GetByUUID extends GetByUUIDCommand<Instrument> {
    
    @Parameters(description = "Instrument uuid")
    private UUID uuid;

    @Override
    protected Supplier<UUID> getUUIDProvider() {
      return () -> uuid;
    }

    @Override
    protected RepositoryFactory<Instrument> getRepositoryFactory() {
      return repositoryFactory;
    }

    @Override
    protected Class<Instrument> getClazz() {
      return clazz;
    }
  }
  
  @Command(name = "get-by-name", description = "Get instrument by name", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class GetByName extends GetByUniqueFieldCommand<Instrument> {
    
    @Parameters(description = "Instrument name")
    private String name;

    @Override
    protected Supplier<String> getUniqueFieldProvider() {
      return () -> name;
    }

    @Override
    protected RepositoryFactory<Instrument> getRepositoryFactory() {
      return repositoryFactory;
    }

    @Override
    protected Class<Instrument> getClazz() {
      return clazz;
    }
  }
  
  @Command(name = "update", description = "Update instrument", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Update extends UpdateCommand<Instrument> {
    
    @Parameters(description = "File containing instrument (- for stdin)")
    private File file;

    @Override
    protected Supplier<File> getJsonBlobProvider() {
      return () -> file;
    }

    @Override
    protected Class<Instrument> getJsonClass() {
      return clazz;
    }

    @Override
    public TypeReference<List<Instrument>> getTypeReference() {
      return new TypeReference<>() {};
    }

    @Override
    protected RepositoryFactory<Instrument> getRepositoryFactory() {
      return repositoryFactory;
    }

    @Override
    protected Class<Instrument> getClazz() {
      return clazz;
    }
  }
  
  @Command(name = "delete", description = "Delete instrument", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Delete extends DeleteCommand<Instrument> {
    
    @Parameters(description = "Instrument uuid")
    private UUID uuid;

    @Override
    protected Supplier<UUID> getUUIDProvider() {
      return () -> uuid;
    }

    @Override
    protected RepositoryFactory<Instrument> getRepositoryFactory() {
      return repositoryFactory;
    }

    @Override
    protected Class<Instrument> getClazz() {
      return clazz;
    }
  }
  
  @Command(name = "translate", description = "Translate instrument from spreadsheet", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Translate extends TranslateCommand<Instrument> {
    
    @Option(names = {"--translate-from", "-tf"}, description = "Input file format", required = true)
    private TranslationType translationType;
    
    @Option(names = {"--translator-name", "-tn"}, description = "Translator name", required = true)
    private String translatorName;
    
    @Parameters(description = "Input file (stdin not supported)")
    private File file;

    @Override
    protected Supplier<TranslationType> getTranslationTypeSupplier() {
      return () -> translationType;
    }

    @Override
    protected Supplier<String> getTranslatorNameSupplier() {
      return () -> translatorName;
    }

    @Override
    protected Supplier<File> getInputSupplier() {
      return () -> file;
    }

    @Override
    protected Class<Instrument> getJsonClass() {
      return clazz;
    }

    @Override
    protected RepositoryFactory[] getDependencyRepositoryFactories() {
      return new RepositoryFactory[] {
          FileTypeRepositoryFactory::createJsonRepository
      };
    }
  }
  
  @Command(name = "generate-translator", description = "Generate default CSV or Excel translator", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class GenerateTranslator extends GenerateTranslatorCommand<Instrument> {
    
    @Parameters(description = "Translator type")
    private TranslationType translatorType;

    @Override
    protected Class<Instrument> getClazz() {
      return clazz;
    }

    @Override
    protected TranslationType getTranslatorType() {
      return translatorType;
    }
  }
}
