package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.ObjectWithName;
import javax.swing.JPanel;

abstract class ObjectWithNameFormTest<O extends ObjectWithName, F extends ObjectWithNameForm<O>> extends MetadataFormTest<O, F> {

  @Override
  protected void populateAdditionalFormFields(O object, JPanel contentPanel) {

  }

  @Override
  protected void assertAdditionalFieldsEqual(O expected, O actual) {

  }
}
