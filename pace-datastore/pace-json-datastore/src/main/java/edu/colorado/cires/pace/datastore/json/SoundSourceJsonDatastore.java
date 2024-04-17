package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.SoundSource;
import java.io.IOException;
import java.nio.file.Path;

public class SoundSourceJsonDatastore extends JsonDatastore<SoundSource> {

  public SoundSourceJsonDatastore(Path workDirectory, ObjectMapper objectMapper) throws IOException {
    super(workDirectory.resolve("source-sources"), objectMapper, SoundSource.class, SoundSource::getName);
  }

  @Override
  public String getUniqueFieldName() {
    return "name";
  }
}
