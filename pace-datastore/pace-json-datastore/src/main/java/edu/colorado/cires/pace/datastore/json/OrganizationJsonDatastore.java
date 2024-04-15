package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.Organization;
import java.io.IOException;
import java.nio.file.Path;

public class OrganizationJsonDatastore extends JsonDatastore<Organization> {

  public OrganizationJsonDatastore(Path workDirectory, ObjectMapper objectMapper) throws IOException {
    super(workDirectory.resolve("organizations"), objectMapper, Organization.class, Organization::getName);
  }

  @Override
  public String getUniqueFieldName() {
    return "name";
  }
}
