package edu.colorado.cires.pace.translator;

import edu.colorado.cires.pace.data.object.Platform;
import edu.colorado.cires.pace.data.object.Project;
import edu.colorado.cires.pace.data.object.Sea;
import edu.colorado.cires.pace.data.object.Ship;
import edu.colorado.cires.pace.data.object.TabularTranslationField;
import edu.colorado.cires.pace.data.object.TabularTranslator;
import edu.colorado.cires.pace.data.validation.ConstraintViolation;
import edu.colorado.cires.pace.data.validation.ValidationException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

final class TranslatorUtils {

  public static <O> O convertMapToObject(Map<String, Optional<String>> propertyMap, Class<O> clazz)
      throws ValidationException, TranslationException {
    if (clazz.isAssignableFrom(Ship.class)) {
      return (O) shipFromMap(propertyMap);
    } else if (clazz.isAssignableFrom(Sea.class)) {
      return (O) seaFromMap(propertyMap);
    } else if (clazz.isAssignableFrom(Project.class)) {
      return (O) projectFromMap(propertyMap);
    } else if (clazz.isAssignableFrom(Platform.class)) {
      return (O) platformFromMap(propertyMap);
    } else {
      throw new TranslationException(String.format(
          "Translation not supported for %s", clazz.getSimpleName()
      ));
    }
  }

  public static void validateTranslator(TabularTranslator<? extends TabularTranslationField> translator, Class<?> clazz) throws TranslationException {
    Set<String> translatorFields = translator.getFields().stream()
        .map(TabularTranslationField::getPropertyName)
        .collect(Collectors.toSet());
    
    if (clazz.isAssignableFrom(Ship.class)) {
      validateObjectWithUUIDAndUniqueFieldTranslator(translatorFields, Ship.class);
    } else if (clazz.isAssignableFrom(Sea.class)) {
      validateObjectWithUUIDAndUniqueFieldTranslator(translatorFields, Sea.class);
    } else if (clazz.isAssignableFrom(Project.class)) {
      validateObjectWithUUIDAndUniqueFieldTranslator(translatorFields, Project.class);
    } else if (clazz.isAssignableFrom(Platform.class)) {
      validateObjectWithUUIDAndUniqueFieldTranslator(translatorFields, Platform.class);
    } else {
      throw new TranslationException(String.format(
          "Translation not supported for %s", clazz.getSimpleName()
      ));
    }
  }
  
  private static void validateObjectWithUUIDAndUniqueFieldTranslator(Set<String> propertyNames, Class<?> clazz) throws TranslationException {
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
  
  private static Ship shipFromMap(Map<String, Optional<String>> propertyMap) throws ValidationException {
    
    Set<ConstraintViolation> violations = new HashSet<>(0);
    
    UUID uuid = null;
    try {
      uuid = uuidFromString(getProperty(propertyMap, "uuid"));
    } catch (ValidationException e) {
      violations.addAll(e.getViolations());
    }
    
    try {
      Ship ship = Ship.builder()
          .uuid(uuid)
          .name(getProperty(propertyMap, "name"))
          .build();
      
      if (!violations.isEmpty()) {
        throw new ValidationException(Ship.class, violations);
      }
      
      return ship;
    } catch (ValidationException e) {
      violations.addAll(e.getViolations());
      throw new ValidationException(Ship.class, violations);
    }
  }

  private static Sea seaFromMap(Map<String, Optional<String>> propertyMap) throws ValidationException {

    Set<ConstraintViolation> violations = new HashSet<>(0);

    UUID uuid = null;
    try {
      uuid = uuidFromString(getProperty(propertyMap, "uuid"));
    } catch (ValidationException e) {
      violations.addAll(e.getViolations());
    }

    try {
      Sea sea = Sea.builder()
          .uuid(uuid)
          .name(getProperty(propertyMap, "name"))
          .build();

      if (!violations.isEmpty()) {
        throw new ValidationException(Sea.class, violations);
      }

      return sea;
    } catch (ValidationException e) {
      violations.addAll(e.getViolations());
      throw new ValidationException(Sea.class, violations);
    }
  }

  private static Project projectFromMap(Map<String, Optional<String>> propertyMap) throws ValidationException {

    Set<ConstraintViolation> violations = new HashSet<>(0);

    UUID uuid = null;
    try {
      uuid = uuidFromString(getProperty(propertyMap, "uuid"));
    } catch (ValidationException e) {
      violations.addAll(e.getViolations());
    }

    try {
      Project project = Project.builder()
          .uuid(uuid)
          .name(getProperty(propertyMap, "name"))
          .build();

      if (!violations.isEmpty()) {
        throw new ValidationException(Project.class, violations);
      }

      return project;
    } catch (ValidationException e) {
      violations.addAll(e.getViolations());
      throw new ValidationException(Project.class, violations);
    }
  }

  private static Platform platformFromMap(Map<String, Optional<String>> propertyMap) throws ValidationException {

    Set<ConstraintViolation> violations = new HashSet<>(0);

    UUID uuid = null;
    try {
      uuid = uuidFromString(getProperty(propertyMap, "uuid"));
    } catch (ValidationException e) {
      violations.addAll(e.getViolations());
    }

    try {
      Platform platform = Platform.builder()
          .uuid(uuid)
          .name(getProperty(propertyMap, "name"))
          .build();

      if (!violations.isEmpty()) {
        throw new ValidationException(Platform.class, violations);
      }

      return platform;
    } catch (ValidationException e) {
      violations.addAll(e.getViolations());
      throw new ValidationException(Platform.class, violations);
    }
  }
  
  private static UUID uuidFromString(String uuidString) throws ValidationException {
    if (uuidString == null || StringUtils.isBlank(uuidString)) {
      return null;
    }
    
    try {
      return UUID.fromString(uuidString);
    } catch (IllegalArgumentException e) {
      throw new ValidationException(UUID.class, Set.of(
          ConstraintViolation.builder()
              .property("uuid")
              .message("invalid uuid format")
              .build()
      ));
    }
  }
  
  private static String getProperty(Map<String, Optional<String>> map, String property) {
    Optional<String> value = map.get(property);
    if (value == null) {
      return null;
    }
    
    return value.orElse(null);
  }
}
