package edu.colorado.cires.pace.translator;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.data.translator.Translator;
import edu.colorado.cires.pace.translator.TranslatorExecutor.MapWithRowNumber;
import edu.colorado.cires.pace.translator.converter.Converter;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class SpreadsheetConverter {
  
  public static <T extends Translator, O extends ObjectWithUniqueField> Stream<O> execute(Supplier<Stream<MapWithRowNumber>> reader, T translator, Converter<T, O> converter) {
    RuntimeException runtimeException = new RuntimeException("Translation failed");
    
    Stream<O> objectStream = reader.get()
        .map(mapWithRowNumber -> {
          try {
            return converter.convert(translator, mapWithRowNumber.map(), mapWithRowNumber.row(), runtimeException);
          } catch (TranslationException e) {
            runtimeException.addSuppressed(e);
            return null;
          }
        }).filter(Objects::nonNull);
    
    if (runtimeException.getSuppressed().length > 0) {
      throw runtimeException;
    }
    
    return objectStream;
  }

}
