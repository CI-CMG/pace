package edu.colorado.cires.pace.cli.command.instrument;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.command.CommandTest;
import edu.colorado.cires.pace.cli.command.fileType.FileTypeCommandTest;
import edu.colorado.cires.pace.data.object.FileType;
import edu.colorado.cires.pace.data.object.Instrument;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

class InstrumentCommandTest extends CommandTest<Instrument> {
  
  private final FileTypeCommandTest fileTypeCommandTest = new FileTypeCommandTest();
  
  private final List<FileType> fileTypes = new ArrayList<>(0);

  @Override
  public Instrument createObject(String uniqueField) {
    return Instrument.builder()
        .name(uniqueField)
        .fileTypes(fileTypes)
        .build();
  }
  
  @BeforeEach
  public void beforeEach() throws IOException {
    super.beforeEach();
    fileTypes.addAll(List.of(
        fileTypeCommandTest.writeObject(FileType.builder()
                .type("type-1")
            .build()),
        fileTypeCommandTest.writeObject(FileType.builder()
                .type("type-2")
            .build())
    ));
    clearOut();
  }
  
  @AfterEach
  public void afterEach() {
    super.afterEach();
    fileTypes.clear();
  }

  @Override
  protected String getRepositoryFileName() {
    return "instruments.json";
  }

  @Override
  protected String getCommandPrefix() {
    return "instrument";
  }

  @Override
  protected TypeReference<List<Instrument>> getTypeReference() {
    return new TypeReference<>() {};
  }

  @Override
  protected Class<Instrument> getClazz() {
    return Instrument.class;
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }

  @Override
  protected void assertObjectsEqual(Instrument expected, Instrument actual, boolean checkUUID) {
    assertEquals(expected.getName(), actual.getName());
    for (int i = 0; i < expected.getFileTypes().size(); i++) {
      FileTypeCommandTest.assertFileTypesEqual(
          expected.getFileTypes().get(i),
          actual.getFileTypes().get(i),
          true
      );
    }
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    } else {
      assertNotNull(actual.getUuid());
    }
  }

  @Override
  protected String getUniqueField(Instrument object) {
    return object.getName();
  }

  @Override
  protected Instrument updateObject(Instrument original, String uniqueField) {
    return original.toBuilder()
        .name(uniqueField)
        .build();
  }
}