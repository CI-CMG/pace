package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.TabularTranslationField;
import javax.swing.JPanel;

public abstract class TranslatorFieldPanel<F extends TabularTranslationField> extends JPanel {
  
  protected abstract F toField();

}
