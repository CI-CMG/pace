package edu.colorado.cires.pace.cli.command.migrate;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.cli.command.dataset.PackageRepositoryFactory;
import edu.colorado.cires.pace.cli.command.detectionType.DetectionTypeRepositoryFactory;
import edu.colorado.cires.pace.cli.command.fileType.FileTypeRepositoryFactory;
import edu.colorado.cires.pace.cli.command.instrument.InstrumentRepositoryFactory;
import edu.colorado.cires.pace.cli.command.organization.OrganizationRepositoryFactory;
import edu.colorado.cires.pace.cli.command.person.PersonRepositoryFactory;
import edu.colorado.cires.pace.cli.command.platform.PlatformRepositoryFactory;
import edu.colorado.cires.pace.cli.command.project.ProjectRepositoryFactory;
import edu.colorado.cires.pace.cli.command.sea.SeaRepositoryFactory;
import edu.colorado.cires.pace.cli.command.ship.ShipRepositoryFactory;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.datastore.Datastore;
import edu.colorado.cires.pace.migrator.MigrationException;
import edu.colorado.cires.pace.migrator.MigrationRepositoryPair;
import edu.colorado.cires.pace.migrator.Migrator;
import edu.colorado.cires.pace.utilities.ApplicationPropertyResolver;
import edu.colorado.cires.pace.utilities.SerializationUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "migrate", description = "Migrate passive packer sqlite data", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
public class MigrateCommand implements Runnable {
  
  @Parameters(description = "Source data sqlite file")
  private File sourceDataFile;
  
  @Parameters(description = "Local data sqlite file")
  private File localDataFile;

  @Override
  public void run() {
    Path datastoreDirectory = ApplicationPropertyResolver.getDataDir();
    ObjectMapper objectMapper = SerializationUtils.createObjectMapper();
    
    RuntimeException runtimeException = new RuntimeException();

    try {
      Datastore<Package> packageDatastore = PackageRepositoryFactory.createJsonDatastore(datastoreDirectory, objectMapper); 
      
      Migrator.migrate(new MigrationRepositoryPair<>(
          DetectionTypeRepositoryFactory.createSQLiteRepository(sourceDataFile.toPath(), packageDatastore),
          DetectionTypeRepositoryFactory.createJsonRepository(datastoreDirectory, objectMapper)
          ), runtimeException);
      Migrator.migrate(new MigrationRepositoryPair<>(
          FileTypeRepositoryFactory.createSQLiteRepository(sourceDataFile.toPath()),
          FileTypeRepositoryFactory.createJsonRepository(datastoreDirectory, objectMapper)
      ), runtimeException);
      Migrator.migrate(new MigrationRepositoryPair<>(
          InstrumentRepositoryFactory.createSQLiteRepository(sourceDataFile.toPath(), datastoreDirectory, objectMapper, packageDatastore),
          InstrumentRepositoryFactory.createJsonRepository(datastoreDirectory, objectMapper)
      ), runtimeException);
      Migrator.migrate(new MigrationRepositoryPair<>(
          OrganizationRepositoryFactory.createSQLiteRepository(
              localDataFile.toPath(),
              packageDatastore
          ),
          OrganizationRepositoryFactory.createJsonRepository(datastoreDirectory, objectMapper)
      ), runtimeException);
      Migrator.migrate(new MigrationRepositoryPair<>(
          PersonRepositoryFactory.createSQLiteRepository(localDataFile.toPath(), packageDatastore),
          PersonRepositoryFactory.createJsonRepository(datastoreDirectory, objectMapper)
      ), runtimeException);
      Migrator.migrate(new MigrationRepositoryPair<>(
          PlatformRepositoryFactory.createSQLiteRepository(sourceDataFile.toPath(), packageDatastore),
          PlatformRepositoryFactory.createJsonRepository(datastoreDirectory, objectMapper)
      ), runtimeException);
      Migrator.migrate(new MigrationRepositoryPair<>(
          ProjectRepositoryFactory.createSQLiteRepository(localDataFile.toPath(), packageDatastore),
          ProjectRepositoryFactory.createJsonRepository(datastoreDirectory, objectMapper)
      ), runtimeException);
      Migrator.migrate(new MigrationRepositoryPair<>(
          SeaRepositoryFactory.createSQLiteRepository(sourceDataFile.toPath(), packageDatastore),
          SeaRepositoryFactory.createJsonRepository(datastoreDirectory, objectMapper)
      ), runtimeException);
      Migrator.migrate(new MigrationRepositoryPair<>(
          ShipRepositoryFactory.createSQLiteRepository(sourceDataFile.toPath(), packageDatastore),
          ShipRepositoryFactory.createJsonRepository(datastoreDirectory, objectMapper)
      ), runtimeException);

      if (runtimeException.getSuppressed().length > 0) {
        MigrationException migrationException = new MigrationException("Migration failed");
        for (Throwable throwable : runtimeException.getSuppressed()) {
          migrationException.addSuppressed(throwable);
        }
        throw migrationException;
      }
    } catch (MigrationException | IOException e) {
      throw new RuntimeException(e);
    }
  }
}
