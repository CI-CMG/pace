package edu.colorado.cires.pace.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.detectionType.DetectionType;
import edu.colorado.cires.pace.data.object.contact.organization.Organization;
import edu.colorado.cires.pace.data.object.contact.person.Person;
import edu.colorado.cires.pace.data.object.platform.Platform;
import edu.colorado.cires.pace.data.object.project.Project;
import edu.colorado.cires.pace.data.object.sea.Sea;
import edu.colorado.cires.pace.data.object.sensor.base.Sensor;
import edu.colorado.cires.pace.data.object.ship.Ship;
import edu.colorado.cires.pace.datastore.Datastore;
import edu.colorado.cires.pace.datastore.json.DetectionTypeJsonDatastore;
import edu.colorado.cires.pace.datastore.json.FileTypeJsonDatastore;
import edu.colorado.cires.pace.datastore.json.InstrumentJsonDatastore;
import edu.colorado.cires.pace.datastore.json.OrganizationJsonDatastore;
import edu.colorado.cires.pace.datastore.json.PackageJsonDatastore;
import edu.colorado.cires.pace.datastore.json.PersonJsonDatastore;
import edu.colorado.cires.pace.datastore.json.PlatformJsonDatastore;
import edu.colorado.cires.pace.datastore.json.ProjectJsonDatastore;
import edu.colorado.cires.pace.datastore.json.SeaJsonDatastore;
import edu.colorado.cires.pace.datastore.json.SensorJsonDatastore;
import edu.colorado.cires.pace.datastore.json.ShipJsonDatastore;
import edu.colorado.cires.pace.datastore.json.TranslatorJsonDatastore;
import edu.colorado.cires.pace.repository.DetectionTypeRepository;
import edu.colorado.cires.pace.repository.FileTypeRepository;
import edu.colorado.cires.pace.repository.InstrumentRepository;
import edu.colorado.cires.pace.repository.OrganizationRepository;
import edu.colorado.cires.pace.repository.PackageRepository;
import edu.colorado.cires.pace.repository.PersonRepository;
import edu.colorado.cires.pace.repository.PlatformRepository;
import edu.colorado.cires.pace.repository.ProjectRepository;
import edu.colorado.cires.pace.repository.SeaRepository;
import edu.colorado.cires.pace.repository.SensorRepository;
import edu.colorado.cires.pace.repository.ShipRepository;
import edu.colorado.cires.pace.repository.TranslatorRepository;
import edu.colorado.cires.pace.utilities.ApplicationPropertyResolver;
import java.io.IOException;
import java.nio.file.Path;
import javax.swing.JTabbedPane;

public class ApplicationTabs extends JTabbedPane {

  private DataPanelFactory createDataPanelFactory(ObjectMapper objectMapper) throws IOException {
    Path dataDir = ApplicationPropertyResolver.getDataDir();
    
    TranslatorRepository translatorRepository = new TranslatorRepository(
        new TranslatorJsonDatastore(dataDir, objectMapper)
    );
    PackageJsonDatastore packageJsonDatastore = new PackageJsonDatastore(dataDir, objectMapper);
    FileTypeJsonDatastore fileTypeJsonDatastore = new FileTypeJsonDatastore(dataDir, objectMapper);
    InstrumentJsonDatastore instrumentJsonDatastore = new InstrumentJsonDatastore(dataDir, objectMapper);
    FileTypeRepository fileTypeRepository = new FileTypeRepository(fileTypeJsonDatastore, instrumentJsonDatastore);

    Datastore< Project> projectDatastore = new ProjectJsonDatastore(dataDir, objectMapper);
    ProjectRepository projectRepository = new ProjectRepository(
        projectDatastore,
        packageJsonDatastore
    );
    
    Datastore<Person> personDatastore = new PersonJsonDatastore(dataDir, objectMapper);
    PersonRepository personRepository = new PersonRepository(personDatastore, packageJsonDatastore);
    Datastore<Organization> organizationDatastore = new OrganizationJsonDatastore(dataDir, objectMapper);
    OrganizationRepository organizationRepository = new OrganizationRepository(
        organizationDatastore,
        packageJsonDatastore
    );
    
    Datastore<Platform> platformDatastore = new PlatformJsonDatastore(dataDir, objectMapper);
    PlatformRepository platformRepository = new PlatformRepository(
        platformDatastore,
        packageJsonDatastore
    );
    InstrumentRepository instrumentRepository = new InstrumentRepository(instrumentJsonDatastore, fileTypeJsonDatastore, packageJsonDatastore);
    
    Datastore<Sensor> sensorDatastore = new SensorJsonDatastore(dataDir, objectMapper);
    SensorRepository sensorRepository = new SensorRepository(
        sensorDatastore,
        packageJsonDatastore
    );
    Datastore<DetectionType> detectionTypeDatastore = new DetectionTypeJsonDatastore(dataDir, objectMapper);
    DetectionTypeRepository detectionTypeRepository = new DetectionTypeRepository(
        detectionTypeDatastore,
        packageJsonDatastore
    );
    
    Datastore<Sea> seaDatastore = new SeaJsonDatastore(dataDir, objectMapper);
    SeaRepository seaRepository = new SeaRepository(
        seaDatastore,
        packageJsonDatastore
    );
    
    Datastore<Ship> shipDatastore = new ShipJsonDatastore(dataDir, objectMapper);
    ShipRepository shipRepository = new ShipRepository(
        shipDatastore,
        packageJsonDatastore
    );
    PackageRepository packageRepository = new PackageRepository(
        packageJsonDatastore,
        detectionTypeDatastore,
        instrumentJsonDatastore,
        organizationDatastore,
        personDatastore,
        platformDatastore,
        projectDatastore,
        seaDatastore,
        sensorDatastore,
        shipDatastore
    );

    return new DataPanelFactory(
        projectRepository,
        personRepository,
        organizationRepository,
        platformRepository,
        instrumentRepository,
        sensorRepository,
        detectionTypeRepository,
        seaRepository,
        shipRepository,
        fileTypeRepository,
        translatorRepository,
        packageRepository
    );
  }
  
  private final ObjectMapper objectMapper;

  public ApplicationTabs(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }
  
  public void init() throws IOException {
    DataPanelFactory dataPanelFactory = createDataPanelFactory(objectMapper);

    setName("applicationTabs");
    setTabPlacement(JTabbedPane.LEFT);
    add("Packages", dataPanelFactory.createPackagesPanel());
    add("Metadata", new MetadataTabbedPane(dataPanelFactory));
    add("Translators", dataPanelFactory.createTranslatorsPanel());
  }
}
