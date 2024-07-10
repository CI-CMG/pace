package edu.colorado.cires.pace.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import edu.colorado.cires.pace.data.object.ObjectWithName;
import org.assertj.swing.fixture.JPanelFixture;

abstract class ObjectWithNamePanelTest<O extends ObjectWithName> extends MetadataPanelTest<O> {

  @Override
  protected void fillOutForm(JPanelFixture formFixture, O object) {
    enterFieldText(formFixture, "name", object.getName());
  }

  @Override
  protected String[] objectToRow(O object) {
    return new String[] {
        object.getName()
    };
  }

  @Override
  protected void requireFormContents(JPanelFixture formFixture, O object) {
    requireFieldText(formFixture, "uuid", object.getUuid().toString());
    requireFieldText(formFixture, "name", object.getName());
  }

  @Override
  protected String getUniqueField(O object) {
    return object.getName();
  }

  @Override
  protected void assertObjectsEqual(O expected, O actual, boolean checkUUID) {
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    } else {
      assertNotNull(actual.getUuid());
    }

    assertEquals(expected.getName(), actual.getName());
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }
}
