package edu.colorado.cires.pace.gui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.utilities.SerializationUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import javax.swing.JButton;
import org.apache.commons.io.FileUtils;
import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.Robot;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.AbstractContainerFixture;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JOptionPaneFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.assertj.swing.fixture.JTabbedPaneFixture;
import org.assertj.swing.fixture.JTableCellFixture;
import org.assertj.swing.fixture.JTableFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
  
  @BeforeEach
  public void beforeEach() throws IOException {
    Path metadataDirectory = testPath.resolve("test-metadata");
    if (!testPath.toFile().exists()) {
      FileUtils.forceMkdir(testPath.toFile());
      FileUtils.forceMkdir(metadataDirectory.toFile());
    }
    objectMapper.writeValue(metadataDirectory.resolve(getJsonFileName()).toFile(), Collections.emptyList());

    Robot robot = BasicRobot.robotWithCurrentAwtHierarchy();
    Application application = GuiActionRunner.execute(Application::new);
    window = new FrameFixture(robot, application)
        .requireTitle("PACE");
    window.show();
  }
  
  @AfterEach
  public void afterEach() throws IOException {
    window.cleanUp();
    FileUtils.deleteQuietly(testPath.toFile());

  }
  
  private JTabbedPaneFixture getTabbedPane(String name) {
    return window.tabbedPane(name)
        .requireVisible();
  }
  
  private void selectTab(JTabbedPaneFixture tabbedPaneFixture, String tabTitle) {
    tabbedPaneFixture.selectTab(tabTitle);
  }
  
  protected JPanelFixture getPanel(AbstractContainerFixture<?, ?, ?> fixture, String panelName) {
    return fixture.panel(panelName)
        .requireVisible();
  }
  
  protected JTextComponentFixture getTextFixture(JPanelFixture panelFixture, String name) {
    return panelFixture.textBox(name)
        .requireVisible();
  }
  
  protected void requireFieldText(JPanelFixture panelFixture, String name, String text) {
    getTextFixture(panelFixture, name)
        .requireText(text);
  }
  
  protected void enterFieldText(JPanelFixture panelFixture, String name, String text) {
    getTextFixture(panelFixture, name).requireEnabled()
        .enterText(text)
        .requireText(text);
  }
  
  protected JButtonFixture getButton(AbstractContainerFixture<?, ?, ?> panelFixture, String buttonText) {
    return panelFixture.button(new GenericTypeMatcher<>(JButton.class) {
      @Override
      protected boolean isMatching(JButton component) {
        return component.getText().equals(buttonText);
      }
    }).requireVisible();
  }
  
  private DialogFixture getDialog(String dialogName) {
    return window.dialog(dialogName);
  }
  
  private JTableFixture getTable(JPanelFixture panelFixture, String name) {
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
  
  @Test
  void testCRUD() throws IOException {
    JPanelFixture metadataPanelFixture = navigateToMetadataTab();
    
    JTableFixture tableFixture = getTable(metadataPanelFixture, "dataTable");
    requireTableContents(tableFixture, Collections.emptyList());
    
    O object1 = createAndSaveObject(metadataPanelFixture, "A", false);
    requireTableContents(tableFixture, Collections.singletonList(object1));
    O object2 = createAndSaveObject(metadataPanelFixture, "B", false);
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


  private JTableCellFixture getTableCell(JTableFixture tableFixture, String name) {
    return tableFixture.cell(name)
        .requireNotEditable();
  }

}
