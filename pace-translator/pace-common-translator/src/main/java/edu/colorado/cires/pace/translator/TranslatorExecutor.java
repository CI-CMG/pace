package edu.colorado.cires.pace.translator;

import edu.colorado.cires.pace.data.object.TabularTranslationField;
import edu.colorado.cires.pace.data.object.TabularTranslator;
import edu.colorado.cires.pace.repository.CRUDRepository;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class TranslatorExecutor<O, T extends TabularTranslator<? extends TabularTranslationField>> {
  
  private final T translatorDefinition;
  private final Class<O> clazz;
  private final CRUDRepository<?>[] dependencyRepositories;

  protected TranslatorExecutor(T translatorDefinition, Class<O> clazz, CRUDRepository<?>... dependencyRepositories) {
    this.translatorDefinition = translatorDefinition;
    this.clazz = clazz;
    this.dependencyRepositories = dependencyRepositories;
  }

  public Stream<ObjectWithRowException<O>> translate(InputStream inputStream) throws IOException {
    return translate(getPropertyStream(inputStream, translatorDefinition));
  }
  
  public Stream<ObjectWithRowException<O>> translate(Reader reader) throws IOException {
    return translate(getPropertyStream(reader, translatorDefinition));
  }
  
  private Stream<ObjectWithRowException<O>> translate(Stream<MapWithRowNumber> propertyStream) {
    return propertyStream
        .map(m -> {
          try {
            O object = TranslatorUtils.convertMapToObject(m.map(), clazz, m.row(), dependencyRepositories);
            return new ObjectWithRowException<>(object, m.row(), null);
          } catch (RowConversionException e) {
            return new ObjectWithRowException<>(null, m.row(), e);
          }
        });
  }

  protected abstract Stream<MapWithRowNumber> getPropertyStream(InputStream inputStream, T translatorDefinition)
      throws IOException;
  
  protected abstract Stream<MapWithRowNumber> getPropertyStream(Reader reader, T translatorDefinition) throws IOException;
  
  public record MapWithRowNumber(Map<String, ValueWithColumnNumber> map, Integer row) {}
  
  public record ValueWithColumnNumber(Optional<String> value, Integer column) {}

}
