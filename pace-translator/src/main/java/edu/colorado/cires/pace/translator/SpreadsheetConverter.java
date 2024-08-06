package edu.colorado.cires.pace.translator;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.base.Translator;
import edu.colorado.cires.pace.translator.converter.Converter;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class SpreadsheetConverter {
  
  public static <T extends Translator, O extends AbstractObject> Stream<ObjectWithRowError<O>> execute(Supplier<Stream<MapWithRowNumber>> reader, T translator, Converter<T, O> converter) {
    RuntimeException runtimeException = new RuntimeException("Translation failed");

    return reader.get()
        .filter(m -> !m.map().values().stream().allMatch(
            v -> v.value().isEmpty()
        ))
        .map(mapWithRowNumber -> {
          try {
            RuntimeException rte = new RuntimeException();
            O object = converter.convert(translator, mapWithRowNumber.map(), mapWithRowNumber.row(), rte);
            if (rte.getSuppressed().length > 0) {
              for (Throwable throwable : rte.getSuppressed()) {
                runtimeException.addSuppressed(throwable);
              }
              return new ObjectWithRowError<>(object, mapWithRowNumber.row(), rte);
            }
            return new ObjectWithRowError<>(object, mapWithRowNumber.row(), null);
          } catch (TranslationException e) {
            runtimeException.addSuppressed(e);
            return new ObjectWithRowError<>(null, mapWithRowNumber.row(), e);
          }
        });
  }

}
