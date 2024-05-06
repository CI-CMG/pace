package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.Organization;
import java.io.IOException;
import java.nio.file.Path;

public class OrganizationJsonDatastore extends JsonDatastore<Organization> {

  public OrganizationJsonDatastore(Path workDirectory, ObjectMapper objectMapper) throws IOException {
    super(workDirectory.resolve("organizations.json"), objectMapper, Organization.class, Organization::getName, new TypeReference<>() {});
  }

  @Override
  public String getUniqueFieldName() {
    return "name";
  }
}
