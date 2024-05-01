package edu.colorado.cires.pace.cli.command.instrument;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.datastore.sqlite.InstrumentSQLiteDatastore;
import edu.colorado.cires.pace.repository.InstrumentRepository;
import edu.colorado.cires.pace.datastore.json.FileTypeJsonDatastore;
import edu.colorado.cires.pace.datastore.json.InstrumentJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

public final class InstrumentRepositoryFactory {

  public static InstrumentRepository createJsonRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new InstrumentRepository(
        new InstrumentJsonDatastore(datastoreDirectory, objectMapper),
        new FileTypeJsonDatastore(datastoreDirectory, objectMapper)
    );
  }
  
  public static InstrumentRepository createSQLiteRepository(Path sqliteFile, Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    FileTypeJsonDatastore fileTypeJsonDatastore = new FileTypeJsonDatastore(datastoreDirectory, objectMapper);
    return new InstrumentRepository(
        new InstrumentSQLiteDatastore(sqliteFile, fileTypeJsonDatastore),
        fileTypeJsonDatastore
    );
  }

}
