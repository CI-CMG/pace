package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.base.Translator;

public abstract class SimpleObjectTranslatorFormTest<T extends Translator, F extends BaseTranslatorForm<T>> extends BaseTranslatorFormTest<T, F> {

  @Override
  protected String[] getHeaderOptions() {
    return new String[] {
      "UUID", "Name"  
    };
  }

  @Override
  protected void populateInitialForm(BaseTranslatorForm<T> translatorForm) {
    selectComboBoxOption("uuid", "UUID");
    selectComboBoxOption("name", "Name");
  }
}
