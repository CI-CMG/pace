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

  public Stream<O> translate(InputStream inputStream) throws TranslationException {
    return getPropertyStream(inputStream, translatorDefinition)
        .map(this::convertMapToObject);
  }
  
  public Stream<O> translate(Reader reader) throws TranslationException {
    return getPropertyStream(reader, translatorDefinition)
        .map(this::convertMapToObject);
  }
  
  private O convertMapToObject(Map<String, Optional<String>> propertyMap) {
    try {
      return TranslatorUtils.convertMapToObject(propertyMap, clazz, dependencyRepositories);
    } catch (TranslationException e) {
      throw new RuntimeException(e);
    }
  }

  protected abstract Stream<Map<String, Optional<String>>> getPropertyStream(InputStream inputStream, T translatorDefinition)
      throws TranslationException;
  
  protected abstract Stream<Map<String, Optional<String>>> getPropertyStream(Reader reader, T translatorDefinition) throws TranslationException;

}
