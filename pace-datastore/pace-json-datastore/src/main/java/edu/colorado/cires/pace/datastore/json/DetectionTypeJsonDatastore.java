package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.detectionType.DetectionType;
import java.io.IOException;
import java.nio.file.Path;

public class DetectionTypeJsonDatastore extends JsonDatastore<DetectionType> {

  public DetectionTypeJsonDatastore(Path workDirectory, ObjectMapper objectMapper) throws IOException {
    super(workDirectory.resolve("detection-types"), objectMapper, DetectionType.class, DetectionType::getSource);
  }

  @Override
  public String getUniqueFieldName() {
    return "source";
  }
}
