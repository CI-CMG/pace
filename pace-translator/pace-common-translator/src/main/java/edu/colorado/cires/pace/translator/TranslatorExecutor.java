package edu.colorado.cires.pace.translator;

import edu.colorado.cires.pace.data.object.TabularTranslationField;
import edu.colorado.cires.pace.data.object.TabularTranslator;
import edu.colorado.cires.pace.data.validation.ValidationException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class TranslatorExecutor<O, T extends TabularTranslator<? extends TabularTranslationField>> {
  
  private final T translatorDefinition;
  private final Class<O> clazz;

  protected TranslatorExecutor(T translatorDefinition, Class<O> clazz) throws TranslationException {
    this.translatorDefinition = translatorDefinition;
    this.clazz = clazz;
    validateTabularTranslator(translatorDefinition, clazz);
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
      return TranslatorUtils.convertMapToObject(propertyMap, clazz);
    } catch (ValidationException | TranslationException e) {
      throw new RuntimeException(e);
    }
  }

  protected abstract Stream<Map<String, Optional<String>>> getPropertyStream(InputStream inputStream, T translatorDefinition)
      throws TranslationException;
  
  protected abstract Stream<Map<String, Optional<String>>> getPropertyStream(Reader reader, T translatorDefinition) throws TranslationException;

  private static void validateTabularTranslator(TabularTranslator<?> translator, Class<?> clazz) throws TranslationException {
    Set<String> propertyNames = translator.getFields().stream().map(TabularTranslationField::getPropertyName)
        .collect(Collectors.toSet());

    Set<String> missingFieldNames = Arrays.stream(clazz.getDeclaredFields())
        .map(Field::getName)
        .filter(name -> !propertyNames.contains(name))
        .collect(Collectors.toSet());

    if (!missingFieldNames.isEmpty()) {
      throw new TranslationException(
          String.format(
              "Translator does not fully describe %s. Missing fields: %s", clazz.getSimpleName(), missingFieldNames
          )
      );
    }
  }

}
