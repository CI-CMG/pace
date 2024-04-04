package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.DetectionType;
import java.io.IOException;
import java.nio.file.Path;

public class DetectionTypeJsonDatastore extends JsonDatastore<DetectionType, String> {

  public DetectionTypeJsonDatastore(Path workDirectory, ObjectMapper objectMapper) throws IOException {
    super(workDirectory.resolve("data-types"), objectMapper, DetectionType.class, DetectionType::getUUID, DetectionType::getScienceName);
  }
}
