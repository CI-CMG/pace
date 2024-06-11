package edu.colorado.cires.pace.translator.converter;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.data.translator.Translator;
import edu.colorado.cires.pace.translator.TranslationException;
import edu.colorado.cires.pace.translator.TranslatorExecutor.ValueWithColumnNumber;
import java.util.Map;

public abstract class Converter<T extends Translator, O extends ObjectWithUniqueField> {
  
  public abstract O convert(T translator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException)
      throws TranslationException;

}
