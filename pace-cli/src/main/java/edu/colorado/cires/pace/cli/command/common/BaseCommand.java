package edu.colorado.cires.pace.cli.command.common;

import edu.colorado.cires.pace.cli.command.common.BaseCommand.Create;
import edu.colorado.cires.pace.cli.command.common.BaseCommand.Delete;
import edu.colorado.cires.pace.cli.command.common.BaseCommand.GetByUUID;
import edu.colorado.cires.pace.cli.command.common.BaseCommand.GetByUniqueField;
import edu.colorado.cires.pace.cli.command.common.BaseCommand.List;
import edu.colorado.cires.pace.cli.command.common.BaseCommand.Translate;
import edu.colorado.cires.pace.cli.command.common.BaseCommand.Update;
import edu.colorado.cires.pace.cli.command.base.PaceCLI;
import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import java.io.File;
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParentCommand;

@Command(name = "crud-command", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class, subcommands = {
    Create.class,
    Delete.class,
    List.class,
    GetByUUID.class,
    GetByUniqueField.class,
    Update.class,
    Translate.class
})
public abstract class BaseCommand<O extends ObjectWithUniqueField> implements Runnable {
  
  private final Class<O> clazz;
  private final RepositoryFactory<O> repositoryFactory;
  private final RepositoryFactory[] dependencyRepositoryFactories;
  
  @ParentCommand
  private PaceCLI paceCLI;

  protected BaseCommand(Class<O> clazz, RepositoryFactory<O> repositoryFactory) {
    this.clazz = clazz;
    this.repositoryFactory = repositoryFactory;
    this.dependencyRepositoryFactories = new RepositoryFactory<?>[]{};
  }

  protected BaseCommand(Class<O> clazz, RepositoryFactory<O> repositoryFactory, RepositoryFactory... dependencyRepositoryFactories) {
    this.clazz = clazz;
    this.repositoryFactory = repositoryFactory;
    this.dependencyRepositoryFactories = dependencyRepositoryFactories;
  }

  public Class<O> getClazz() {
    return clazz;
  }

  public RepositoryFactory<O> getRepositoryFactory() {
    return repositoryFactory;
  }

  public RepositoryFactory[] getDependencyRepositoryFactories() {
    return dependencyRepositoryFactories;
  }

  @Command(name = "create")
  static class Create<O extends ObjectWithUniqueField> extends CreateCommand<O> {
    
    @ParentCommand
    private BaseCommand baseCommand;

    @Parameters(description = "File containing object (- for stdin)")
    private File file;

    @Override
    protected Supplier<File> getJsonBlobProvider() {
      return () -> file;
    }

    @Override
    protected Class<O> getJsonClass() {
      return baseCommand.getClazz();
    }

    @Override
    protected RepositoryFactory<O> getRepositoryFactory() {
      return baseCommand.getRepositoryFactory();
    }
  }
  
  @Command(name = "delete")
  static class Delete<O extends ObjectWithUniqueField> extends DeleteCommand<O> {
    
    @ParentCommand
    private BaseCommand baseCommand;

    @Parameters(description = "object uuid")
    private UUID uuid;

    @Override
    protected Supplier<UUID> getUUIDProvider() {
      return () -> uuid;
    }

    @Override
    protected RepositoryFactory<O> getRepositoryFactory() {
      return baseCommand.getRepositoryFactory();
    }
  }
  
  @Command(name = "list")
  static class List<O extends ObjectWithUniqueField> extends FindAllCommand<O> {
    
    @ParentCommand
    private BaseCommand baseCommand;

    @Override
    protected RepositoryFactory<O> getRepositoryFactory() {
      return baseCommand.getRepositoryFactory();
    }
  }
  
  @Command(name = "get-by-uuid")
  static class GetByUUID<O extends ObjectWithUniqueField> extends GetByUUIDCommand<O> {
    
    @ParentCommand
    private BaseCommand baseCommand;

    @Parameters(description = "object uuid")
    private UUID uuid;

    @Override
    protected Supplier<UUID> getUUIDProvider() {
      return () -> uuid;
    }

    @Override
    protected RepositoryFactory<O> getRepositoryFactory() {
      return baseCommand.getRepositoryFactory();
    }
  }
  
  @Command(name = "get-by-name")
  static class GetByUniqueField<O extends ObjectWithUniqueField> extends GetByUniqueFieldCommand<O> {
    
    @ParentCommand
    private BaseCommand baseCommand;

    @Parameters(description = "object name")
    private String name;

    @Override
    protected Supplier<String> getUniqueFieldProvider() {
      return () -> name;
    }

    @Override
    protected RepositoryFactory<O> getRepositoryFactory() {
      return baseCommand.getRepositoryFactory();
    }
  }
  
  @Command(name = "update")
  static class Update<O extends ObjectWithUniqueField> extends UpdateCommand<O> {

    @Parameters(description = "File containing object (- for stdin)")
    private File file;
    
    @ParentCommand
    private BaseCommand baseCommand;
    
    @Override
    protected Supplier<File> getJsonBlobProvider() {
      return () -> file;
    }

    @Override
    protected Class<O> getJsonClass() {
      return baseCommand.getClazz();
    }

    @Override
    protected RepositoryFactory<O> getRepositoryFactory() {
      return baseCommand.getRepositoryFactory();
    }
  }
  
  @Command(name = "translate")
  static class Translate<O extends ObjectWithUniqueField> extends TranslateCommand<O> {
    
    @ParentCommand
    private BaseCommand baseCommand;
    
    @Parameters(description = "File to translate from")
    private File file;
    
    @Option(names = {"--translate-from", "-tf"}, description = "File format to translate from", required = true)
    private TranslationType translationType;
    
    @Option(names = {"--translator-name", "-tn"}, description = "Translator name", required = true)
    private String translatorName;

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
    protected <T> Class<T> getJsonClass() {
      return baseCommand.getClazz();
    }

    @Override
    protected RepositoryFactory[] getDependencyRepositoryFactories() {
      return baseCommand.getDependencyRepositoryFactories();
    }
  }

}
