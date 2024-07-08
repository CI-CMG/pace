package edu.colorado.cires.pace.cli.command.sensor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.OtherSensor;
import edu.colorado.cires.pace.data.object.Position;
import edu.colorado.cires.pace.data.translator.OtherSensorTranslator;
import edu.colorado.cires.pace.data.translator.PositionTranslator;

class OtherSensorCommandTest extends SensorCommandTest<OtherSensor, OtherSensorTranslator> {

  @Override
  protected void assertSensorTypeSpecificFields(OtherSensor expected, OtherSensor actual) {
    assertEquals(expected.getSensorType(), actual.getSensorType());
    assertEquals(expected.getProperties(), actual.getProperties());
  }

  @Override
  public OtherSensor createObject(String uniqueField) {
    return OtherSensor.builder()
        .name(uniqueField)
        .sensorType("sensorType")
        .properties("properties")
        .description("description")
        .position(Position.builder()
            .x(1f)
            .y(2f)
            .z(3f)
            .build())
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
        "sensorUUID",
        "sensorName",
        "description",
        "position (X)",
        "position (Y)",
        "position (Z)",
        "sensorType",
        "properties"
    };
  }

  @Override
  protected OtherSensorTranslator createTranslator(String name) {
    return OtherSensorTranslator.builder()
        .name(name)
        .sensorUUID("sensorUUID")
        .sensorName("sensorName")
        .description("description")
        .positionTranslator(PositionTranslator.builder()
            .x("position (X)")
            .y("position (Y)")
            .z("position (Z)")
            .build())
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
        object.getPosition().getX().toString(),
        object.getPosition().getY().toString(),
        object.getPosition().getZ().toString(),
        object.getSensorType(),
        object.getProperties()
    };
  }
}
