package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.detectionType.DetectionType;
import java.io.IOException;
import java.nio.file.Path;

/**
 * DetectionTypeJsonDatastore extends JsonDatastore and provides
 * source as the unique field name
 */
public class DetectionTypeJsonDatastore extends JsonDatastore<DetectionType> {

  /**
   * Initializes a detection type json datastore
   *
   * @param workDirectory directory for holding datastore
   * @param objectMapper relevant object mapper for datastore
   * @throws IOException for input or output error
   */
  public DetectionTypeJsonDatastore(Path workDirectory, ObjectMapper objectMapper) throws IOException {
    super(workDirectory.resolve("detection-types"), objectMapper, DetectionType.class, DetectionType::getSource);
  }

  /**
   * Returns the unique field name
   * @return String source
   */
  @Override
  public String getUniqueFieldName() {
    return "source";
  }
}
