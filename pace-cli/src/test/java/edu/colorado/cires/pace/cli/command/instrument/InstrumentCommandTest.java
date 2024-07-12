package edu.colorado.cires.pace.cli.command.instrument;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.command.TranslateCommandTest;
import edu.colorado.cires.pace.cli.command.fileType.FileTypeCommandTest;
import edu.colorado.cires.pace.data.object.FileType;
import edu.colorado.cires.pace.data.object.Instrument;
import edu.colorado.cires.pace.data.translator.InstrumentTranslator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class InstrumentCommandTest extends TranslateCommandTest<Instrument, InstrumentTranslator> {
  
  private final FileTypeCommandTest fileTypeCommandTest = new FileTypeCommandTest();
  
  private final List<FileType> fileTypes = new ArrayList<>(0);

  @Override
  public Instrument createObject(String uniqueField, boolean withUUID) {
    return Instrument.builder()
        .uuid(withUUID ? UUID.randomUUID() : null)
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
    assertInstrumentsEqual(expected, actual, checkUUID);
  }

  public static void assertInstrumentsEqual(Instrument expected, Instrument actual, boolean checkUUID) {
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

  @Override
  protected String[] getTranslatorFields() {
    return new String[] {
        "UUID",
        "instrumentName",
        "fileTypes"
    };
  }

  @Override
  protected InstrumentTranslator createTranslator(String name) {
    return InstrumentTranslator.builder()
        .name(name)
        .instrumentUUID("UUID")
        .instrumentName("instrumentName")
        .fileTypes("fileTypes")
        .build();
  }

  @Override
  protected String[] objectToRow(Instrument object) {
    return new String[] {
        object.getUuid() == null ? "" : object.getUuid().toString(),
        object.getName(),
        object.getFileTypes().stream()
            .map(FileType::getType)
            .collect(Collectors.joining(";"))
    };
  }
}