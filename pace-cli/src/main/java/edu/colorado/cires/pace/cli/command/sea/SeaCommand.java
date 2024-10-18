package edu.colorado.cires.pace.cli.command.sea;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.command.common.FindAllCommand;
import edu.colorado.cires.pace.cli.command.common.GetByUUIDCommand;
import edu.colorado.cires.pace.cli.command.common.GetByUniqueFieldCommand;
import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.cli.command.sea.SeaCommand.FindAll;
import edu.colorado.cires.pace.cli.command.sea.SeaCommand.GetByName;
import edu.colorado.cires.pace.cli.command.sea.SeaCommand.GetByUUID;
import edu.colorado.cires.pace.data.object.sea.Sea;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "sea", description = "Manage seas", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class, subcommands = {
    FindAll.class,
    GetByUUID.class,
    GetByName.class
})
public class SeaCommand {
  
  private static final RepositoryFactory<Sea> repositoryFactory = SeaRepositoryFactory::createJsonRepository;
  private static final Class<Sea> clazz = Sea.class;
  private static final TypeReference<List<Sea>> typeReference = new TypeReference<>() {};
  
  @Command(name = "list", description = "List sea areas", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class FindAll extends FindAllCommand<Sea> {

    @Option(names = { "--names", "-n" }, split = ",", description = "Filter results based on names")
    private List<String> names = new ArrayList<>(0);

    @Option(names = {"--show-hidden"}, description = "Filter results based on visibility", defaultValue = "false")
    private Boolean showHidden;

    @Option(names = {"--show-visible"}, description = "Filter results based on visibility", defaultValue = "false")
    private Boolean showVisible;

    @Override
    protected RepositoryFactory<Sea> getRepositoryFactory() {
      return repositoryFactory;
    }

    @Override
    protected List<String> getUniqueFields() {
      return names;
    }

    @Override
    protected Boolean getShowHidden() {
      return showHidden;
    }

    @Override
    protected Boolean getShowVisible() {
      return showVisible;
    }
  }
  
  @Command(name = "get-by-uuid", description = "Get sea area by uuid", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class GetByUUID extends GetByUUIDCommand<Sea> {
    
    @Parameters(description = "Sea area uuid")
    private UUID uuid;

    @Override
    protected Supplier<UUID> getUUIDProvider() {
      return () -> uuid;
    }

    @Override
    protected RepositoryFactory<Sea> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "get-by-name", description = "Get sea area by name", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class GetByName extends GetByUniqueFieldCommand<Sea> {
    
    @Parameters(description = "Sea area name")
    private String name;

    @Override
    protected Supplier<String> getUniqueFieldProvider() {
      return () -> name;
    }

    @Override
    protected RepositoryFactory<Sea> getRepositoryFactory() {
      return repositoryFactory;
    }
  }

}
