package edu.colorado.cires.pace.data.object;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;
import org.junit.jupiter.api.Test;

class PersonTest extends ObjectWithUniqueFieldTest<Person> {

  @Override
  protected Person createObject() {
    return Person.builder().build();
  }
  
  @Test
  void testGetUniqueField() {
    Person person = createObject();
    assertNull(person.getUniqueField());
    person = person.toBuilder()
        .name("name")
        .build();
    assertEquals("name", person.getUniqueField());
  }
}