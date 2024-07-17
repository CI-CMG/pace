package edu.colorado.cires.pace.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.utilities.SerializationUtils;
import edu.colorado.cires.pace.utilities.TranslationType;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.swing.JButton;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FileUtils;
import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.Robot;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.AbstractContainerFixture;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JFileChooserFixture;
import org.assertj.swing.fixture.JOptionPaneFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.assertj.swing.fixture.JTabbedPaneFixture;
import org.assertj.swing.fixture.JTableCellFixture;
import org.assertj.swing.fixture.JTableFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.fixture.JToolBarFixture;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

abstract class MetadataPanelTest<O extends ObjectWithUniqueField> {

  protected final Path testPath = Paths.get("target").resolve("test-dir");
  protected final ObjectMapper objectMapper = SerializationUtils.createObjectMapper();

  protected FrameFixture window;

  protected abstract String getJsonFileName();
  protected abstract String getMetadataTabTitle();
  protected abstract String getMetadataPanelName();
  protected abstract String getFormPanelName();
  protected abstract void fillOutForm(JPanelFixture formFixture, O object);
  protected abstract O createObject(String uniqueField);
  protected abstract String[] objectToRow(O object);
  protected abstract void requireFormContents(JPanelFixture formFixture, O object);
  protected abstract O updateObject(O original, String uniqueField);
  protected abstract String getUniqueField(O object);
  protected abstract void assertObjectsEqual(O expected, O actual, boolean checkUUID);
  protected abstract TypeReference<List<O>> getTypeReference();
  protected abstract String getUniqueFieldName();
  protected abstract Class<O> getObjectClass();
  protected abstract String[] getSpreadsheetHeaders();
  
  private String createTranslator(File file) {
    selectTab(getTabbedPane("applicationTabs"), "Translators");
    JPanelFixture panelFixture = getPanel(window, "translatorsPanel");
    JTableFixture tableFixture = getTable(panelFixture, "dataTable");
    tableFixture.requireContents(new String[0][]);
    getButton(panelFixture, "Create")
        .requireEnabled()
        .click();

    DialogFixture dialogFixture = getDialog("translatorDialog")
        .requireVisible();

    panelFixture = getPanel(dialogFixture, "translatorForm");
    getButton(panelFixture, "Import Spreadsheet Headers")
        .requireEnabled()
        .click();

    JFileChooserFixture fileChooserFixture = panelFixture.fileChooser()
        .requireVisible();
    
    fileChooserFixture.fileNameTextBox().target().setText(file.toPath().toAbsolutePath().toString());

    fileChooserFixture
        .approveButton().click();

    fillOutTranslatorForm(panelFixture);

    String translatorName = "test-translator";

    enterFieldText(panelFixture, "name", translatorName);

    getButton(dialogFixture, "Save")
        .requireEnabled()
        .click();

    dialogFixture.requireNotVisible();

    tableFixture.requireContents(new String[][] {
        new String[] { translatorName }
    });

    return translatorName;
  }

  protected abstract void fillOutTranslatorForm(JPanelFixture panelFixture);

  @BeforeEach
  public void beforeEach() throws IOException {
    Path metadataDirectory = testPath.resolve("test-metadata");
    if (!testPath.toFile().exists()) {
      FileUtils.forceMkdir(testPath.toFile());
      FileUtils.forceMkdir(metadataDirectory.toFile());
    }
    objectMapper.writeValue(metadataDirectory.resolve(getJsonFileName()).toFile(), Collections.emptyList());
    objectMapper.writeValue(metadataDirectory.resolve("translators.json").toFile(), Collections.emptyList());

    Robot robot = BasicRobot.robotWithCurrentAwtHierarchy();
    Application application = GuiActionRunner.execute(() -> {
      Application app = new Application();
      app.createGUI();
      return app;
    });
    window = new FrameFixture(robot, application)
        .requireTitle("PACE")
        .requireSize(UIUtils.getPercentageOfWindowDimension(0.75, 0.65));
    window.show();
  }
  
  @AfterEach
  public void afterEach() throws IOException {
    window.cleanUp();
    FileUtils.deleteQuietly(testPath.toFile());

  }
  
