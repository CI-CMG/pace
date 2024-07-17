package edu.colorado.cires.pace.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import edu.colorado.cires.pace.data.object.DepthSensor;
import edu.colorado.cires.pace.data.object.Position;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.fixture.JPanelFixture;
import org.assertj.swing.fixture.JTabbedPaneFixture;

class DepthSensorPanelTest extends SensorsPanelTest<DepthSensor> {

  @Override
  protected void fillOutForm(JPanelFixture formFixture, DepthSensor object) {
    if (object.getUuid() == null) {
      formFixture.comboBox()
          .requireVisible()
          .requireEnabled()
          .requireItemCount(3)
          .selectItem("depth");
    }
    
    enterFieldText(formFixture, "name", object.getName());
    enterFieldText(formFixture,  "description", object.getDescription());
    Position position = object.getPosition();
    enterFieldText(formFixture, "x", position.getX().toString());
    enterFieldText(formFixture, "y", position.getY().toString());
    enterFieldText(formFixture, "z", position.getZ().toString());
  }

  @Override
  protected DepthSensor createObject(String uniqueField) {
    return DepthSensor.builder()
        .name(uniqueField)
        .description("a")
        .position(Position.builder()
            .x(1f)
            .y(2f)
            .z(3f)
            .build())
        .build();
  }

  @Override
  protected void requireFormContents(JPanelFixture formFixture, DepthSensor object) {
    requireFieldText(formFixture, "uuid", object.getUuid().toString());
    requireFieldText(formFixture,  "name", object.getName());
    requireFieldText(formFixture, "description", object.getDescription());
    
    JPanelFixture positionForm = getPanel(formFixture, "positionForm");
    Position position = object.getPosition();
    requireFieldText(positionForm, "x", position.getX().toString());
    requireFieldText(positionForm, "y", position.getY().toString());
    requireFieldText(positionForm, "z", position.getZ().toString());
  }

  @Override
  protected DepthSensor updateObject(DepthSensor original, String uniqueField) {
    return original.toBuilder()
        .name(uniqueField)
        .description("z")
        .position(Position.builder()
            .x(4f)
            .y(5f)
            .z(6f)
            .build())
        .build();
  }

  @Override
  protected void assertObjectsEqual(DepthSensor expected, DepthSensor actual, boolean checkUUID) {
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    } else {
      assertNotNull(actual.getUuid());
    }
    
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getDescription(), actual.getDescription());

    Position expectedPosition = expected.getPosition();
    Position actualPosition = actual.getPosition();
    assertEquals(expectedPosition.getX(), actualPosition.getX());
    assertEquals(expectedPosition.getY(), actualPosition.getY());
    assertEquals(expectedPosition.getZ(), actualPosition.getZ());
  }

  @Override
  protected String[] getSpreadsheetHeaders() {
    return new String[] {
        "UUID", "Name", "Description", "Position (X)", "Position (Y)", "Position (Z)"
    };
  }

  @Override
  protected String[] objectToSpreadsheetRow(DepthSensor object) {
    return new String[] {
        object.getName(),
        object.getDescription(),
        object.getPosition().getX().toString(),
        object.getPosition().getY().toString(),
        object.getPosition().getZ().toString()
    };
  }

  @Override
  protected void fillOutTranslatorForm(JPanelFixture panelFixture) {
    selectComboBoxOption(panelFixture, "translatorType", "Sensor", 11);

    JPanelFixture formFixture = panelFixture.panel(new GenericTypeMatcher<>(SensorTranslatorForm.class) {
      @Override
      protected boolean isMatching(SensorTranslatorForm sensorTranslatorForm) {
        return true;
      }
    }).requireVisible();

    selectComboBoxOption(formFixture, "sensorType", "Depth", 3);

    JTabbedPaneFixture tabbedPaneFixture = formFixture.tabbedPane().requireVisible();
    tabbedPaneFixture.selectTab("2. Sensor Info");

    panelFixture.panel(new GenericTypeMatcher<>(DepthSensorTranslatorForm.class) {
      @Override
      protected boolean isMatching(DepthSensorTranslatorForm depthSensorTranslatorForm) {
        return true;
      }
    }).requireVisible();

    selectComboBoxOption(formFixture, "uuid", "UUID", 6);
    selectComboBoxOption(formFixture, "name", "Name", 6);
    selectComboBoxOption(formFixture, "description", "Description", 6);

    JPanelFixture positionFixture = panelFixture.panel(new GenericTypeMatcher<>(PositionTranslatorForm.class) {
      @Override
      protected boolean isMatching(PositionTranslatorForm positionTranslatorForm) {
        return true;
      }
    }).requireVisible();

    selectComboBoxOption(positionFixture, "x", "Position (X)", 6);
    selectComboBoxOption(positionFixture, "y", "Position (Y)", 6);
    selectComboBoxOption(positionFixture, "z", "Position (Z)", 6);
  }
}
