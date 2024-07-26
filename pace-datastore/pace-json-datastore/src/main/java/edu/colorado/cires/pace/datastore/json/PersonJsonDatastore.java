package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.contact.person.Person;
import java.io.IOException;
import java.nio.file.Path;

public class PersonJsonDatastore extends JsonDatastore<Person> {

  public PersonJsonDatastore(Path workDirectory, ObjectMapper objectMapper) throws IOException {
    super(workDirectory.resolve("people"), objectMapper, Person.class, Person::getName);
  }

  @Override
  public String getUniqueFieldName() {
    return "name";
  }
}
