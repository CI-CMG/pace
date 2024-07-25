package edu.colorado.cires.pace.cli.command.sensor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.OtherSensor;
import edu.colorado.cires.pace.data.translator.OtherSensorTranslator;
import java.util.UUID;

class OtherSensorCommandTest extends SensorCommandTest<OtherSensor, OtherSensorTranslator> {

  @Override
  protected void assertSensorTypeSpecificFields(OtherSensor expected, OtherSensor actual) {
    assertEquals(expected.getSensorType(), actual.getSensorType());
    assertEquals(expected.getProperties(), actual.getProperties());
  }

  @Override
  public OtherSensor createObject(String uniqueField, boolean withUUID) {
    return OtherSensor.builder()
        .uuid(withUUID ? UUID.randomUUID() : null)
        .name(uniqueField)
        .sensorType("sensorType")
        .properties("properties")
        .description("description")
        .build();
  }

  @Override
  protected OtherSensor updateObject(OtherSensor original, String uniqueField) {
    return original.toBuilder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected String[] getTranslatorFields() {
    return new String[] {
        "UUID",
        "sensorName",
        "description",
        "sensorType",
        "properties"
    };
  }

  @Override
  protected OtherSensorTranslator createTranslator(String name) {
    return OtherSensorTranslator.builder()
        .name(name)
        .sensorUUID("UUID")
        .sensorName("sensorName")
        .description("description")
        .sensorType("sensorType")
        .properties("properties")
        .build();
  }

  @Override
  protected String[] objectToRow(OtherSensor object) {
    return new String[] {
        object.getUuid() == null ? "" : object.getUuid().toString(),
        object.getName(),
        object.getDescription(),
        object.getSensorType(),
        object.getProperties()
    };
  }
}
