package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.Platform;
import java.io.IOException;
import java.nio.file.Path;

public class PlatformJsonDatastore extends JsonDatastore<Platform> {

  public PlatformJsonDatastore(Path workDirectory, ObjectMapper objectMapper) throws IOException {
    super(workDirectory.resolve("platforms"), objectMapper, Platform.class);
  }
}
