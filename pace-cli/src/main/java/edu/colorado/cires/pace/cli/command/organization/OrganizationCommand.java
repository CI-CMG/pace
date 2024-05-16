package edu.colorado.cires.pace.cli.command.organization;

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
import edu.colorado.cires.pace.cli.command.organization.OrganizationCommand.Create;
import edu.colorado.cires.pace.cli.command.organization.OrganizationCommand.Delete;
import edu.colorado.cires.pace.cli.command.organization.OrganizationCommand.FindAll;
import edu.colorado.cires.pace.cli.command.organization.OrganizationCommand.GenerateTranslator;
import edu.colorado.cires.pace.cli.command.organization.OrganizationCommand.GetByName;
import edu.colorado.cires.pace.cli.command.organization.OrganizationCommand.GetByUUID;
import edu.colorado.cires.pace.cli.command.organization.OrganizationCommand.Translate;
import edu.colorado.cires.pace.cli.command.organization.OrganizationCommand.Update;
import edu.colorado.cires.pace.data.object.Organization;
import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "organization", description = "Manage organizations", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class, subcommands = {
    Create.class,
    FindAll.class,
    GetByUUID.class,
    GetByName.class,
    Update.class,
    Delete.class,
    Translate.class,
    GenerateTranslator.class
})
public class OrganizationCommand implements Runnable {
  
  private static final RepositoryFactory<Organization> repositoryFactory = OrganizationRepositoryFactory::createJsonRepository;
  private static final Class<Organization> clazz = Organization.class; 

  @Override
  public void run() {}
  
  @Command(name = "create", description = "Create organization", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Create extends CreateCommand<Organization> {
    
    @Parameters(description = "File containing organization (- for stdin)")
    private File file;

    @Override
    protected Supplier<File> getJsonBlobProvider() {
      return () -> file;
    }

    @Override
    protected Class<Organization> getJsonClass() {
      return clazz;
    }

    @Override
    public TypeReference<List<Organization>> getTypeReference() {
      return new TypeReference<>() {};
    }

    @Override
    protected RepositoryFactory<Organization> getRepositoryFactory() {
      return repositoryFactory;
    }

    @Override
    protected Class<Organization> getClazz() {
      return clazz;
    }
  }
  
  @Command(name = "list", description = "List organizations", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class FindAll extends FindAllCommand<Organization> {

    @Override
    protected RepositoryFactory<Organization> getRepositoryFactory() {
      return repositoryFactory;
    }

    @Override
    protected Class<Organization> getClazz() {
      return clazz;
    }
  }
  
  @Command(name = "get-by-uuid", description = "Get organization by uuid", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class GetByUUID extends GetByUUIDCommand<Organization> {
    
    @Parameters(description = "Organization uuid")
    private UUID uuid;

    @Override
    protected Supplier<UUID> getUUIDProvider() {
      return () -> uuid;
    }

    @Override
    protected RepositoryFactory<Organization> getRepositoryFactory() {
      return repositoryFactory;
    }

    @Override
    protected Class<Organization> getClazz() {
      return clazz;
    }
  }
  
  @Command(name = "get-by-name", description = "Get organization by name", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class GetByName extends GetByUniqueFieldCommand<Organization> {
    
    @Parameters(description = "Organization name")
    private String name;

    @Override
    protected Supplier<String> getUniqueFieldProvider() {
      return () -> name;
    }

    @Override
    protected RepositoryFactory<Organization> getRepositoryFactory() {
      return repositoryFactory;
    }

    @Override
    protected Class<Organization> getClazz() {
      return clazz;
    }
  }
  
  @Command(name = "update", description = "Update organization", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Update extends UpdateCommand<Organization> {
    
    @Parameters(description = "File containing organization (- for stdin)")
    private File file;

    @Override
    protected Supplier<File> getJsonBlobProvider() {
      return () -> file;
    }

    @Override
    protected Class<Organization> getJsonClass() {
      return clazz;
    }

    @Override
    public TypeReference<List<Organization>> getTypeReference() {
      return new TypeReference<>() {};
    }

    @Override
    protected RepositoryFactory<Organization> getRepositoryFactory() {
      return repositoryFactory;
    }

    @Override
    protected Class<Organization> getClazz() {
      return clazz;
    }
  }
  
  @Command(name = "delete", description = "Delete organization", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Delete extends DeleteCommand<Organization> {
    
    @Parameters(description = "Organization uuid")
    private UUID uuid;

    @Override
    protected Supplier<UUID> getUUIDProvider() {
      return () -> uuid;
    }

    @Override
    protected RepositoryFactory<Organization> getRepositoryFactory() {
      return repositoryFactory;
    }

    @Override
    protected Class<Organization> getClazz() {
      return clazz;
    }
  }
  
  @Command(name = "translate", description = "Translate organization from spreadsheet", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Translate extends TranslateCommand<Organization> {
    
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
    protected Class<Organization> getJsonClass() {
      return clazz;
    }

    @Override
    protected RepositoryFactory[] getDependencyRepositoryFactories() {
      return new RepositoryFactory[0];
    }
  }
  
  @Command(name = "generate-translator", description = "Generate default CSV or Excel translator", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class GenerateTranslator extends GenerateTranslatorCommand<Organization> {
    
    @Parameters(description = "Translator type")
    private TranslationType translatorType;

    @Override
    protected Class<Organization> getClazz() {
      return clazz;
    }

    @Override
    protected TranslationType getTranslatorType() {
      return translatorType;
    }
  }
}
