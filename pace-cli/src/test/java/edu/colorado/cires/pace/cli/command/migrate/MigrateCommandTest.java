package edu.colorado.cires.pace.cli.command.migrate;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.command.CLITest;
import edu.colorado.cires.pace.cli.error.ExecutionErrorHandler.CLIError;
import edu.colorado.cires.pace.data.object.detectionType.DetectionType;
import edu.colorado.cires.pace.data.object.fileType.FileType;
import edu.colorado.cires.pace.data.object.instrument.Instrument;
import edu.colorado.cires.pace.data.object.base.ObjectWithUniqueField;
import edu.colorado.cires.pace.data.object.contact.organization.Organization;
import edu.colorado.cires.pace.data.object.contact.person.Person;
import edu.colorado.cires.pace.data.object.platform.Platform;
import edu.colorado.cires.pace.data.object.project.Project;
import edu.colorado.cires.pace.data.object.sea.Sea;
import edu.colorado.cires.pace.data.object.ship.Ship;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

class MigrateCommandTest extends CLITest {

  @Test
  void testMigrate() throws IOException {
    File sourceData = Paths.get("src/test/resources")
            .resolve("sourceData.sqlite").toFile();
    File localData = sourceData.toPath().getParent()
        .resolve("localData.sqlite").toFile();
    
    execute("migrate", sourceData.toString(), localData.toString());
    clearOut();
    
    assertSavedObjects(
        new File("src/test/resources/organizations.csv"),
        1,
        listObjects("organization", new TypeReference<>() {}),
        Organization::getName,
        (organization, record) -> {
          assertEquals(organization.getUuid().toString(), record.get(9), record.get(0));
          assertEquals(organization.getName(), StringUtils.isBlank(record.get(1)) ? null : record.get(1));
          assertEquals(organization.getStreet(), StringUtils.isBlank(record.get(2)) ? null : record.get(2));
          assertEquals(organization.getCity(), StringUtils.isBlank(record.get(3)) ? null : record.get(3));
          assertEquals(organization.getState(), StringUtils.isBlank(record.get(4)) ? null : record.get(4));
          assertEquals(organization.getZip(), StringUtils.isBlank(record.get(5)) ? null : record.get(5));
          assertEquals(organization.getCountry(), StringUtils.isBlank(record.get(6)) ? null : record.get(6));
          assertEquals(organization.getEmail(), StringUtils.isBlank(record.get(7)) ? null : record.get(7));
          assertEquals(organization.getPhone(), StringUtils.isBlank(record.get(8)) ? null : record.get(8));
        } 
    );
    
    assertSavedObjects(
        new File("src/test/resources/people.csv"),
        1,
        listObjects("person", new TypeReference<>() {}),
        Person::getName,
        (person, record) -> {
          assertEquals(person.getUuid().toString(), record.get(11));
          assertEquals(person.getName(), record.get(1));
          assertEquals(person.getOrganization(), StringUtils.isBlank(record.get(2)) ? null : record.get(2));
          assertEquals(person.getPosition(), StringUtils.isBlank(record.get(3)) ? null : record.get(3));
          assertEquals(person.getStreet(), StringUtils.isBlank(record.get(4)) ? null : record.get(4));
          assertEquals(person.getCity(), StringUtils.isBlank(record.get(5)) ? null : record.get(5));
          assertEquals(person.getState(), StringUtils.isBlank(record.get(6)) ? null : record.get(6));
          assertEquals(person.getZip(), StringUtils.isBlank(record.get(7)) ? null : record.get(7));
          assertEquals(person.getCountry(), StringUtils.isBlank(record.get(8)) ? null : record.get(8));
          assertEquals(person.getEmail(), StringUtils.isBlank(record.get(9)) ? null : record.get(9));
          assertEquals(person.getPhone(), StringUtils.isBlank(record.get(10)) ? null : record.get(10));
          assertEquals(person.getOrcid(), StringUtils.isBlank(record.get(13)) ? null : record.get(13));
        }
    );
    
    assertSavedObjects(
        new File("src/test/resources/projects.csv"),
        0,
        listObjects("project", new TypeReference<>() {}),
        Project::getName,
        (project, record) -> assertEquals(project.getName(), record.get(0))
    );
    
    assertSavedObjects(
        new File("src/test/resources/detectionTypes.csv"),
        0,
        listObjects("detection-type", new TypeReference<>() {}),
        DetectionType::getSource,
        (detectionType, record) -> {
          assertEquals(detectionType.getSource(), record.get(0));
          assertEquals(detectionType.getScienceName(), StringUtils.isBlank(record.get(1)) ? null : record.get(1));
        }
    );
    
    assertSavedObjects(
        new File("src/test/resources/fileTypes.csv"),
        0,
        listObjects("file-type", new TypeReference<>() {}),
        FileType::getType,
        (fileType, record) -> {
          assertEquals(fileType.getType(), record.get(0));
          assertEquals(fileType.getComment(), StringUtils.isBlank(record.get(1)) ? null : record.get(1));
        }
    );
    
    assertSavedObjects(
        new File("src/test/resources/instruments.csv"),
        0,
        listObjects("instrument", new TypeReference<>() {}),
        Instrument::getName,
        (instrument, record) -> {
          assertEquals(instrument.getName(), record.get(0));
          assertEquals(
              String.join(",", instrument.getFileTypes()), 
              record.get(1)
          );
        }
    );
    
    assertSavedObjects(
        new File("src/test/resources/platforms.csv"),
        0,
        listObjects("platform", new TypeReference<>() {}),
        Platform::getName,
        (platform, record) -> assertEquals(platform.getName(), record.get(0))
    );
    
    assertSavedObjects(
        new File("src/test/resources/seas.csv"),
        1,
        listObjects("sea", new TypeReference<>() {}),
        Sea::getName,
        (sea, record) -> assertEquals(sea.getName(), record.get(1))
    );
    
    assertSavedObjects(
        new File("src/test/resources/ships.csv"),
        0,
        listObjects("ship", new TypeReference<>() {}),
        Ship::getName,
        (ship, record) -> assertEquals(ship.getName(), record.get(0))
    );
  }

