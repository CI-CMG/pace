package edu.colorado.cires.pace.translator;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.pace.data.object.ObjectWithName;
import edu.colorado.cires.pace.data.object.Platform;
import edu.colorado.cires.pace.data.object.Project;
import edu.colorado.cires.pace.data.object.Sea;
import edu.colorado.cires.pace.data.object.Ship;
import edu.colorado.cires.pace.data.object.TabularTranslationField;
import edu.colorado.cires.pace.data.object.TabularTranslator;
import edu.colorado.cires.pace.data.validation.ConstraintViolation;
import edu.colorado.cires.pace.data.validation.ValidationException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TranslatorExecutorTest {
  
  static class TestTranslatorField implements TabularTranslationField {
    private final String propertyName;
    private final int columnNumber;

    TestTranslatorField(String propertyName, int columnNumber) {
      this.propertyName = propertyName;
      this.columnNumber = columnNumber;
    }

    @Override
    public String getPropertyName() {
      return propertyName;
    }

    @Override
    public int getColumnNumber() {
      return columnNumber;
    }
  }
  
  static class TestTranslator implements TabularTranslator<TabularTranslationField> {
    
    private final List<TabularTranslationField> fields;

    TestTranslator(List<TabularTranslationField> fields) {
      this.fields = fields;
    }

    @Override
    public List<TabularTranslationField> getFields() {
      return fields;
    }

    @Override
    public String getName() {
      return "";
    }

    @Override
    public UUID getUuid() {
      return null;
    }
  }
  
  private TranslatorExecutor<ObjectWithName, TabularTranslator<TabularTranslationField>> createExecutor(TestTranslator translator, List<Map<String, Optional<String>>> maps, Class<ObjectWithName> clazz)
      throws TranslationException {
    return new TranslatorExecutor<>(translator, clazz) {

      @Override
      protected Stream<Map<String, Optional<String>>> getPropertyStream(InputStream inputStream,
          TabularTranslator<TabularTranslationField> translatorDefinition) {
        return maps.stream();
      }

      @Override
      protected Stream<Map<String, Optional<String>>> getPropertyStream(Reader reader,
          TabularTranslator<TabularTranslationField> translatorDefinition) {
        return maps.stream();
      }
    };
  }

  @ParameterizedTest
  @ValueSource(classes = {
      Ship.class,
      Sea.class,
      Project.class,
      Platform.class
  })
  void translate(Class<ObjectWithName> clazz) throws TranslationException {
    List<Map<String, Optional<String>>> maps = List.of(
        Map.of(
            "uuid", Optional.of(UUID.randomUUID().toString()),
            "name", Optional.of("test-name-1")
        ),
        Map.of(
            "uuid", Optional.of(UUID.randomUUID().toString()),
            "name", Optional.of("test-name-2")
        )
    );
    
    List<ObjectWithName> ships = createExecutor(new TestTranslator(List.of(
        new TestTranslatorField("uuid", 1),
        new TestTranslatorField("name", 2)
    )), maps, clazz).translate((InputStream) null).toList();
    assertEquals(2, ships.size());
    assertEquals(maps.get(0).get("uuid").orElseThrow(), ships.get(0).getUuid().toString());
    assertEquals(maps.get(0).get("name").orElseThrow(), ships.get(0).getName());
    assertEquals(maps.get(1).get("uuid").orElseThrow(), ships.get(1).getUuid().toString());
    assertEquals(maps.get(1).get("name").orElseThrow(), ships.get(1).getName());
  }

  @ParameterizedTest
  @ValueSource(classes = {
      Ship.class,
      Sea.class,
      Project.class,
      Platform.class
  })
  void translateInvalidObject(Class<ObjectWithName> clazz) {
    List<Map<String, Optional<String>>> maps = List.of(
        Map.of(
            "uuid", Optional.of("test-uuid"),
            "name", Optional.of("test-name-1")
        ),
        Map.of(
            "uuid", Optional.of(UUID.randomUUID().toString()),
            "name", Optional.of("test-name-2")
        )
    );

    RuntimeException exception = assertThrows(RuntimeException.class, () -> createExecutor(new TestTranslator(List.of(
        new TestTranslatorField("uuid", 1),
        new TestTranslatorField("name", 2)
    )), maps, clazz).translate((InputStream) null).toList());
    Throwable cause = exception.getCause();
    assertInstanceOf(ValidationException.class, cause);
    ValidationException validationException = (ValidationException) cause;
    assertEquals(String.format(
        "%s validation failed", clazz.getSimpleName()
    ), validationException.getMessage());
    Set<ConstraintViolation> violations = validationException.getViolations();
    assertEquals(1, violations.size());
    ConstraintViolation violation = violations.iterator().next();
    assertEquals("uuid", violation.getProperty());
    assertEquals("invalid uuid format", violation.getMessage());
  }
  
  @ParameterizedTest
  @ValueSource(classes = {
      Ship.class,
      Sea.class,
      Project.class,
      Platform.class
  })
  void testInvalidTranslatorDefinition(Class<ObjectWithName> clazz) {
    TranslationException exception = assertThrows(TranslationException.class, () -> createExecutor(new TestTranslator(List.of(
        new TestTranslatorField("uuid", 1),
        new TestTranslatorField("name-invalid", 2)
    )), Collections.emptyList(), clazz));
    assertEquals(String.format(
        "Translator does not fully describe %s. Missing fields: [name]", clazz.getSimpleName()
    ), exception.getMessage());
  }
}