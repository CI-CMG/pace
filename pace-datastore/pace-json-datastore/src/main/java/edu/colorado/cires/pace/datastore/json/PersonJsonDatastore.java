package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.Person;
import java.io.IOException;
import java.nio.file.Path;

public class PersonJsonDatastore extends JsonDatastore<Person, String> {

  protected PersonJsonDatastore(Path workDirectory, ObjectMapper objectMapper) throws IOException {
    super(workDirectory.resolve("people"), objectMapper, Person.class, Person::getUUID, Person::getName);
  }
}
