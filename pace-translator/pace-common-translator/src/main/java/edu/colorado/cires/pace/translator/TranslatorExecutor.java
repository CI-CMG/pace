package edu.colorado.cires.pace.translator;

import edu.colorado.cires.pace.data.object.TabularTranslationField;
import edu.colorado.cires.pace.data.object.TabularTranslator;
import edu.colorado.cires.pace.repository.CRUDRepository;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class TranslatorExecutor<O, T extends TabularTranslator<? extends TabularTranslationField>> {
  
  private final T translatorDefinition;
  private final Class<O> clazz;
  private final CRUDRepository<?>[] dependencyRepositories;

  protected TranslatorExecutor(T translatorDefinition, Class<O> clazz, CRUDRepository<?>... dependencyRepositories) throws TranslationException {
    this.translatorDefinition = translatorDefinition;
    this.clazz = clazz;
    this.dependencyRepositories = dependencyRepositories;
    TranslatorUtils.validateTranslator(translatorDefinition, clazz);
  }

  public Stream<ObjectWithRuntimeException<O>> translate(InputStream inputStream) throws TranslationException {
    return translate(getPropertyStream(inputStream, translatorDefinition));
  }
  
  public Stream<ObjectWithRuntimeException<O>> translate(Reader reader) throws TranslationException {
    return translate(getPropertyStream(reader, translatorDefinition));
  }
  
  private Stream<ObjectWithRuntimeException<O>> translate(Stream<MapWithRowNumber> propertyStream) {
    return propertyStream
        .map(m -> {
          try {
            O object = TranslatorUtils.convertMapToObject(m.map(), clazz, m.row(), dependencyRepositories);
            return new ObjectWithRuntimeException<>(object, null);
          } catch (TranslationException e) {
            RuntimeException runtimeException = new RuntimeException();
            for (Throwable throwable : e.getCause().getSuppressed()) {
              runtimeException.addSuppressed(throwable);
            }
            return new ObjectWithRuntimeException<>(null, runtimeException);
          }
        });
  }

  protected abstract Stream<MapWithRowNumber> getPropertyStream(InputStream inputStream, T translatorDefinition)
      throws TranslationException;
  
  protected abstract Stream<MapWithRowNumber> getPropertyStream(Reader reader, T translatorDefinition) throws TranslationException;
  
  public record MapWithRowNumber(Map<String, Optional<String>> map, Integer row) {}

}
