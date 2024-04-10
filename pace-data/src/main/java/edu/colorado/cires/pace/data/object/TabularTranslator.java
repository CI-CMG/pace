package edu.colorado.cires.pace.data.object;

import java.util.List;

public interface TabularTranslator<T extends TabularTranslationField> extends ObjectWithName {
  
  List<T> getFields();

}
