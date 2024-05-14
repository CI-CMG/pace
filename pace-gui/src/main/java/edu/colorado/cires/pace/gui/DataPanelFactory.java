package edu.colorado.cires.pace.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.Organization;
import edu.colorado.cires.pace.data.object.Person;
import edu.colorado.cires.pace.data.object.Project;
import edu.colorado.cires.pace.datastore.json.CSVTranslatorJsonDatastore;
import edu.colorado.cires.pace.datastore.json.ExcelTranslatorJsonDatastore;
import edu.colorado.cires.pace.datastore.json.OrganizationJsonDatastore;
import edu.colorado.cires.pace.datastore.json.PersonJsonDatastore;
import edu.colorado.cires.pace.datastore.json.ProjectJsonDatastore;
import edu.colorado.cires.pace.repository.CSVTranslatorRepository;
import edu.colorado.cires.pace.repository.ExcelTranslatorRepository;
import edu.colorado.cires.pace.repository.OrganizationRepository;
import edu.colorado.cires.pace.repository.PersonRepository;
import edu.colorado.cires.pace.repository.ProjectRepository;
import edu.colorado.cires.pace.utilities.ApplicationPropertyResolver;
import edu.colorado.cires.pace.utilities.SerializationUtils;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

final class DataPanelFactory {

  private static final Path workDir = new ApplicationPropertyResolver().getWorkDir();
  private static final ObjectMapper objectMapper = SerializationUtils.createObjectMapper();
  private static final ExcelTranslatorRepository excelTranslatorRepository;
  private static final CSVTranslatorRepository csvTranslatorRepository;

  static {
    try {
      excelTranslatorRepository = new ExcelTranslatorRepository(
          new ExcelTranslatorJsonDatastore(workDir, objectMapper)
      );
      csvTranslatorRepository = new CSVTranslatorRepository(
          new CSVTranslatorJsonDatastore(workDir, objectMapper)
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static DataPanel<Project> createProjectsPanel() throws IOException {
    return new DataPanel<>(new ProjectRepository(
        new ProjectJsonDatastore(
            workDir, objectMapper
        )
    ), excelTranslatorRepository, csvTranslatorRepository, new String[]{
        "UUID", "Name"
    }, (project -> new Object[] {
        project.getUuid(), project.getName()
    }), (o) -> Project.builder()
        .uuid((UUID) o[0])
        .name((String) o[1])
        .build(), ProjectForm::new, Project.class);
  }
  
  public static DataPanel<Person> createPeoplePanel() throws IOException {
    return new DataPanel<>(
        new PersonRepository(new PersonJsonDatastore(workDir, objectMapper)),
        excelTranslatorRepository,
        csvTranslatorRepository,
        new String[]{"UUID", "Name", "Organization", "Position", "Street", "City", "State", "Zip", "Country", "Email",
            "Phone", "Orcid"},
        (person) -> new Object[]{person.getUuid(), person.getName(), person.getOrganization(), person.getPosition(),
            person.getStreet(), person.getCity(), person.getState(), person.getZip(), person.getCountry(), person.getEmail(),
            person.getPhone(), person.getOrcid()},
        (objects) -> Person.builder()
            .uuid((UUID) objects[0])
            .name((String) objects[1])
            .organization((String) objects[2])
            .position((String) objects[3])
            .street((String) objects[4])
            .city((String) objects[5])
            .state((String) objects[6])
            .zip((String) objects[7])
            .country((String) objects[8])
            .email((String) objects[9])
            .phone((String) objects[10])
            .orcid((String) objects[11])
            .build(),
        PersonForm::new,
        Person.class
    );
  }
  
  public static DataPanel<Organization> createOrganizationsPanel() throws IOException {
    return new DataPanel<>(
        new OrganizationRepository(new OrganizationJsonDatastore(workDir, objectMapper)),
        excelTranslatorRepository,
        csvTranslatorRepository,
        new String[]{"UUID", "Name", "Street", "City", "State", "Zip", "Country", "Email", "Phone"},
        (person) -> new Object[]{person.getUuid(), person.getName(), person.getStreet(), person.getCity(),
            person.getState(), person.getZip(), person.getCountry(), person.getEmail(), person.getPhone()},
        (objects) -> Organization.builder()
            .uuid((UUID) objects[0])
            .name((String) objects[1])
            .street((String) objects[2])
            .city((String) objects[3])
            .state((String) objects[4])
            .zip((String) objects[5])
            .country((String) objects[6])
            .email((String) objects[7])
            .phone((String) objects[8])
            .build(),
        OrganizationForm::new,
        Organization.class
    );
  }

}
