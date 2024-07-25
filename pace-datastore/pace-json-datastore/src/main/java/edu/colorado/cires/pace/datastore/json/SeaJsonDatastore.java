package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.Sea;
import java.io.IOException;
import java.nio.file.Path;

public class SeaJsonDatastore extends JsonDatastore<Sea> {

  public SeaJsonDatastore(Path workDirectory, ObjectMapper objectMapper) throws IOException {
    super(workDirectory.resolve("seas"), objectMapper, Sea.class, Sea::getName);
  }

  @Override
  public String getUniqueFieldName() {
    return "name";
  }
}
