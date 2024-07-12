package edu.colorado.cires.pace.cli.command;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.data.translator.Translator;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public abstract class ObjectWithEditableUUIDCommandTest<O extends ObjectWithUniqueField, T extends Translator> extends TranslateCommandTest<O, T> {

  @Test
  void testCreateUUIDDefined() throws IOException {
    O object = createObject("test", true);
    O created = writeObject(object);
    assertObjectsEqual(object, created, true);
  }
}
