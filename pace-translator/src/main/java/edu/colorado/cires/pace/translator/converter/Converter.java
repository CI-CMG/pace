package edu.colorado.cires.pace.translator.converter;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.base.Translator;
import edu.colorado.cires.pace.translator.TranslationException;
import edu.colorado.cires.pace.translator.ValueWithColumnNumber;
import java.util.Map;

/**
 * Converter translates objects to abstract objects
 * @param <T> translator type
 * @param <O> object type
 */
public abstract class Converter<T extends Translator, O extends AbstractObject> {

  /**
   * Creates an object by mapping using the provided translator
   * @param translator translates to type O
   * @param properties maps property names to values
   * @param row relevant row
   * @param runtimeException thrown in case of error mapping
   * @return O object
   * @throws TranslationException thrown if referencing Converter class convert function
   */
  public abstract O convert(T translator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException)
      throws TranslationException;

}
