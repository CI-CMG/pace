package edu.colorado.cires.pace.translator.converter;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.base.Translator;
import edu.colorado.cires.pace.translator.TranslationException;
import edu.colorado.cires.pace.translator.ValueWithColumnNumber;
import java.util.Map;

public abstract class Converter<T extends Translator, O extends AbstractObject> {
  
  public abstract O convert(T translator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException)
      throws TranslationException;

}
