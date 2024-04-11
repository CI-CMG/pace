package edu.colorado.cires.pace.cli.command.instrument;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.repository.InstrumentRepository;
import edu.colorado.cires.pace.datastore.json.FileTypeJsonDatastore;
import edu.colorado.cires.pace.datastore.json.InstrumentJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

final class InstrumentRepositoryFactory {

  public static InstrumentRepository createRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new InstrumentRepository(
        new InstrumentJsonDatastore(datastoreDirectory, objectMapper),
        new FileTypeJsonDatastore(datastoreDirectory, objectMapper)
    );
  }

}
