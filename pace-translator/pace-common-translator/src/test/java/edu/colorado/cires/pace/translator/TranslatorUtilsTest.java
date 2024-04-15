package edu.colorado.cires.pace.translator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.colorado.cires.pace.data.object.ObjectWithName;
import edu.colorado.cires.pace.data.object.Platform;
import edu.colorado.cires.pace.data.object.Project;
import edu.colorado.cires.pace.data.object.Sea;
import edu.colorado.cires.pace.data.object.Ship;
import edu.colorado.cires.pace.data.validation.ConstraintViolation;
import edu.colorado.cires.pace.data.validation.ValidationException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class TranslatorUtilsTest {
  
  @Test
  void testUnsupportedType() {
    record TestObject(String name) {}
    
    TranslationException exception = assertThrows(TranslationException.class, () -> TranslatorUtils.convertMapToObject(Map.of(
        "p1", Optional.of("2")
    ), TestObject.class));
    assertEquals(String.format(
        "Translation not supported for %s", TestObject.class.getSimpleName()
    ), exception.getMessage());
  }
  
  @ParameterizedTest
  @ValueSource(classes = {
      Ship.class,
      Project.class,
      Sea.class,
      Platform.class
  })
  void testConvert(Class<? extends ObjectWithName> clazz) throws ValidationException, TranslationException {
    Map<String, Optional<String>> propertyMap = Map.of(
        "uuid", Optional.of(UUID.randomUUID().toString()),
        "name", Optional.of("test-name")
    );
    
    ObjectWithName object = TranslatorUtils.convertMapToObject(propertyMap, clazz);
    assertEquals(propertyMap.get("uuid").orElseThrow(
        () -> new IllegalStateException("uuid not found in propertyMap")
    ), object.getUuid().toString());
    assertEquals(propertyMap.get("name").orElseThrow(
        () -> new IllegalStateException("name not found in propertyMap")
    ), object.getName());
  }

  @ParameterizedTest
  @ValueSource(classes = {
      Ship.class,
      Sea.class,
      Project.class,
      Platform.class
  })
  void testConvertMissingProperty(Class<? extends ObjectWithName> clazz) throws ValidationException, TranslationException {
    Map<String, Optional<String>> propertyMap = Map.of(
        "name", Optional.of("test-name")
    );

    ObjectWithName object = TranslatorUtils.convertMapToObject(propertyMap, clazz);
    assertNull(object.getUuid());
    assertEquals(propertyMap.get("name").orElseThrow(
        () -> new IllegalStateException("name not found in propertyMap")
    ), object.getName());
  }

  @ParameterizedTest
  @ValueSource(classes = {
      Ship.class,
      Project.class,
      Sea.class,
      Platform.class
  })
  void testConvertNullUUID(Class<? extends ObjectWithName> clazz) throws ValidationException, TranslationException {
    Map<String, Optional<String>> propertyMap = Map.of(
        "uuid", Optional.empty(),
        "name", Optional.of("test-name")
    );

    ObjectWithName object = TranslatorUtils.convertMapToObject(propertyMap, clazz);
    assertNull(object.getUuid());
    assertEquals(propertyMap.get("name").orElseThrow(
        () -> new IllegalStateException("name not found in propertyMap")
    ), object.getName());
  }

  @ParameterizedTest
  @ValueSource(classes = {
      Ship.class,
      Project.class,
      Sea.class,
      Platform.class
  })
  void testConvertBadUUIDAndName(Class<? extends ObjectWithName> clazz) {
    Map<String, Optional<String>> propertyMap = Map.of(
        "uuid", Optional.of("test-uuid"),
        "name", Optional.empty()
    );

    ValidationException exception = assertThrows(ValidationException.class, () -> TranslatorUtils.convertMapToObject(propertyMap, clazz));
    assertEquals(String.format(
        "%s validation failed", clazz.getSimpleName()
    ), exception.getMessage());
    
    assertEquals(2, exception.getViolations().size());
    ConstraintViolation violation = exception.getViolations().stream()
        .filter(v -> v.getProperty().equals("uuid"))
        .findFirst().orElseThrow(
            () -> new IllegalStateException("uuid violation not found")
        );
    assertEquals("uuid", violation.getProperty());
    assertEquals("invalid uuid format", violation.getMessage());
    violation = exception.getViolations().stream()
        .filter(v -> v.getProperty().equals("name"))
        .findFirst().orElseThrow(
            () -> new IllegalStateException("name violation not found")
        );
    assertEquals("name", violation.getProperty());
    assertEquals("name must not be blank", violation.getMessage());
  }

  @ParameterizedTest
  @ValueSource(classes = {
      Ship.class,
      Project.class,
      Sea.class,
      Platform.class
  })
  void testConvertShipBadUUID(Class<? extends ObjectWithName> clazz) {
    Map<String, Optional<String>> propertyMap = Map.of(
        "uuid", Optional.of("test-uuid"),
        "name", Optional.of("test-name")
    );

    ValidationException exception = assertThrows(ValidationException.class, () -> TranslatorUtils.convertMapToObject(propertyMap, clazz));
    assertEquals(String.format(
        "%s validation failed", clazz.getSimpleName()
    ), exception.getMessage());

    assertEquals(1, exception.getViolations().size());
    ConstraintViolation violation = exception.getViolations().stream()
        .filter(v -> v.getProperty().equals("uuid"))
        .findFirst().orElseThrow(
            () -> new IllegalStateException("uuid violation not found")
        );
    assertEquals("uuid", violation.getProperty());
    assertEquals("invalid uuid format", violation.getMessage());
  }

}
