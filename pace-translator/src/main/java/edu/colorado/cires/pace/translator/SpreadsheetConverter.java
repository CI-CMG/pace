package edu.colorado.cires.pace.translator;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.data.translator.Translator;
import edu.colorado.cires.pace.translator.converter.Converter;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class SpreadsheetConverter {
  
  public static <T extends Translator, O extends ObjectWithUniqueField> Stream<ObjectWithRowException<O>> execute(Supplier<Stream<MapWithRowNumber>> reader, T translator, Converter<T, O> converter) {
    RuntimeException runtimeException = new RuntimeException("Translation failed");

    return reader.get()
        .map(mapWithRowNumber -> {
          try {
            RuntimeException rte = new RuntimeException();
            O object = converter.convert(translator, mapWithRowNumber.map(), mapWithRowNumber.row(), rte);
            if (rte.getSuppressed().length > 0) {
              for (Throwable throwable : rte.getSuppressed()) {
                runtimeException.addSuppressed(throwable);
              }
              return new ObjectWithRowException<>(object, mapWithRowNumber.row(), rte);
            }
            return new ObjectWithRowException<>(object, mapWithRowNumber.row(), null);
          } catch (TranslationException e) {
            runtimeException.addSuppressed(e);
            return new ObjectWithRowException<>(null, mapWithRowNumber.row(), e);
          }
        });
  }

}