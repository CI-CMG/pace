package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.Ship;
import java.io.IOException;
import java.nio.file.Path;

public class ShipJsonDatastore extends JsonDatastore<Ship> {

  public ShipJsonDatastore(Path workDirectory, ObjectMapper objectMapper) throws IOException {
    super(workDirectory, objectMapper, Ship.class);
  }

  @Override
  public String getUniqueFieldName() {
    return "name";
  }
}