  protected JTabbedPaneFixture getTabbedPane(String name) {
    return window.tabbedPane(name)
        .requireVisible();
  }
  
  protected void selectTab(JTabbedPaneFixture tabbedPaneFixture, String tabTitle) {
    tabbedPaneFixture.selectTab(tabTitle);
  }
  
  protected JPanelFixture getPanel(AbstractContainerFixture<?, ?, ?> fixture, String panelName) {
    return fixture.panel(panelName)
        .requireVisible();
  }
  
  protected JTextComponentFixture getTextFixture(AbstractContainerFixture<?, ?, ?> panelFixture, String name) {
    return panelFixture.textBox(name)
        .requireVisible();
  }
  
  protected void requireFieldText(JPanelFixture panelFixture, String name, String text) {
    getTextFixture(panelFixture, name)
        .requireText(text);
  }
  
  protected void enterFieldText(JPanelFixture panelFixture, String name, String text) {
    JTextComponentFixture textComponentFixture = getTextFixture(panelFixture, name).requireEnabled();
    textComponentFixture.target().setText(text);
    textComponentFixture.requireText(text);
  }
  
  protected void selectComboBoxOption(JPanelFixture panelFixture, String name, String itemToSelect, int expectedItemCount) {
    panelFixture.comboBox(name)
        .requireVisible()
        .requireEnabled()
        .requireItemCount(expectedItemCount)
        .selectItem(itemToSelect);
  }
  
  protected JButtonFixture getButton(AbstractContainerFixture<?, ?, ?> panelFixture, String buttonText) {
    return panelFixture.button(new GenericTypeMatcher<>(JButton.class) {
      @Override
      protected boolean isMatching(JButton component) {
        return component.getText().equals(buttonText);
      }
    }).requireVisible();
  }
  
  protected DialogFixture getDialog(String dialogName) {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    return window.dialog(dialogName)
        .requireSize(new Dimension(
            (int) (screenSize.width * 0.5),
            (int) (screenSize.height * 0.4)
        ))
        .requireModal();
  }
  
  protected JTableFixture getTable(JPanelFixture panelFixture, String name) {
    return panelFixture.table(name)
        .requireVisible();
  }
  
  private void requireTableContents(JTableFixture tableFixture, List<O> expectedObjects) {
    tableFixture.requireContents(
        expectedObjects.stream()
            .map(this::objectToRow)
            .toArray(String[][]::new)
    );
  }
  
  private JPanelFixture navigateToMetadataTab() {
    selectTab(getTabbedPane("applicationTabs"), "Metadata");
    selectTab(getTabbedPane("metadataTabs"), getMetadataTabTitle());
    return getPanel(window, getMetadataPanelName());
  }

  private O assertObjectSaved(O object, boolean checkUUID) throws IOException {
    File file = testPath.resolve("test-metadata")
        .resolve(getJsonFileName()).toFile();
    
    List<O> savedObjects = objectMapper.readValue(file, getTypeReference());
    O savedObject = savedObjects.stream()
      .filter(o -> getUniqueField(o).equals(getUniqueField(object)))
      .findFirst()
      .orElseThrow();
    assertObjectsEqual(object, savedObject, checkUUID);
    
    return savedObject;
  }
  
  private JOptionPaneFixture getOptionPane(String title, String message) {
    return window.optionPane()
        .requireTitle(title)
        .requireMessage(message);
  }

  private O createAndSaveObject(JPanelFixture panelFixture, String uniqueField, boolean expectError) throws IOException {
    JButtonFixture buttonFixture = getButton(panelFixture, "Create")
        .requireEnabled();
    buttonFixture.click();

    DialogFixture dialogFixture = getDialog("formDialog").requireVisible();
    JPanelFixture formPanelFixture = getPanel(dialogFixture, "formPanel");
    JPanelFixture formFixture = getPanel(formPanelFixture, getFormPanelName());
    
    O object = createObject(uniqueField);
    fillOutForm(formFixture, object);

    buttonFixture = getButton(formPanelFixture, "Save")
        .requireEnabled();
    buttonFixture.click();
    
    if (expectError) {
      dialogFixture.requireVisible();
      JOptionPaneFixture optionPaneFixture = getOptionPane("Error", String.format(
          "%s with %s = %s already exists", getObjectClass().getSimpleName(), getUniqueFieldName(), getUniqueField(object)
      )).requireErrorMessage();
      optionPaneFixture.okButton().click();
      dialogFixture.target().dispose();
      dialogFixture.requireNotVisible();
      return object;
    } else {
      dialogFixture.requireNotVisible();
      return assertObjectSaved(object, false);
    }
  }

