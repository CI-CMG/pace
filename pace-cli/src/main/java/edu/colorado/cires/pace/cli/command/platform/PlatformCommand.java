package edu.colorado.cires.pace.cli.command.platform;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.command.common.CreateCommand;
import edu.colorado.cires.pace.cli.command.common.DeleteCommand;
import edu.colorado.cires.pace.cli.command.common.FindAllCommand;
import edu.colorado.cires.pace.cli.command.common.GetByUUIDCommand;
import edu.colorado.cires.pace.cli.command.common.GetByUniqueFieldCommand;
import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.TranslateCommand;
import edu.colorado.cires.pace.data.translator.PlatformTranslator;
import edu.colorado.cires.pace.translator.converter.Converter;
import edu.colorado.cires.pace.translator.converter.PlatformConverter;
import edu.colorado.cires.pace.utilities.TranslationType;
import edu.colorado.cires.pace.cli.command.common.UpdateCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.cli.command.platform.PlatformCommand.Create;
import edu.colorado.cires.pace.cli.command.platform.PlatformCommand.Delete;
import edu.colorado.cires.pace.cli.command.platform.PlatformCommand.FindAll;
import edu.colorado.cires.pace.cli.command.platform.PlatformCommand.GetByName;
import edu.colorado.cires.pace.cli.command.platform.PlatformCommand.GetByUUID;
import edu.colorado.cires.pace.cli.command.platform.PlatformCommand.Translate;
import edu.colorado.cires.pace.cli.command.platform.PlatformCommand.Update;
import edu.colorado.cires.pace.data.object.Platform;
import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "platform", description = "Manage platforms", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class, subcommands = {
    Create.class,
    FindAll.class,
    GetByUUID.class,
    GetByName.class,
    Update.class,
    Delete.class,
    Translate.class
})
public class PlatformCommand implements Runnable {
  
  private static final RepositoryFactory<Platform> repositoryFactory = PlatformRepositoryFactory::createJsonRepository;
  private static final Class<Platform> clazz = Platform.class;
  private static final TypeReference<List<Platform>> typeReference = new TypeReference<>() {};

  @Override
  public void run() {}
  
  @Command(name = "create", description = "Create platform", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Create extends CreateCommand<Platform> {
    
    @Parameters(description = "File containing platform (- for stdin)")
    private File file;

    @Override
    protected Supplier<File> getJsonBlobProvider() {
      return () -> file;
    }

    @Override
    protected Class<Platform> getJsonClass() {
      return clazz;
    }

    @Override
    public TypeReference<List<Platform>> getTypeReference() {
      return typeReference;
    }

    @Override
    protected RepositoryFactory<Platform> getRepositoryFactory() {
      return repositoryFactory;
    }

    @Override
    protected Class<Platform> getClazz() {
      return clazz;
    }
  }
  
  @Command(name = "list", description = "List platforms", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class FindAll extends FindAllCommand<Platform> {

    @Override
    protected RepositoryFactory<Platform> getRepositoryFactory() {
      return repositoryFactory;
    }

    @Override
    protected Class<Platform> getClazz() {
      return clazz;
    }
  }
  
  @Command(name = "get-by-uuid", description = "Get platform by uuid", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class GetByUUID extends GetByUUIDCommand<Platform> {
    
    @Parameters(description = "Platform uuid")
    private UUID uuid;

    @Override
    protected Supplier<UUID> getUUIDProvider() {
      return () -> uuid;
    }

    @Override
    protected RepositoryFactory<Platform> getRepositoryFactory() {
      return repositoryFactory;
    }

    @Override
    protected Class<Platform> getClazz() {
      return clazz;
    }
  }
  
  @Command(name = "get-by-name", description = "Get platform by name", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class GetByName extends GetByUniqueFieldCommand<Platform> {
    
    @Parameters(description = "Platform name")
    private String name;

    @Override
    protected Supplier<String> getUniqueFieldProvider() {
      return () -> name;
    }

    @Override
    protected RepositoryFactory<Platform> getRepositoryFactory() {
      return repositoryFactory;
    }

    @Override
    protected Class<Platform> getClazz() {
      return clazz;
    }
  }
  
  @Command(name = "update", description = "Update platform", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Update extends UpdateCommand<Platform> {
    
    @Parameters(description = "FIle containing platform (- for stdin)")
    private File file;

    @Override
    protected Supplier<File> getJsonBlobProvider() {
      return () -> file;
    }

    @Override
    protected Class<Platform> getJsonClass() {
      return clazz;
    }

    @Override
    public TypeReference<List<Platform>> getTypeReference() {
      return typeReference;
    }

    @Override
    protected RepositoryFactory<Platform> getRepositoryFactory() {
      return repositoryFactory;
    }

    @Override
    protected Class<Platform> getClazz() {
      return clazz;
    }
  }
  
  @Command(name = "delete", description = "Delete platform", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Delete extends DeleteCommand<Platform> {
    
    @Parameters(description = "Platform uuid")
    private UUID uuid;

    @Override
    protected Supplier<UUID> getUUIDProvider() {
      return () -> uuid;
    }

    @Override
    protected RepositoryFactory<Platform> getRepositoryFactory() {
      return repositoryFactory;
    }

    @Override
    protected Class<Platform> getClazz() {
      return clazz;
    }
  }
  
  @Command(name = "translate", description = "Translate platform from spreadsheet", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Translate extends TranslateCommand<Platform, PlatformTranslator> {
    
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
    protected Class<Platform> getJsonClass() {
      return clazz;
    }

    @Override
    protected RepositoryFactory<Platform>[] getDependencyRepositoryFactories() {
      return new RepositoryFactory[0];
    }

    @Override
    protected Converter<PlatformTranslator, Platform> getConverter() {
      return new PlatformConverter();
    }

    @Override
    protected TypeReference<List<Platform>> getTypeReference() {
      return typeReference;
    }
  }
}
