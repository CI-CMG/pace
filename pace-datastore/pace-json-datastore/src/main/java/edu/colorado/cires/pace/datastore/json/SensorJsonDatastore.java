package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.Sensor;
import java.io.IOException;
import java.nio.file.Path;

public class SensorJsonDatastore extends JsonDatastore<Sensor> {

  public SensorJsonDatastore(Path storageDirectory, ObjectMapper objectMapper)
      throws IOException {
    super(storageDirectory.resolve("sensors"), objectMapper, Sensor.class);
  }

  @Override
  public String getUniqueFieldName() {
    return "name";
  }
}