  private O updateAndSaveObject(JTableFixture tableFixture, O object, String uniqueField, boolean expectError) throws IOException {
    getTableCell(tableFixture, getUniqueField(object))
        .doubleClick();

    DialogFixture dialogFixture = getDialog("formDialog").requireVisible();
    JPanelFixture formPanelFixture = getPanel(dialogFixture, "formPanel");
    JPanelFixture formFixture = getPanel(formPanelFixture, getFormPanelName());
    requireFormContents(formFixture, object);
    object = updateObject(object, uniqueField);
    fillOutForm(formFixture, object);

    JButtonFixture buttonFixture = getButton(formPanelFixture, "Save")
        .requireEnabled();
    buttonFixture.click();


    if (expectError) {
      dialogFixture.requireVisible();
      JOptionPaneFixture optionPaneFixture = getOptionPane("Error", String.format(
          "%s with %s = %s already exists", getObjectClass().getSimpleName(), getUniqueFieldName(), uniqueField
      )).requireErrorMessage();
      optionPaneFixture.okButton().click();
      dialogFixture.target().dispose();
      dialogFixture.requireNotVisible();
      return object;
    } else {
      dialogFixture.requireNotVisible();
      return assertObjectSaved(object, true);
    }
  }

  private void deleteObject(JTableFixture tableFixture, O object1) {
    getTableCell(tableFixture, getUniqueField(object1))
        .doubleClick();

    DialogFixture dialogFixture = getDialog("formDialog").requireVisible();
    JPanelFixture formPanelFixture = getPanel(dialogFixture, "formPanel");

    JButtonFixture buttonFixture = getButton(formPanelFixture, "Delete")
        .requireEnabled();
    buttonFixture.click();
    dialogFixture.requireNotVisible();
  }

  private void searchObjects(JPanelFixture panelFixture, String uniqueField) {
    JToolBarFixture toolBarFixture = panelFixture.toolBar()
        .requireVisible();
    
    JTextComponentFixture textField = getTextFixture(toolBarFixture, "searchField")
        .requireEnabled();
    textField.target().setText(uniqueField);
    textField.requireText(uniqueField);
    
    getButton(toolBarFixture, "Search")
        .requireEnabled()
        .click();
  }

  private void clearSearchParameters(JPanelFixture panelFixture) {
    JToolBarFixture toolBarFixture = panelFixture.toolBar()
        .requireVisible();
    
    getButton(toolBarFixture, "Clear")
        .requireEnabled()
        .click();
  }
  
  @Test
  void testCRUD() throws IOException {
    JPanelFixture metadataPanelFixture = navigateToMetadataTab();
    
    JTableFixture tableFixture = getTable(metadataPanelFixture, "dataTable");
    requireTableContents(tableFixture, Collections.emptyList());
    
    O object1 = createAndSaveObject(metadataPanelFixture, "A", false);
    requireTableContents(tableFixture, Collections.singletonList(object1));
    O object2 = createAndSaveObject(metadataPanelFixture, "B", false);
    requireTableContents(tableFixture, List.of(object1, object2));
    
    searchObjects(metadataPanelFixture, getUniqueField(object1));
    requireTableContents(tableFixture, Collections.singletonList(object1));
    clearSearchParameters(metadataPanelFixture);
    requireTableContents(tableFixture, List.of(object1, object2));
    
    createAndSaveObject(metadataPanelFixture, "A", true);
    requireTableContents(tableFixture, List.of(object1, object2));
    
    object1 = updateAndSaveObject(tableFixture, object1, "A-test", false);
    requireTableContents(tableFixture, List.of(object1, object2));
    
    updateAndSaveObject(tableFixture, object1, "B", true);
    requireTableContents(tableFixture, List.of(object1, object2));
    
    
    deleteObject(tableFixture, object1);
    requireTableContents(tableFixture, Collections.singletonList(object2));
  }
  
