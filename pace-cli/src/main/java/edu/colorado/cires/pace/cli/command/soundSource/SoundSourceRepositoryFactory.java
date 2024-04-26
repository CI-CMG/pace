package edu.colorado.cires.pace.cli.command.soundSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.datastore.json.SoundSourceJsonDatastore;
import edu.colorado.cires.pace.repository.SoundSourceRepository;
import java.io.IOException;
import java.nio.file.Path;

public final class SoundSourceRepositoryFactory {
  
  public static SoundSourceRepository createRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new SoundSourceRepository(
        new SoundSourceJsonDatastore(datastoreDirectory, objectMapper)
    );
  }

}
