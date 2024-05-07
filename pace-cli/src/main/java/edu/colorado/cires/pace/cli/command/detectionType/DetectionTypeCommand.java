package edu.colorado.cires.pace.cli.command.detectionType;

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
import edu.colorado.cires.pace.cli.command.detectionType.DetectionTypeCommand.Create;
import edu.colorado.cires.pace.cli.command.detectionType.DetectionTypeCommand.Delete;
import edu.colorado.cires.pace.cli.command.detectionType.DetectionTypeCommand.FindAll;
import edu.colorado.cires.pace.cli.command.detectionType.DetectionTypeCommand.GenerateTranslator;
import edu.colorado.cires.pace.cli.command.detectionType.DetectionTypeCommand.GetBySource;
import edu.colorado.cires.pace.cli.command.detectionType.DetectionTypeCommand.GetByUUID;
import edu.colorado.cires.pace.cli.command.detectionType.DetectionTypeCommand.Translate;
import edu.colorado.cires.pace.cli.command.detectionType.DetectionTypeCommand.Update;
import edu.colorado.cires.pace.data.object.DetectionType;
import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "detection-type", description = "Manage detection types", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class, subcommands = {
    Create.class,
    FindAll.class,
    GetByUUID.class,
    GetBySource.class,
    Update.class,
    Delete.class,
    Translate.class,
    GenerateTranslator.class
})
public class DetectionTypeCommand implements Runnable {
  
  private static final RepositoryFactory<DetectionType> repositoryFactory = DetectionTypeRepositoryFactory::createJsonRepository;
  private static final Class<DetectionType> clazz = DetectionType.class;

  @Override
  public void run() {}
  
  @Command(name = "create", description = "Create detection type", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Create extends CreateCommand<DetectionType> {
    
    @Parameters(description = "File containing detection type (- for stdin)")
    private File file;

    @Override
    protected Supplier<File> getJsonBlobProvider() {
      return () -> file;
    }

    @Override
    protected Class<DetectionType> getJsonClass() {
      return clazz;
    }

    @Override
    public TypeReference<List<DetectionType>> getTypeReference() {
      return new TypeReference<>() {};
    }

    @Override
    protected RepositoryFactory<DetectionType> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "list", description = "List detection types", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class FindAll extends FindAllCommand<DetectionType> {

    @Override
    protected RepositoryFactory<DetectionType> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "get-by-uuid", description = "Get detection type by uuid", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class GetByUUID extends GetByUUIDCommand<DetectionType> {
    
    @Parameters(description = "Detection type uuid")
    private UUID uuid;

    @Override
    protected Supplier<UUID> getUUIDProvider() {
      return () -> uuid;
    }

    @Override
    protected RepositoryFactory<DetectionType> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "get-by-source", description = "Get detection type by source", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class GetBySource extends GetByUniqueFieldCommand<DetectionType> {
    
    @Parameters(description = "Detection type source")
    private String source;

    @Override
    protected Supplier<String> getUniqueFieldProvider() {
      return () -> source;
    }

    @Override
    protected RepositoryFactory<DetectionType> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "update", description = "Update detection type", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Update extends UpdateCommand<DetectionType> {
    
    @Parameters(description = "File containing detection type (- for stdin)")
    private File file;

    @Override
    protected Supplier<File> getJsonBlobProvider() {
      return () -> file;
    }

    @Override
    protected Class<DetectionType> getJsonClass() {
      return clazz;
    }

    @Override
    public TypeReference<List<DetectionType>> getTypeReference() {
      return new TypeReference<>() {};
    }

    @Override
    protected RepositoryFactory<DetectionType> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "delete", description = "Delete detection type", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Delete extends DeleteCommand<DetectionType> {
    
    @Parameters(description = "Detection type uuid")
    private UUID uuid;

    @Override
    protected Supplier<UUID> getUUIDProvider() {
      return () -> uuid;
    }

    @Override
    protected RepositoryFactory<DetectionType> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "translate", description = "Translate detection type from spreadsheet", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Translate extends TranslateCommand<DetectionType> {
    
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
    protected Class<DetectionType> getJsonClass() {
      return clazz;
    }

    @Override
    protected RepositoryFactory[] getDependencyRepositoryFactories() {
      return new RepositoryFactory[0];
    }
  }
  
  @Command(name = "generate-translator", description = "Generate default CSV or Excel Translator", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class GenerateTranslator extends GenerateTranslatorCommand<DetectionType> {
    
    @Parameters(description = "Translator type")
    private TranslationType translatorType;

    @Override
    protected Class<DetectionType> getClazz() {
      return clazz;
    }

    @Override
    protected TranslationType getTranslatorType() {
      return translatorType;
    }
  }
}
