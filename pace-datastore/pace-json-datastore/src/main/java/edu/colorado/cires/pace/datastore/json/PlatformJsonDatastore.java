package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.Platform;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class PlatformJsonDatastore extends JsonDatastore<Platform> {

  public PlatformJsonDatastore(Path workDirectory, ObjectMapper objectMapper) throws IOException {
    super(workDirectory.resolve("platforms.json"), objectMapper, Platform.class, Platform::getName, new TypeReference<>() {});
  }

  @Override
  public String getUniqueFieldName() {
    return "name";
  }
}
