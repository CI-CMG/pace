package edu.colorado.cires.pace.data;

import java.util.List;

public interface TabularTranslator<T extends TabularTranslationField> extends ObjectWithName {
  
  List<T> fields();

}
