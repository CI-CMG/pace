package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.Person;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class PersonJsonDatastore extends JsonDatastore<Person> {

  public PersonJsonDatastore(Path workDirectory, ObjectMapper objectMapper) throws IOException {
    super(workDirectory.resolve("people.json"), objectMapper, Person.class, Person::getName, new TypeReference<>() {});
  }

  @Override
  public String getUniqueFieldName() {
    return "name";
  }
}
