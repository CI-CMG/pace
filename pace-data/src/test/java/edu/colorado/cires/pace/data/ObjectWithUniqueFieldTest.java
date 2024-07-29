package edu.colorado.cires.pace.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public abstract class ObjectWithUniqueFieldTest<O extends AbstractObject> {
  
  protected abstract O createObject();
  
  @Test
  void testSetUUID() {
    UUID uuid = UUID.randomUUID();
    O object = createObject();
    assertNull(object.getUuid());
    object = (O) object.setUuid(uuid);
    assertEquals(uuid, object.getUuid());
  }
  
  @Test
  void testSetVisible() {
    O object = createObject();
    assertTrue(object.isVisible());
    object = (O) object.setVisible(false);
    assertFalse(object.isVisible());
    object = (O) object.setVisible(true);
    assertTrue(object.isVisible());
  }

}
