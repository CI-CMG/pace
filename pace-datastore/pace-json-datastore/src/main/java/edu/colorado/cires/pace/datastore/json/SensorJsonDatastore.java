package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.sensor.base.Sensor;
import java.io.IOException;
import java.nio.file.Path;

/**
 * SensorJsonDatastore extends JsonDatastore and returns name
 * as the unique field name
 */
public class SensorJsonDatastore extends JsonDatastore<Sensor> {

  /**
   * Initializes the sensor JSON datastore
   *
   * @param storageDirectory location of datastore on machine
   * @param objectMapper relevant object mapper
   * @throws IOException in case of input or output error
   */
  public SensorJsonDatastore(Path storageDirectory, ObjectMapper objectMapper)
      throws IOException {
    super(storageDirectory.resolve("sensors"), objectMapper, Sensor.class, Sensor::getName);
  }

  /**
   * Returns unique field name
   * @return String name
   */
  @Override
  public String getUniqueFieldName() {
    return "name";
  }
}
