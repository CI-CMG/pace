package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.Instrument;
import java.io.IOException;
import java.nio.file.Path;

public class InstrumentJsonDatastore extends JsonDatastore<Instrument, String> {

  protected InstrumentJsonDatastore(Path workDirectory, ObjectMapper objectMapper) throws IOException {
    super(workDirectory.resolve("instruments"), objectMapper, Instrument.class, Instrument::getUUID, Instrument::getName);
  }
}
