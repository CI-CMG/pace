package edu.colorado.cires.pace.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import edu.colorado.cires.pace.data.object.AudioSensor;
import edu.colorado.cires.pace.data.object.Position;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.fixture.JPanelFixture;
import org.assertj.swing.fixture.JTabbedPaneFixture;

class AudioSensorPanelTest extends SensorsPanelTest<AudioSensor> {

  @Override
  protected void fillOutForm(JPanelFixture formFixture, AudioSensor object) {
    if (object.getUuid() == null) {
      formFixture.comboBox()
          .requireVisible()
          .requireEnabled()
          .requireItemCount(3)
          .selectItem("audio");
    }
    
    enterFieldText(formFixture, "name", object.getName());
    enterFieldText(formFixture, "description", object.getDescription());
    enterFieldText(formFixture, "hydrophoneId", object.getHydrophoneId());
    enterFieldText(formFixture, "preampId", object.getPreampId());
    Position position = object.getPosition();
    enterFieldText(formFixture, "x", position.getX().toString());
    enterFieldText(formFixture, "y", position.getY().toString());
    enterFieldText(formFixture, "z", position.getZ().toString());
  }

  @Override
  protected AudioSensor createObject(String uniqueField) {
    return AudioSensor.builder()
        .name(uniqueField)
        .description("a")
        .position(Position.builder()
            .x(1f)
            .y(2f)
            .z(3f)
            .build())
        .hydrophoneId("b")
        .preampId("c")
        .build();
  }

  @Override
  protected void requireFormContents(JPanelFixture formFixture, AudioSensor object) {
    requireFieldText(formFixture, "name", object.getName());
    requireFieldText(formFixture, "description", object.getDescription());
    requireFieldText(formFixture, "hydrophoneId", object.getHydrophoneId());
    requireFieldText(formFixture, "preampId", object.getPreampId());
    Position position = object.getPosition();
    requireFieldText(formFixture, "x", position.getX().toString());
    requireFieldText(formFixture, "y", position.getY().toString());
    requireFieldText(formFixture, "z", position.getZ().toString());
  }

  @Override
  protected AudioSensor updateObject(AudioSensor original, String uniqueField) {
    return original.toBuilder()
        .name(uniqueField)
        .description("z")
        .preampId("x")
        .hydrophoneId("y")
        .position(Position.builder()
            .x(4f)
            .y(5f)
            .z(6f)
            .build())
        .build();
  }

  @Override
  protected void assertObjectsEqual(AudioSensor expected, AudioSensor actual, boolean checkUUID) {
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    } else {
      assertNotNull(actual.getUuid());
    }

    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getDescription(), actual.getDescription());
    assertEquals(expected.getHydrophoneId(), actual.getHydrophoneId());
    assertEquals(expected.getPreampId(), actual.getPreampId());

    Position expectedPosition = expected.getPosition();
    Position actualPosition = actual.getPosition();
    assertEquals(expectedPosition.getX(), actualPosition.getX());
    assertEquals(expectedPosition.getY(), actualPosition.getY());
    assertEquals(expectedPosition.getZ(), actualPosition.getZ());
  }

  @Override
  protected String[] getSpreadsheetHeaders() {
    return new String[] {
      "UUID", "Name", "Description", "Position (X)", "Position (Y)", "Position (Z)", "Hydrophone ID", "Preamp ID"  
    };
  }

  @Override
  protected String[] objectToSpreadsheetRow(AudioSensor object) {
    return new String[] {
        object.getName(),
        object.getDescription(),
        object.getPosition().getX().toString(),
        object.getPosition().getY().toString(),
        object.getPosition().getZ().toString(),
        object.getHydrophoneId(),
        object.getPreampId()
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

    selectComboBoxOption(formFixture, "sensorType", "Audio", 3);

    JTabbedPaneFixture tabbedPaneFixture = formFixture.tabbedPane().requireVisible();
    tabbedPaneFixture.selectTab("2. Sensor Info");

    JPanelFixture typeSpecificFormFixture = panelFixture.panel(new GenericTypeMatcher<>(AudioSensorTranslatorForm.class) {

      @Override
      protected boolean isMatching(AudioSensorTranslatorForm audioSensorTranslatorForm) {
        return true;
      }
    }).requireVisible();

    selectComboBoxOption(formFixture, "uuid", "UUID", 8);
    selectComboBoxOption(formFixture, "name", "Name", 8);
    selectComboBoxOption(formFixture, "description", "Description", 8);

    JPanelFixture positionFixture = panelFixture.panel(new GenericTypeMatcher<>(PositionTranslatorForm.class) {
      @Override
      protected boolean isMatching(PositionTranslatorForm positionTranslatorForm) {
        return true;
      }
    }).requireVisible();

    selectComboBoxOption(positionFixture, "x", "Position (X)", 8);
    selectComboBoxOption(positionFixture, "y", "Position (Y)", 8);
    selectComboBoxOption(positionFixture, "z", "Position (Z)", 8);

    selectComboBoxOption(typeSpecificFormFixture, "hydrophoneId", "Hydrophone ID", 8);
    selectComboBoxOption(typeSpecificFormFixture, "preampId", "Preamp ID", 8);
  }
}
