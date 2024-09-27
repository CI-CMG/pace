package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.sea.Sea;
import java.io.IOException;
import java.nio.file.Path;

/**
 * SeaJsonDatastore extends JsonDatastore and returns name
 * as the unique field name
 */
public class SeaJsonDatastore extends JsonDatastore<Sea> {

  /**
   * Initializes a sea JSON datastore
   *
   * @param workDirectory location of the datastore
   * @param objectMapper relevant object mapper
   * @throws IOException in case of input or output error
   */
  public SeaJsonDatastore(Path workDirectory, ObjectMapper objectMapper) throws IOException {
    super(workDirectory.resolve("seas"), objectMapper, Sea.class, Sea::getName);
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
