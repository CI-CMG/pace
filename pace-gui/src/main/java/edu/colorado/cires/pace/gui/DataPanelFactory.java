package edu.colorado.cires.pace.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.DetectionType;
import edu.colorado.cires.pace.data.object.FileType;
import edu.colorado.cires.pace.data.object.Instrument;
import edu.colorado.cires.pace.data.object.Organization;
import edu.colorado.cires.pace.data.object.Package;
import edu.colorado.cires.pace.data.object.Person;
import edu.colorado.cires.pace.data.object.Platform;
import edu.colorado.cires.pace.data.object.Project;
import edu.colorado.cires.pace.data.object.Sea;
import edu.colorado.cires.pace.data.object.Sensor;
import edu.colorado.cires.pace.data.object.Ship;
import edu.colorado.cires.pace.data.translator.Translator;
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
import edu.colorado.cires.pace.translator.DatasetType;
import edu.colorado.cires.pace.translator.LocationType;
import edu.colorado.cires.pace.translator.converter.PackageConverter;
import edu.colorado.cires.pace.utilities.SerializationUtils;
import java.util.List;

public class DataPanelFactory {

  private final ObjectMapper objectMapper = SerializationUtils.createObjectMapper();
  
  private final ProjectRepository projectRepository;
  private final PersonRepository personRepository;
  private final OrganizationRepository organizationRepository;
  private final PlatformRepository platformRepository;
  private final InstrumentRepository instrumentRepository;
  private final SensorRepository sensorRepository;
  private final DetectionTypeRepository detectionTypeRepository;
  private final SeaRepository seaRepository;
  private final ShipRepository shipRepository;
  private final FileTypeRepository fileTypeRepository;
  private final TranslatorRepository translatorRepository;
  private final PackageRepository packageRepository;

  DataPanelFactory(ProjectRepository projectRepository, PersonRepository personRepository, OrganizationRepository organizationRepository,
      PlatformRepository platformRepository, InstrumentRepository instrumentRepository, SensorRepository sensorRepository,
      DetectionTypeRepository detectionTypeRepository, SeaRepository seaRepository, ShipRepository shipRepository,
      FileTypeRepository fileTypeRepository, TranslatorRepository translatorRepository, PackageRepository packageRepository) {
    this.projectRepository = projectRepository;
    this.personRepository = personRepository;
    this.organizationRepository = organizationRepository;
    this.platformRepository = platformRepository;
    this.instrumentRepository = instrumentRepository;
    this.sensorRepository = sensorRepository;
    this.detectionTypeRepository = detectionTypeRepository;
    this.seaRepository = seaRepository;
    this.shipRepository = shipRepository;
    this.fileTypeRepository = fileTypeRepository;
    this.translatorRepository = translatorRepository;
    this.packageRepository = packageRepository;
  }

  public DataPanel<Project> createProjectsPanel() {
    ProjectsPanel panel = new ProjectsPanel(projectRepository, translatorRepository);
    panel.init();
    return panel;
  }
  
  public DataPanel<Platform> createPlatformPanel() {
    PlatformsPanel panel = new PlatformsPanel(
        platformRepository, translatorRepository
    );
    panel.init();
    return panel;
  }
  
  public DataPanel<FileType> createFileTypesPanel() {
    FileTypesPanel panel = new FileTypesPanel(
        fileTypeRepository, translatorRepository
    );
    panel.init();
    return panel;
  }
  
  public DataPanel<Person> createPeoplePanel() {
    PeoplePanel panel = new PeoplePanel(personRepository, translatorRepository);
    panel.init();
    return panel;
  }
  
  public DataPanel<Organization> createOrganizationsPanel() {
    OrganizationsPanel panel = new OrganizationsPanel(
        organizationRepository, translatorRepository
    );
    panel.init();
    return panel;
  }
  
  public DataPanel<Package> createPackagesPanel() {
    PackagesPanel panel = new PackagesPanel(
        packageRepository,
        new String[] { "UUID", "Site Or Cruise Name", "Deployment ID", "Projects", "Dataset Type", "Location Type", "Select for Packaging", "Visible", "Object" },
        (p) -> new Object[] { 
            p.getUuid(),
            p.getSiteOrCruiseName(),
            p.getDeploymentId(),
            String.join(", ", p.getProjects().toArray(String[]::new)),
            DatasetType.fromPackage(p).getName(),
            LocationType.fromLocationDetail(p.getLocationDetail()).getName(),
            false,
            p.isVisible(),
            p
        },
        Package.class,
        objectMapper,
        translatorRepository,
        new PackageConverter(
        ),
        personRepository,
        organizationRepository,
        projectRepository
    ) {
      @Override
      protected List<String> getHiddenColumns() {
        return List.of("UUID", "Object", "Visible");
      }
    };
    panel.init();
    return panel;
  }

  public DataPanel<Translator> createTranslatorsPanel() {
    TranslatorPanel panel = new TranslatorPanel(
        translatorRepository,
        new String[] { "UUID", "Name", "Object", "Visible" },
        (t) -> new Object[] { t.getUuid(), t.getName(), t, t.isVisible() },
        (o) -> (Translator) o[2],
        TranslatorForm::create
    ) {
      @Override
      protected List<String> getHiddenColumns() {
        return List.of("UUID", "Object", "Visible");
      }
    };
    panel.init();
    return panel;
  }
  
  public DataPanel<Instrument> createInstrumentsPanel() {
    InstrumentsPanel panel = new InstrumentsPanel(
        instrumentRepository, fileTypeRepository, translatorRepository
    );
    panel.init();
    return panel;
  }
  
  public DataPanel<Sensor> createSensorsPanel() {
    SensorsPanel panel = new SensorsPanel(
        sensorRepository, translatorRepository
    ); 
    panel.init();
    return panel; 
  }

  public DataPanel<Sea> createSeaAreasPanel() {
    SeaAreasPanel panel = new SeaAreasPanel(
        seaRepository, translatorRepository
    );
    panel.init();
    return panel;
  }

  public DataPanel<DetectionType> createDetectionTypesPanel() {
    DetectionTypesPanel panel = new DetectionTypesPanel(
        detectionTypeRepository, translatorRepository
    );
    panel.init();
    return panel;
  }
  
  public DataPanel<Ship> createShipsPanel() {
    ShipsPanel panel = new ShipsPanel(
        shipRepository, translatorRepository
    );
    panel.init();
    return panel;
  }
}
