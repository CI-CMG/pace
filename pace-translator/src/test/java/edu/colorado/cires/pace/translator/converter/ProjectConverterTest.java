package edu.colorado.cires.pace.translator.converter;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.pace.data.object.project.Project;
import edu.colorado.cires.pace.data.object.project.translator.ProjectTranslator;
import edu.colorado.cires.pace.translator.TranslationException;
import edu.colorado.cires.pace.translator.ValueWithColumnNumber;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ProjectConverterTest {
  
  private final Converter<ProjectTranslator, Project> converter = new ProjectConverter();

  @Test
  void convert() throws TranslationException {
    UUID uuid = UUID.randomUUID();
    String name = "name-value";
    
    Project project = converter.convert(
        ProjectTranslator.builder()
            .projectUUID("uuID")
            .projectName("NAME_")
            .build(),
        Map.of(
            "uuID", new ValueWithColumnNumber(Optional.of(uuid.toString()), 1),
            "NAME_", new ValueWithColumnNumber(Optional.of(name), 2)
        ),
        1,
        new RuntimeException()
    );
    
    assertEquals(uuid, project.getUuid());
    assertEquals(name, project.getName());
  }
}