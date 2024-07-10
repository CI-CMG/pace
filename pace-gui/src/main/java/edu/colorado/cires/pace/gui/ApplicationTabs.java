package edu.colorado.cires.pace.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.datastore.json.DetectionTypeJsonDatastore;
import edu.colorado.cires.pace.datastore.json.FileTypeJsonDatastore;
import edu.colorado.cires.pace.datastore.json.InstrumentJsonDatastore;
import edu.colorado.cires.pace.datastore.json.OrganizationJsonDatastore;
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
    FileTypeJsonDatastore fileTypeJsonDatastore = new FileTypeJsonDatastore(propertyResolver.getDataDir(), objectMapper);
    FileTypeRepository fileTypeRepository = new FileTypeRepository(fileTypeJsonDatastore);
    ProjectRepository projectRepository = new ProjectRepository(new ProjectJsonDatastore(propertyResolver.getDataDir(), objectMapper));
    PersonRepository personRepository = new PersonRepository(new PersonJsonDatastore(propertyResolver.getDataDir(), objectMapper));
    OrganizationRepository organizationRepository = new OrganizationRepository(new OrganizationJsonDatastore(propertyResolver.getDataDir(), objectMapper));
    PlatformRepository platformRepository = new PlatformRepository(new PlatformJsonDatastore(propertyResolver.getDataDir(), objectMapper));
    InstrumentRepository instrumentRepository = new InstrumentRepository(new InstrumentJsonDatastore(propertyResolver.getDataDir(), objectMapper), fileTypeJsonDatastore);
    SensorRepository sensorRepository = new SensorRepository(new SensorJsonDatastore(propertyResolver.getDataDir(), objectMapper));
    DetectionTypeRepository detectionTypeRepository = new DetectionTypeRepository(new DetectionTypeJsonDatastore(propertyResolver.getDataDir(), objectMapper));
    SeaRepository seaRepository = new SeaRepository(new SeaJsonDatastore(propertyResolver.getDataDir(), objectMapper));
    ShipRepository shipRepository = new ShipRepository(new ShipJsonDatastore(propertyResolver.getDataDir(), objectMapper));

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
        translatorRepository
    );
  }

  public ApplicationTabs(ObjectMapper objectMapper, ApplicationPropertyResolver propertyResolver) throws IOException {

    DataPanelFactory dataPanelFactory = createDataPanelFactory(objectMapper, propertyResolver);
    
    setName("applicationTabs");
    setTabPlacement(JTabbedPane.LEFT);
    add("Packages", dataPanelFactory.createPackagesPanel());
    add("Metadata", new MetadataTabbedPane(dataPanelFactory));
    add("Translators", dataPanelFactory.createTranslatorsPanel());
  }
}
