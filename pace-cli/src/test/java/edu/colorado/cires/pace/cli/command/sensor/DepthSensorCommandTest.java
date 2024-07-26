package edu.colorado.cires.pace.cli.command.sensor;

import edu.colorado.cires.pace.data.object.sensor.depth.DepthSensor;
import edu.colorado.cires.pace.data.object.sensor.depth.translator.DepthSensorTranslator;
import java.util.UUID;

class DepthSensorCommandTest extends SensorCommandTest<DepthSensor, DepthSensorTranslator> {

  @Override
  public DepthSensor createObject(String uniqueField, boolean withUUID) {
    return DepthSensor.builder()
        .uuid(withUUID ? UUID.randomUUID() : null)
        .name(uniqueField)
        .description("description")
        .build();
  }

  @Override
  protected DepthSensor updateObject(DepthSensor original, String uniqueField) {
    return original.toBuilder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected void assertSensorTypeSpecificFields(DepthSensor expected, DepthSensor actual) {
    
  }

  @Override
  protected String[] getTranslatorFields() {
    return new String[] {
        "UUID",
        "sensorName",
        "description"
    };
  }

  @Override
  protected DepthSensorTranslator createTranslator(String name) {
    return DepthSensorTranslator.builder()
        .name(name)
        .sensorUUID("UUID")
        .sensorName("sensorName")
        .description("description")
        .build();
  }

  @Override
  protected String[] objectToRow(DepthSensor object) {
    return new String[] {
        object.getUuid() == null ? "" : object.getUuid().toString(),
        object.getName(),
        object.getDescription()
    };
  }
}