  @Test
  void testMigrationErrors() throws IOException {
    File sourceData = Paths.get("src/test/resources")
        .resolve("sourceData.sqlite").toFile();
    File localData = sourceData.toPath().getParent()
        .resolve("invalidLocalData.sqlite").toFile();

    execute("migrate", sourceData.toString(), localData.toString());

    CLIError exception = getCLIException();
    assertEquals("Migration failed", exception.message());
    ArrayList<String> violations = (ArrayList<String>) exception.detail();
    assertEquals(40, violations.size());
    violations.forEach(
        v -> assertEquals("Organization validation failed (name: must not be blank)", v)
    );
    
    clearOut();

    assertSavedObjects(
        new File("src/test/resources/organizations2.csv"),
        1,
        listObjects("organization", new TypeReference<>() {}),
        Organization::getName,
        (organization, record) -> {
          assertEquals(organization.getUuid().toString(), record.get(9), record.get(0));
          assertEquals(organization.getName(), StringUtils.isBlank(record.get(1)) ? null : record.get(1));
          assertEquals(organization.getStreet(), StringUtils.isBlank(record.get(2)) ? null : record.get(2));
          assertEquals(organization.getCity(), StringUtils.isBlank(record.get(3)) ? null : record.get(3));
          assertEquals(organization.getState(), StringUtils.isBlank(record.get(4)) ? null : record.get(4));
          assertEquals(organization.getZip(), StringUtils.isBlank(record.get(5)) ? null : record.get(5));
          assertEquals(organization.getCountry(), StringUtils.isBlank(record.get(6)) ? null : record.get(6));
          assertEquals(organization.getEmail(), StringUtils.isBlank(record.get(7)) ? null : record.get(7));
          assertEquals(organization.getPhone(), StringUtils.isBlank(record.get(8)) ? null : record.get(8));
        }
    );

    assertSavedObjects(
        new File("src/test/resources/people.csv"),
        1,
        listObjects("person", new TypeReference<>() {}),
        Person::getName,
        (person, record) -> {
          assertEquals(person.getUuid().toString(), record.get(11));
          assertEquals(person.getName(), record.get(1));
          assertEquals(person.getOrganization(), StringUtils.isBlank(record.get(2)) ? null : record.get(2));
          assertEquals(person.getPosition(), StringUtils.isBlank(record.get(3)) ? null : record.get(3));
          assertEquals(person.getStreet(), StringUtils.isBlank(record.get(4)) ? null : record.get(4));
          assertEquals(person.getCity(), StringUtils.isBlank(record.get(5)) ? null : record.get(5));
          assertEquals(person.getState(), StringUtils.isBlank(record.get(6)) ? null : record.get(6));
          assertEquals(person.getZip(), StringUtils.isBlank(record.get(7)) ? null : record.get(7));
          assertEquals(person.getCountry(), StringUtils.isBlank(record.get(8)) ? null : record.get(8));
          assertEquals(person.getEmail(), StringUtils.isBlank(record.get(9)) ? null : record.get(9));
          assertEquals(person.getPhone(), StringUtils.isBlank(record.get(10)) ? null : record.get(10));
          assertEquals(person.getOrcid(), StringUtils.isBlank(record.get(13)) ? null : record.get(13));
        }
    );

    assertSavedObjects(
        new File("src/test/resources/projects.csv"),
        0,
        listObjects("project", new TypeReference<>() {}),
        Project::getName,
        (project, record) -> assertEquals(project.getName(), record.get(0))
    );

    assertSavedObjects(
        new File("src/test/resources/detectionTypes.csv"),
        0,
        listObjects("detection-type", new TypeReference<>() {}),
        DetectionType::getSource,
        (detectionType, record) -> {
          assertEquals(detectionType.getSource(), record.get(0));
          assertEquals(detectionType.getScienceName(), StringUtils.isBlank(record.get(1)) ? null : record.get(1));
        }
    );

    assertSavedObjects(
        new File("src/test/resources/fileTypes.csv"),
        0,
        listObjects("file-type", new TypeReference<>() {}),
        FileType::getType,
        (fileType, record) -> {
          assertEquals(fileType.getType(), record.get(0));
          assertEquals(fileType.getComment(), StringUtils.isBlank(record.get(1)) ? null : record.get(1));
        }
    );

    assertSavedObjects(
        new File("src/test/resources/instruments.csv"),
        0,
        listObjects("instrument", new TypeReference<>() {}),
        Instrument::getName,
        (instrument, record) -> {
          assertEquals(instrument.getName(), record.get(0));
          assertEquals(
              String.join(",", instrument.getFileTypes()),
              record.get(1)
          );
        }
    );

    assertSavedObjects(
        new File("src/test/resources/platforms.csv"),
        0,
        listObjects("platform", new TypeReference<>() {}),
        Platform::getName,
        (platform, record) -> assertEquals(platform.getName(), record.get(0))
    );

    assertSavedObjects(
        new File("src/test/resources/seas.csv"),
        1,
        listObjects("sea", new TypeReference<>() {}),
        Sea::getName,
        (sea, record) -> assertEquals(sea.getName(), record.get(1))
    );

    assertSavedObjects(
        new File("src/test/resources/ships.csv"),
        0,
        listObjects("ship", new TypeReference<>() {}),
        Ship::getName,
        (ship, record) -> assertEquals(ship.getName(), record.get(0))
    );
  }
  
