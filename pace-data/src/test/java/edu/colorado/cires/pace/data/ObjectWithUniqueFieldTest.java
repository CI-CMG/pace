package edu.colorado.cires.pace.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public abstract class ObjectWithUniqueFieldTest<O extends ObjectWithUniqueField> {
  
  protected abstract O createObject();
  
  @Test
  void testSetUUID() {
    UUID uuid = UUID.randomUUID();
    O object = createObject();
    assertNull(object.getUuid());
    object = (O) object.setUuid(uuid);
    assertEquals(uuid, object.getUuid());
  }

}
