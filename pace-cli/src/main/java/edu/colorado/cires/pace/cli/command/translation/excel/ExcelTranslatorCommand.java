package edu.colorado.cires.pace.cli.command.translation.excel;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.command.common.CreateCommand;
import edu.colorado.cires.pace.cli.command.common.DeleteCommand;
import edu.colorado.cires.pace.cli.command.common.FindAllCommand;
import edu.colorado.cires.pace.cli.command.common.GetByUUIDCommand;
import edu.colorado.cires.pace.cli.command.common.GetByUniqueFieldCommand;
import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.UpdateCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.cli.command.translation.excel.ExcelTranslatorCommand.Create;
import edu.colorado.cires.pace.cli.command.translation.excel.ExcelTranslatorCommand.Delete;
import edu.colorado.cires.pace.cli.command.translation.excel.ExcelTranslatorCommand.FindAll;
import edu.colorado.cires.pace.cli.command.translation.excel.ExcelTranslatorCommand.GetByName;
import edu.colorado.cires.pace.cli.command.translation.excel.ExcelTranslatorCommand.GetByUUID;
import edu.colorado.cires.pace.cli.command.translation.excel.ExcelTranslatorCommand.Update;
import edu.colorado.cires.pace.data.object.ExcelTranslator;
import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "excel-translator", description = "Manage Excel translators", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class, subcommands = {
    Create.class,
    FindAll.class,
    GetByUUID.class,
    GetByName.class,
    Update.class,
    Delete.class
})
public class ExcelTranslatorCommand implements Runnable {

  private static final RepositoryFactory<ExcelTranslator> repositoryFactory = ExcelTranslatorRepositoryFactory::createRepository;
  private static final Class<ExcelTranslator> clazz = ExcelTranslator.class;
  
  @Override
  public void run() {}
  
  @Command(name = "create", description = "Create Excel translator", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Create extends CreateCommand<ExcelTranslator> {
    
    @Parameters(description = "File containing Excel translator (- for stdin)")
    private File file;

    @Override
    protected Supplier<File> getJsonBlobProvider() {
      return () -> file;
    }

    @Override
    protected Class<ExcelTranslator> getJsonClass() {
      return clazz;
    }

    @Override
    public TypeReference<List<ExcelTranslator>> getTypeReference() {
      return new TypeReference<>() {};
    }

    @Override
    protected RepositoryFactory<ExcelTranslator> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "list", description = "List Excel translators", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class FindAll extends FindAllCommand<ExcelTranslator> {

    @Override
    protected RepositoryFactory<ExcelTranslator> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "get-by-uuid", description = "Get Excel translator by uuid", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class GetByUUID extends GetByUUIDCommand<ExcelTranslator> {
    
    @Parameters(description = "Excel translator uuid")
    private UUID uuid;

    @Override
    protected Supplier<UUID> getUUIDProvider() {
      return () -> uuid;
    }

    @Override
    protected RepositoryFactory<ExcelTranslator> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "get-by-name", description = "Get Excel translator by name", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class GetByName extends GetByUniqueFieldCommand<ExcelTranslator> {
    
    @Parameters(description = "Excel translator name")
    private String name;

    @Override
    protected Supplier<String> getUniqueFieldProvider() {
      return () -> name;
    }

    @Override
    protected RepositoryFactory<ExcelTranslator> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "update", description = "Update Excel translator", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Update extends UpdateCommand<ExcelTranslator> {
    
    @Parameters(description = "File containing Excel translator (- for stdin)")
    private File file;

    @Override
    protected Supplier<File> getJsonBlobProvider() {
      return () -> file;
    }

    @Override
    protected Class<ExcelTranslator> getJsonClass() {
      return clazz;
    }

    @Override
    public TypeReference<List<ExcelTranslator>> getTypeReference() {
      return new TypeReference<>() {};
    }

    @Override
    protected RepositoryFactory<ExcelTranslator> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "delete", description = "Delete Excel translator", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Delete extends DeleteCommand<ExcelTranslator> {
    
    @Parameters(description = "Excel translator uuid")
    private UUID uuid;

    @Override
    protected Supplier<UUID> getUUIDProvider() {
      return () -> uuid;
    }

    @Override
    protected RepositoryFactory<ExcelTranslator> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
}
