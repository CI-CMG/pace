package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.DetectionType;
import java.io.IOException;
import java.nio.file.Path;

public class DetectionTypeJsonDatastore extends JsonDatastore<DetectionType> {

  public DetectionTypeJsonDatastore(Path workDirectory, ObjectMapper objectMapper) throws IOException {
    super(workDirectory.resolve("data-types.json"), objectMapper, DetectionType.class, DetectionType::getSource, new TypeReference<>() {});
  }

  @Override
  public String getUniqueFieldName() {
    return "source";
  }
}
