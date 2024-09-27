package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.contact.organization.Organization;
import java.io.IOException;
import java.nio.file.Path;

/**
 * OrganizationJsonDatastore extends JsonDatastore and returns name as
 * unique field name
 */
public class OrganizationJsonDatastore extends JsonDatastore<Organization> {

  /**
   * Initializes an organization datastore
   *
   * @param workDirectory location of datastore on device
   * @param objectMapper relevant object mapper
   * @throws IOException in case of input or output error
   */
  public OrganizationJsonDatastore(Path workDirectory, ObjectMapper objectMapper) throws IOException {
    super(workDirectory.resolve("organizations"), objectMapper, Organization.class, Organization::getName);
  }

  /**
   * Returns the unique field name
   * @return String name
   */
  @Override
  public String getUniqueFieldName() {
    return "name";
  }
}
