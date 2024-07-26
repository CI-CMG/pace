package edu.colorado.cires.pace.cli.command.dataset;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.cli.command.detectionType.DetectionTypeRepositoryFactory;
import edu.colorado.cires.pace.cli.command.instrument.InstrumentRepositoryFactory;
import edu.colorado.cires.pace.cli.command.organization.OrganizationRepositoryFactory;
import edu.colorado.cires.pace.cli.command.person.PersonRepositoryFactory;
import edu.colorado.cires.pace.cli.command.platform.PlatformRepositoryFactory;
import edu.colorado.cires.pace.cli.command.project.ProjectRepositoryFactory;
import edu.colorado.cires.pace.cli.command.sea.SeaRepositoryFactory;
import edu.colorado.cires.pace.cli.command.sensor.SensorRepositoryFactory;
import edu.colorado.cires.pace.cli.command.ship.ShipRepositoryFactory;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.datastore.Datastore;
import edu.colorado.cires.pace.datastore.json.PackageJsonDatastore;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.PackageRepository;
import java.io.IOException;
import java.nio.file.Path;

public final class PackageRepositoryFactory {
  
  public static CRUDRepository<Package> createRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new PackageRepository(
        createJsonDatastore(datastoreDirectory, objectMapper),
        DetectionTypeRepositoryFactory.createJsonDatastore(datastoreDirectory, objectMapper),
        InstrumentRepositoryFactory.createJsonDatastore(datastoreDirectory, objectMapper),
        OrganizationRepositoryFactory.createJsonDatastore(datastoreDirectory, objectMapper),
        PersonRepositoryFactory.createJsonDatastore(datastoreDirectory, objectMapper),
        PlatformRepositoryFactory.createJsonDatastore(datastoreDirectory, objectMapper),
        ProjectRepositoryFactory.createJsonDatastore(datastoreDirectory, objectMapper),
        SeaRepositoryFactory.createJsonDatastore(datastoreDirectory, objectMapper),
        SensorRepositoryFactory.createJsonDatastore(datastoreDirectory, objectMapper),
        ShipRepositoryFactory.createJsonDatastore(datastoreDirectory, objectMapper)
    );
  }

  public static Datastore<Package> createJsonDatastore(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new PackageJsonDatastore(
        datastoreDirectory, objectMapper
    );
  }

}
