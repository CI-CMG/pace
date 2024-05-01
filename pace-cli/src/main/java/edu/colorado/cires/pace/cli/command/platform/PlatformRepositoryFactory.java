package edu.colorado.cires.pace.cli.command.platform;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.datastore.sqlite.PlatformSQLiteDatastore;
import edu.colorado.cires.pace.repository.PlatformRepository;
import edu.colorado.cires.pace.datastore.json.PlatformJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

public final class PlatformRepositoryFactory {

  public static PlatformRepository createJsonRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new PlatformRepository(
        new PlatformJsonDatastore(
            datastoreDirectory, objectMapper
        )
    );
  }
  
  public static PlatformRepository createSQLiteRepository(Path sqliteFile) {
    return new PlatformRepository(
        new PlatformSQLiteDatastore(sqliteFile)
    );
  }

}
