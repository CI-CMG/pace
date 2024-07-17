package edu.colorado.cires.pace.gui;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.data.object.Position;
import edu.colorado.cires.pace.data.object.Sensor;
import java.util.List;

abstract class SensorsPanelTest<S extends Sensor> extends MetadataPanelTest<S> {
  
  

  @Override
  protected Class<S> getObjectClass() {
    return (Class<S>) Sensor.class;
  }

  @Override
  protected String getJsonFileName() {
    return "sensors.json";
  }

  @Override
  protected String getMetadataTabTitle() {
    return "Sensors";
  }

  @Override
  protected String getMetadataPanelName() {
    return "sensorsPanel";
  }

  @Override
  protected String getFormPanelName() {
    return "sensorForm";
  }

  @Override
  protected String getUniqueField(Sensor object) {
    return object.getName();
  }

  @Override
  protected TypeReference<List<S>> getTypeReference() {
    return new TypeReference<>() {};
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }

  @Override
  protected String[] objectToRow(S object) {
    Position position = object.getPosition();
    return new String[] {
        object.getName(), String.format(
            "(%s, %s, %s)", position.getX(), position.getY(), position.getZ()
    ), object.getDescription()
    };
  }
}
