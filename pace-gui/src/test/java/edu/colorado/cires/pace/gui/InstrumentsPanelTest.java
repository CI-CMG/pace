package edu.colorado.cires.pace.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.data.object.FileType;
import edu.colorado.cires.pace.data.object.Instrument;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

class InstrumentsPanelTest extends MetadataPanelTest<Instrument> {
  
  private final File fileTypesJsonFile = testPath.resolve("test-metadata")
      .resolve("file-types.json")
      .toFile();
  
  private final List<FileType> fileTypes = List.of(
      FileType.builder()
          .uuid(UUID.randomUUID())
          .type("ft-1")
          .comment("comment-1")
          .build(),
      FileType.builder()
          .uuid(UUID.randomUUID())
          .type("ft-2")
          .comment("comment-2")
          .build(),
      FileType.builder()
          .uuid(UUID.randomUUID())
          .type("ft-3")
          .comment("comment-3")
          .build()
  );
  
  @BeforeEach
  public void beforeEach() throws IOException {
    Path metadataDirectory = testPath.resolve("test-metadata");
    if (!testPath.toFile().exists()) {
      FileUtils.forceMkdir(testPath.toFile());
      FileUtils.forceMkdir(metadataDirectory.toFile());
    }
    FileUtils.deleteQuietly(fileTypesJsonFile);
    Files.createFile(fileTypesJsonFile.toPath());
    objectMapper.writeValue(fileTypesJsonFile, fileTypes);
    super.beforeEach();

  }
  
  @AfterEach
  public void afterEach() throws IOException {
    super.afterEach();
    
    FileUtils.deleteQuietly(fileTypesJsonFile);
  }

  @Override
  protected String getJsonFileName() {
    return "instruments.json";
  }

  @Override
  protected String getMetadataTabTitle() {
    return "Instruments";
  }

  @Override
  protected String getMetadataPanelName() {
    return "instrumentsPanel";
  }

  @Override
  protected String getFormPanelName() {
    return "instrumentForm";
  }

  @Override
  protected void fillOutForm(JPanelFixture formFixture, Instrument object) {
    enterFieldText(formFixture, "name", object.getName());
    
    JPanelFixture fileTypeListingsPanel = getPanel(formFixture, "fileTypeListingsPanel");
    Arrays.stream(fileTypeListingsPanel.target().getComponents())
        .filter(c -> c instanceof InstrumentFileTypePanel)
        .forEach(
        c -> {
          JPanelFixture fileTypeListingPanel = getPanel(fileTypeListingsPanel, c.getName());
          JButtonFixture buttonFixture = getButton(fileTypeListingPanel, "Remove");
          buttonFixture.click();
        }
    );
    
    JPanelFixture controlPanel = getPanel(formFixture, "controlPanel");
    JButtonFixture buttonFixture = getButton(controlPanel, "Add File Type");

    for (int i = 0; i < object.getFileTypes().size(); i++) {
      buttonFixture.click();

      List<InstrumentFileTypePanel> panels = Arrays.stream(fileTypeListingsPanel.target().getComponents())
          .filter(c -> c instanceof InstrumentFileTypePanel)
          .map(c -> (InstrumentFileTypePanel) c)
          .toList();
      InstrumentFileTypePanel panel = panels.get(panels.size() - 1);

      JPanelFixture fileTypeListingPanel = fileTypeListingsPanel.panel(new GenericTypeMatcher<>(InstrumentFileTypePanel.class) {

        @Override
        protected boolean isMatching(InstrumentFileTypePanel component) {
          return component == panel;
        }
      });
      
      fileTypeListingPanel.comboBox()
          .requireItemCount(fileTypes.size())
          .requireNoSelection()
          .requireVisible()
          .requireEnabled()
          .selectItem(object.getFileTypes().get(i).getType());
    }
  }

  @Override
  protected Instrument createObject(String uniqueField) {
    return Instrument.builder()
        .name(uniqueField)
        .fileTypes(fileTypes)
        .build();
  }

  @Override
  protected String[] objectToRow(Instrument object) {
    return new String[] {
        object.getName(),
        object.getFileTypes().stream()
            .map(FileType::getType)
            .collect(Collectors.joining(", "))
    };
  }

  @Override
  protected String[] objectToSpreadsheetRow(Instrument object) {
    return new String[] {
      object.getName(),
      object.getFileTypes().stream()
          .map(FileType::getType)
          .collect(Collectors.joining(";"))
    };
  }

  @Override
  protected void requireFormContents(JPanelFixture formFixture, Instrument object) {
    requireFieldText(formFixture, "name", object.getName());

    JPanelFixture fileTypeListingsPanel = getPanel(formFixture, "fileTypeListingsPanel");
    object.getFileTypes().forEach(
        fileType -> {
          JPanelFixture fileTypeListingPanel = getPanel(fileTypeListingsPanel, fileType.getType());
          getButton(fileTypeListingPanel, "Remove").requireEnabled();
          
          fileTypeListingPanel.comboBox()
              .requireEnabled()
              .requireVisible()
              .requireSelection(fileType.getType());
        }
    );
  }

  @Override
  protected Instrument updateObject(Instrument original, String uniqueField) {
    List<FileType> newFileTypes = List.of(
        fileTypes.get(0), fileTypes.get(2)
    );
    return original.toBuilder()
        .name(uniqueField)
        .fileTypes(newFileTypes)
        .build();
  }

  @Override
  protected String getUniqueField(Instrument object) {
    return object.getName();
  }

  @Override
  protected void assertObjectsEqual(Instrument expected, Instrument actual, boolean checkUUID) {
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    } else {
      assertNotNull(actual.getUuid());
    }
    
    assertEquals(expected.getName(), actual.getName());
    
    List<FileType> expectedFileTypes = expected.getFileTypes().stream()
        .sorted(Comparator.comparing(FileType::getType))
        .toList();
    List<FileType> actualFileTypes = actual.getFileTypes().stream()
        .sorted(Comparator.comparing(FileType::getType))
        .toList();
    
    assertEquals(expectedFileTypes.size(), actualFileTypes.size());

    for (int i = 0; i < expectedFileTypes.size(); i++) {
      FileTypesPanelTest.assertFileTypesEquals(
          expectedFileTypes.get(i),
          actualFileTypes.get(i),
          true
      );
    }
  }

  @Override
  protected TypeReference<List<Instrument>> getTypeReference() {
    return new TypeReference<>() {};
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }

  @Override
  protected Class<Instrument> getObjectClass() {
    return Instrument.class;
  }

  @Override
  protected String[] getSpreadsheetHeaders() {
    return new String[] {
        "UUID", "Name", "File Types"
    };
  }

  @Override
  protected void fillOutTranslatorForm(JPanelFixture panelFixture) {
    selectComboBoxOption(panelFixture, "translatorType", "Instrument", 11);
    
    JPanelFixture formFixture = getPanel(panelFixture, "instrumentTranslatorForm");
    selectComboBoxOption(formFixture, "uuid", "UUID", 3);
    selectComboBoxOption(formFixture, "name", "Name", 3);
    selectComboBoxOption(formFixture, "fileTypes", "File Types", 3);
  }
}