  @ParameterizedTest
  @EnumSource(TranslationType.class)
  void testTranslate(TranslationType translationType) throws IOException {
    File file = switch (translationType) {
      case excel -> writeExcel();
      case csv -> writeCSV();
    };

    String translatorName = createTranslator(file);

    JPanelFixture metadataPanelFixture = navigateToMetadataTab();

    JTableFixture tableFixture = getTable(metadataPanelFixture, "dataTable");
    requireTableContents(tableFixture, Collections.emptyList());
    
    getButton(metadataPanelFixture, "Translate")
        .requireEnabled()
        .click();
    
    DialogFixture dialogFixture = getDialog("translateDialog")
        .requireVisible();
    
    JPanelFixture panelFixture = getPanel(dialogFixture, "translateForm");
    JPanelFixture formFixture = getPanel(panelFixture, "translateFormPanel");
    formFixture.comboBox()
        .requireEnabled()
        .requireItemCount(1)
        .requireNoSelection()
        .selectItem(translatorName);
    
    getButton(formFixture, "Choose File")
        .requireEnabled()
        .click();
    
    JFileChooserFixture fileChooserFixture = window.fileChooser()
        .requireVisible();
    
    fileChooserFixture.fileNameTextBox().target().setText(file.toPath().toAbsolutePath().toString());
    
    fileChooserFixture
        .approveButton()
        .click(); 
    
    getButton(panelFixture, "Translate")
        .requireEnabled()
        .click();

    dialogFixture.close();
    dialogFixture.target().dispose();
    dialogFixture.requireNotVisible();

    Set<String> actual = Arrays.stream(tableFixture.contents())
        .map(s -> String.join(", ", s))
        .collect(Collectors.toSet());
    Set<String> expected = IntStream.range(1, 101).boxed()
        .map(i -> createObject(String.format("unique-field-%s", i)))
        .peek(o -> {
          try {
            assertObjectSaved(o, false);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        })
        .map(this::objectToRow)
        .map(s -> String.join(", ", s))
        .collect(Collectors.toSet());
    
    assertEquals(actual, expected);
  }

  private File writeExcel() throws IOException {
    File file = testPath.resolve("test.xlsx").toFile();
    
    try (OutputStream outputStream = new FileOutputStream(file); Workbook workbook = new Workbook(outputStream, "test", "1.0")) {
      Worksheet worksheet = workbook.newWorksheet("test");
      
      String[] headers = getSpreadsheetHeaders();
      for (int i = 0; i < headers.length; i++) {
        worksheet.value(0, i, headers[i]);
      }
      
      for (int i = 1; i < 101; i++) {
        O object = createObject(String.format("unique-field-%s", i));
        List<String> rowData = Arrays.stream(objectToSpreadsheetRow(object)).collect(Collectors.toList());
        rowData.add(0, null);
        for (int i1 = 0; i1 < rowData.size(); i1++) {
          worksheet.value(i, i1, rowData.get(i1));
        }
      }
    }
    
    return file;
  }
  
  private File writeCSV() throws IOException {
    File file = testPath.resolve("test.csv").toFile();
    
    CSVFormat format = CSVFormat.DEFAULT.builder().build();
    try (FileWriter fileWriter = new FileWriter(file); CSVPrinter printer = new CSVPrinter(fileWriter, format)) {
      printer.printRecord((Object[]) getSpreadsheetHeaders());
      
      for (int i = 1; i < 101; i++) {
        O object = createObject(String.format("unique-field-%s", i));
        List<String> rowData = Arrays.stream(objectToSpreadsheetRow(object)).collect(Collectors.toList());
        rowData.add(0, null);
        printer.printRecord(rowData);
      }
    }
    
    return file;
  }

  protected String[] objectToSpreadsheetRow(O object) {
    return objectToRow(object);
  }


  private JTableCellFixture getTableCell(JTableFixture tableFixture, String name) {
    return tableFixture.cell(name)
        .requireNotEditable();
  }

}
