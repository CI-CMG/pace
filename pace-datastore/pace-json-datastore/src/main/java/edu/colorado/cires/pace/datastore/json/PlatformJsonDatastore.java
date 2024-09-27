package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.platform.Platform;
import java.io.IOException;
import java.nio.file.Path;

/**
 * PlatformJsonDatastore extends JsonDatastore and returns name
 * as unique field name
 */
public class PlatformJsonDatastore extends JsonDatastore<Platform> {

  /**
   * Initializes a platform JSON datastore
   *
   * @param workDirectory the location of the datastore
   * @param objectMapper relevant object mapper
   * @throws IOException in case of input or output error
   */
  public PlatformJsonDatastore(Path workDirectory, ObjectMapper objectMapper) throws IOException {
    super(workDirectory.resolve("platforms"), objectMapper, Platform.class, Platform::getName);
  }

  /**
   * Returns the unique field name
   * @return String name
   */
  @Override
  public String getUniqueFieldName() {
    return "name";
  }
}