  private <O extends ObjectWithUniqueField> List<O> listObjects(String commandPrefix, TypeReference<List<O>> typeReference) throws JsonProcessingException {
    execute(commandPrefix, "list");
    
    List<O> objects = objectMapper.readValue(
        getCommandOutput(),
        typeReference
    );
    
    clearOut();
    
    return objects;
  } 
  
  private <O extends ObjectWithUniqueField> void assertSavedObjects(File expectedFile, int uniqueFieldColumn, List<O> objects, Function<O, String> uniqueFieldGetter, ObjectRecordComparator<O> comparator) throws IOException {
    try (InputStream inputStream = new FileInputStream(expectedFile); Reader reader = new InputStreamReader(inputStream)) {
      CSVFormat format = CSVFormat.DEFAULT.builder().build();
      CSVParser parser = format.parse(reader);
      
      List<CSVRecord> records = parser.getRecords();
      assertEquals(objects.size(), records.size());

      for (CSVRecord record : records) {
        String name = record.get(uniqueFieldColumn);
        O object = objects.stream()
            .filter(o -> uniqueFieldGetter.apply(o).equals(name))
            .findFirst()
            .orElseThrow(
                () -> new IllegalStateException(String.format(
                    "%s not found", name
                ))
            );
        comparator.assertRecordRepresentsObject(object, record);
      }
    }
    
    clearOut();
  }
  
  @FunctionalInterface
  interface ObjectRecordComparator<O extends ObjectWithUniqueField> {
    void assertRecordRepresentsObject(O object, CSVRecord record);
  }

}