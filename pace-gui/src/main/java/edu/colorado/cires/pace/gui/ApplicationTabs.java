package edu.colorado.cires.pace.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.DetectionType;
import edu.colorado.cires.pace.data.object.Organization;
import edu.colorado.cires.pace.data.object.Person;
import edu.colorado.cires.pace.data.object.Platform;
import edu.colorado.cires.pace.data.object.Project;
import edu.colorado.cires.pace.data.object.Sea;
import edu.colorado.cires.pace.data.object.Sensor;
import edu.colorado.cires.pace.data.object.Ship;
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
import javax.swing.JTabbedPane;

public class ApplicationTabs extends JTabbedPane {

  private DataPanelFactory createDataPanelFactory(ObjectMapper objectMapper, ApplicationPropertyResolver propertyResolver) throws IOException {
    TranslatorRepository translatorRepository = new TranslatorRepository(
        new TranslatorJsonDatastore(propertyResolver.getDataDir(), objectMapper)
    );
    PackageJsonDatastore packageJsonDatastore = new PackageJsonDatastore(propertyResolver.getDataDir(), objectMapper);
    FileTypeJsonDatastore fileTypeJsonDatastore = new FileTypeJsonDatastore(propertyResolver.getDataDir(), objectMapper);
    InstrumentJsonDatastore instrumentJsonDatastore = new InstrumentJsonDatastore(propertyResolver.getDataDir(), objectMapper);
    FileTypeRepository fileTypeRepository = new FileTypeRepository(fileTypeJsonDatastore, instrumentJsonDatastore);

    Datastore< Project> projectDatastore = new ProjectJsonDatastore(propertyResolver.getDataDir(), objectMapper);
    ProjectRepository projectRepository = new ProjectRepository(
        projectDatastore,
        packageJsonDatastore
    );
    
    Datastore<Person> personDatastore = new PersonJsonDatastore(propertyResolver.getDataDir(), objectMapper);
    PersonRepository personRepository = new PersonRepository(personDatastore, packageJsonDatastore);
    Datastore<Organization> organizationDatastore = new OrganizationJsonDatastore(propertyResolver.getDataDir(), objectMapper);
    OrganizationRepository organizationRepository = new OrganizationRepository(
        organizationDatastore,
        packageJsonDatastore
    );
    
    Datastore<Platform> platformDatastore = new PlatformJsonDatastore(propertyResolver.getDataDir(), objectMapper);
    PlatformRepository platformRepository = new PlatformRepository(
        platformDatastore,
        packageJsonDatastore
    );
    InstrumentRepository instrumentRepository = new InstrumentRepository(instrumentJsonDatastore, fileTypeJsonDatastore, packageJsonDatastore);
    
    Datastore<Sensor> sensorDatastore = new SensorJsonDatastore(propertyResolver.getDataDir(), objectMapper);
    SensorRepository sensorRepository = new SensorRepository(
        sensorDatastore,
        packageJsonDatastore
    );
    Datastore<DetectionType> detectionTypeDatastore = new DetectionTypeJsonDatastore(propertyResolver.getDataDir(), objectMapper);
    DetectionTypeRepository detectionTypeRepository = new DetectionTypeRepository(
        detectionTypeDatastore,
        packageJsonDatastore
    );
    
    Datastore<Sea> seaDatastore = new SeaJsonDatastore(propertyResolver.getDataDir(), objectMapper);
    SeaRepository seaRepository = new SeaRepository(
        seaDatastore,
        packageJsonDatastore
    );
    
    Datastore<Ship> shipDatastore = new ShipJsonDatastore(propertyResolver.getDataDir(), objectMapper);
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
  private final ApplicationPropertyResolver propertyResolver;

  public ApplicationTabs(ObjectMapper objectMapper, ApplicationPropertyResolver propertyResolver) {
    this.objectMapper = objectMapper;
    this.propertyResolver = propertyResolver;
  }
  
  public void init() throws IOException {
    DataPanelFactory dataPanelFactory = createDataPanelFactory(objectMapper, propertyResolver);

    setName("applicationTabs");
    setTabPlacement(JTabbedPane.LEFT);
    add("Packages", dataPanelFactory.createPackagesPanel());
    add("Metadata", new MetadataTabbedPane(dataPanelFactory));
    add("Translators", dataPanelFactory.createTranslatorsPanel());
  }
}
