package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.contact.person.Person;
import java.io.IOException;
import java.nio.file.Path;

/**
 * PersonJsonDatastore extends JsonDatastore and returns name as
 * unique field name
 */
public class PersonJsonDatastore extends JsonDatastore<Person> {

  /**
   * Initializes a person JSON datastore
   *
   * @param workDirectory location of datastore on device
   * @param objectMapper relevant object mapper
   * @throws IOException in case of input or output error
   */
  public PersonJsonDatastore(Path workDirectory, ObjectMapper objectMapper) throws IOException {
    super(workDirectory.resolve("people"), objectMapper, Person.class, Person::getName);
  }

  /**
   * Returns the unique field name
   *
   * @return String name
   */
  @Override
  public String getUniqueFieldName() {
    return "name";
  }
}
