package edu.colorado.cires.pace.cli.command.instrument;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.cli.command.fileType.FileTypeControllerFactory;
import edu.colorado.cires.pace.core.state.controller.InstrumentController;
import edu.colorado.cires.pace.core.state.repository.InstrumentRepository;
import edu.colorado.cires.pace.core.state.service.InstrumentService;
import edu.colorado.cires.pace.datastore.json.InstrumentJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

final class InstrumentControllerFactory {
  
  private static InstrumentJsonDatastore createDatastore(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new InstrumentJsonDatastore(
        datastoreDirectory, objectMapper
    );
  }
  
  private static InstrumentRepository createRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new InstrumentRepository(
        createDatastore(datastoreDirectory, objectMapper),
        FileTypeControllerFactory.createRepository(datastoreDirectory, objectMapper)
    );
  }
  
  private static InstrumentService createService(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new InstrumentService(
        createRepository(datastoreDirectory, objectMapper)
    );
  }
  
  public static InstrumentController createController(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new InstrumentController(
        createService(datastoreDirectory, objectMapper)
    );
  }

}
