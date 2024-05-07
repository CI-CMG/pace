package edu.colorado.cires.pace.cli.command.project;

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
import edu.colorado.cires.pace.cli.command.project.ProjectCommand.Create;
import edu.colorado.cires.pace.cli.command.project.ProjectCommand.Delete;
import edu.colorado.cires.pace.cli.command.project.ProjectCommand.FindAll;
import edu.colorado.cires.pace.cli.command.project.ProjectCommand.GenerateTranslator;
import edu.colorado.cires.pace.cli.command.project.ProjectCommand.GetByName;
import edu.colorado.cires.pace.cli.command.project.ProjectCommand.GetByUUID;
import edu.colorado.cires.pace.cli.command.project.ProjectCommand.Translate;
import edu.colorado.cires.pace.cli.command.project.ProjectCommand.Update;
import edu.colorado.cires.pace.data.object.Project;
import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "project", description = "Manage projects", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class, subcommands = {
    Create.class,
    FindAll.class,
    GetByUUID.class,
    GetByName.class,
    Update.class,
    Delete.class,
    Translate.class,
    GenerateTranslator.class
})
public class ProjectCommand implements Runnable {
  
  private static final RepositoryFactory<Project> repositoryFactory = ProjectRepositoryFactory::createJsonRepository;
  private static final Class<Project> clazz = Project.class;

  @Override
  public void run() {}
  
  @Command(name = "create", description = "Create project", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Create extends CreateCommand<Project> {
    
    @Parameters(description = "File containing project (- for stdin)")
    private File file;

    @Override
    protected Supplier<File> getJsonBlobProvider() {
      return () -> file;
    }

    @Override
    protected Class<Project> getJsonClass() {
      return clazz;
    }

    @Override
    public TypeReference<List<Project>> getTypeReference() {
      return new TypeReference<>() {};
    }

    @Override
    protected RepositoryFactory<Project> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "list", description = "List projects", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class FindAll extends FindAllCommand<Project> {

    @Override
    protected RepositoryFactory<Project> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "uuid", description = "Get project by uuid", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class GetByUUID extends GetByUUIDCommand<Project> {
    
    @Parameters(description = "Project uuid")
    private UUID uuid;

    @Override
    protected Supplier<UUID> getUUIDProvider() {
      return () -> uuid;
    }

    @Override
    protected RepositoryFactory<Project> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "get-by-name", description = "Get project by name", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class GetByName extends GetByUniqueFieldCommand<Project> {
    
    @Parameters(description = "Project name")
    private String name;

    @Override
    protected Supplier<String> getUniqueFieldProvider() {
      return () -> name;
    }

    @Override
    protected RepositoryFactory<Project> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "update", description = "Update project", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Update extends UpdateCommand<Project> {

    @Parameters(description = "File containing project (- for stdin)")
    private File file;

    @Override
    protected Supplier<File> getJsonBlobProvider() {
      return () -> file;
    }

    @Override
    protected Class<Project> getJsonClass() {
      return clazz;
    }

    @Override
    public TypeReference<List<Project>> getTypeReference() {
      return new TypeReference<>() {};
    }

    @Override
    protected RepositoryFactory<Project> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "delete", description = "Delete project", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Delete extends DeleteCommand<Project> {
    
    @Parameters(description = "Project uuid")
    private UUID uuid;

    @Override
    protected Supplier<UUID> getUUIDProvider() {
      return () -> uuid;
    }

    @Override
    protected RepositoryFactory<Project> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "translate", description = "Translate project from spreadsheet", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Translate extends TranslateCommand<Project> {
    
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
    protected Class<Project> getJsonClass() {
      return clazz;
    }

    @Override
    protected RepositoryFactory[] getDependencyRepositoryFactories() {
      return new RepositoryFactory[0];
    }
  }
  
  @Command(name = "generate-translator", description = "Generate default CSV or Excel translator", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class GenerateTranslator extends GenerateTranslatorCommand<Project> {
    
    @Parameters(description = "Translator type")
    private TranslationType translatorType;

    @Override
    protected Class<Project> getClazz() {
      return clazz;
    }

    @Override
    protected TranslationType getTranslatorType() {
      return translatorType;
    }
  }
  
}
