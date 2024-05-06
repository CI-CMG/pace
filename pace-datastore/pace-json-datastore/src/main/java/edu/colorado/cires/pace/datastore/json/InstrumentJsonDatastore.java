package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.Instrument;
import java.io.IOException;
import java.nio.file.Path;

public class InstrumentJsonDatastore extends JsonDatastore<Instrument> {

  public InstrumentJsonDatastore(Path workDirectory, ObjectMapper objectMapper) throws IOException {
    super(workDirectory.resolve("instruments.json"), objectMapper, Instrument.class, Instrument::getName, new TypeReference<>() {});
  }

  @Override
  public String getUniqueFieldName() {
    return "name";
  }
}
