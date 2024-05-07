package edu.colorado.pace.gui.metadata.common;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import javax.swing.JPanel;

public abstract class ObjectForm<O extends ObjectWithUniqueField> {
  
  protected abstract JPanel getFormPanel();
  protected abstract O fieldsToObject();

}
