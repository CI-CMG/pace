package edu.colorado.cires.pace.cli.command.fileType;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.command.common.CreateCommand;
import edu.colorado.cires.pace.cli.command.common.DeleteCommand;
import edu.colorado.cires.pace.cli.command.common.FindAllCommand;
import edu.colorado.cires.pace.cli.command.common.GetByUUIDCommand;
import edu.colorado.cires.pace.cli.command.common.GetByUniqueFieldCommand;
import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.TranslateCommand;
import edu.colorado.cires.pace.data.translator.FileTypeTranslator;
import edu.colorado.cires.pace.translator.converter.Converter;
import edu.colorado.cires.pace.translator.converter.FileTypeConverter;
import edu.colorado.cires.pace.utilities.TranslationType;
import edu.colorado.cires.pace.cli.command.common.UpdateCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.cli.command.fileType.FileTypeCommand.Create;
import edu.colorado.cires.pace.cli.command.fileType.FileTypeCommand.Delete;
import edu.colorado.cires.pace.cli.command.fileType.FileTypeCommand.FindAll;
import edu.colorado.cires.pace.cli.command.fileType.FileTypeCommand.GetByType;
import edu.colorado.cires.pace.cli.command.fileType.FileTypeCommand.GetByUUID;
import edu.colorado.cires.pace.cli.command.fileType.FileTypeCommand.Translate;
import edu.colorado.cires.pace.cli.command.fileType.FileTypeCommand.Update;
import edu.colorado.cires.pace.data.object.FileType;
import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "file-type", description = "Manage file types", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class, subcommands = {
    Create.class,
    FindAll.class,
    GetByUUID.class,
    GetByType.class,
    Update.class,
    Delete.class,
    Translate.class
})
public class FileTypeCommand implements Runnable {
  
  private static final RepositoryFactory<FileType> repositoryFactory = FileTypeRepositoryFactory::createJsonRepository;
  private static final Class<FileType> clazz = FileType.class;
  private static final TypeReference<List<FileType>> typeReference = new TypeReference<>() {};

  @Override
  public void run() {}
  
  @Command(name = "create", description = "Create file type", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Create extends CreateCommand<FileType> {
    
    @Parameters(description = "File containing file type (- for stdin)")
    private File file;

    @Override
    protected Supplier<File> getJsonBlobProvider() {
      return () -> file;
    }

    @Override
    protected Class<FileType> getJsonClass() {
      return clazz;
    }

    @Override
    public TypeReference<List<FileType>> getTypeReference() {
      return typeReference;
    }

    @Override
    protected RepositoryFactory<FileType> getRepositoryFactory() {
      return repositoryFactory;
    }

    @Override
    protected Class<FileType> getClazz() {
      return clazz;
    }
  }
  
  @Command(name = "list", description = "List file types", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class FindAll extends FindAllCommand<FileType> {

    @Override
    protected RepositoryFactory<FileType> getRepositoryFactory() {
      return repositoryFactory;
    }

    @Override
    protected Class<FileType> getClazz() {
      return clazz;
    }
  }
  
  @Command(name = "get-by-uuid", description = "Get file type by uuid", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class GetByUUID extends GetByUUIDCommand<FileType> {
    
    @Parameters(description = "File type uuid")
    private UUID uuid;

    @Override
    protected Supplier<UUID> getUUIDProvider() {
      return () -> uuid;
    }

    @Override
    protected RepositoryFactory<FileType> getRepositoryFactory() {
      return repositoryFactory;
    }

    @Override
    protected Class<FileType> getClazz() {
      return clazz;
    }
  }
  
  @Command(name = "get-by-type", description = "Get file type by type", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class GetByType extends GetByUniqueFieldCommand<FileType> {
    
    @Parameters(description = "File type")
    private String type;

    @Override
    protected Supplier<String> getUniqueFieldProvider() {
      return () -> type;
    }

    @Override
    protected RepositoryFactory<FileType> getRepositoryFactory() {
      return repositoryFactory;
    }

    @Override
    protected Class<FileType> getClazz() {
      return clazz;
    }
  }
  
  @Command(name = "update", description = "Update file type", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Update extends UpdateCommand<FileType> {
    
    @Parameters(description = "File containing file type (- for stdin)")
    private File file;

    @Override
    protected Supplier<File> getJsonBlobProvider() {
      return () -> file;
    }

    @Override
    protected Class<FileType> getJsonClass() {
      return clazz;
    }

    @Override
    public TypeReference<List<FileType>> getTypeReference() {
      return typeReference;
    }

    @Override
    protected RepositoryFactory<FileType> getRepositoryFactory() {
      return repositoryFactory;
    }

    @Override
    protected Class<FileType> getClazz() {
      return clazz;
    }
  }
  
  @Command(name = "delete", description = "Delete file type", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Delete extends DeleteCommand<FileType> {
    
    @Parameters(description = "File type uuid")
    private UUID uuid;

    @Override
    protected Supplier<UUID> getUUIDProvider() {
      return () -> uuid;
    }

    @Override
    protected RepositoryFactory<FileType> getRepositoryFactory() {
      return repositoryFactory;
    }

    @Override
    protected Class<FileType> getClazz() {
      return clazz;
    }
  }
  
  @Command(name = "translate", description = "Translate file type from spreadsheet", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Translate extends TranslateCommand<FileType, FileTypeTranslator> {
    
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
    protected Class<FileType> getJsonClass() {
      return clazz;
    }

    @Override
    protected RepositoryFactory<FileType>[] getDependencyRepositoryFactories() {
      return new RepositoryFactory[0];
    }

    @Override
    protected Converter<FileTypeTranslator, FileType> getConverter() {
      return new FileTypeConverter();
    }

    @Override
    protected TypeReference<List<FileType>> getTypeReference() {
      return typeReference;
    }
  }
}
