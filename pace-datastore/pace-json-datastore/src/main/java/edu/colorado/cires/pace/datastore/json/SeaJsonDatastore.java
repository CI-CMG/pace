package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.Sea;
import java.io.IOException;
import java.nio.file.Path;

public class SeaJsonDatastore extends JsonDatastore<Sea> {

  public SeaJsonDatastore(Path workDirectory, ObjectMapper objectMapper) throws IOException {
    super(workDirectory.resolve("seas.json"), objectMapper, Sea.class, Sea::getName, new TypeReference<>() {});
  }

  @Override
  public String getUniqueFieldName() {
    return "name";
  }
}
