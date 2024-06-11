package edu.colorado.cires.pace.translator.converter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.colorado.cires.pace.data.object.Ship;
import edu.colorado.cires.pace.data.translator.DateTimeSeparatedTimeTranslator;
import edu.colorado.cires.pace.data.translator.DefaultTimeTranslator;
import edu.colorado.cires.pace.data.translator.TimeTranslator;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.NotFoundException;
import edu.colorado.cires.pace.repository.ShipRepository;
import edu.colorado.cires.pace.translator.FieldException;
import edu.colorado.cires.pace.translator.TranslatorExecutor.ValueWithColumnNumber;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class ConversionUtilsTest {

  @Test
  void uuidFromMap() {
    UUID uuid = UUID.randomUUID();
    
    Map<String, ValueWithColumnNumber> map = new HashMap<>(0);
    map.put("test-property", new ValueWithColumnNumber(Optional.of(uuid.toString()), 1));

    UUID result = ConversionUtils.uuidFromMap(map, "test-property", 2, new RuntimeException());
    assertEquals(uuid, result);

    RuntimeException runtimeException = new RuntimeException();
    map.put("test-property", new ValueWithColumnNumber(Optional.of("test"), 1));

    result = ConversionUtils.uuidFromMap(map, "test-property", 2, runtimeException);
    assertNull(result);

    assertEquals(1, runtimeException.getSuppressed().length);
    Throwable throwable = runtimeException.getSuppressed()[0];
    assertInstanceOf(FieldException.class, throwable);
    FieldException fieldException = (FieldException) throwable;
    assertEquals("test-property", fieldException.getProperty());
    assertEquals(1, fieldException.getColumn());
    assertEquals(2, fieldException.getRow());
    assertEquals("Invalid UUID format", fieldException.getMessage());
  }

  @Test
  void floatFromMap() {
    Map<String, ValueWithColumnNumber> map = new HashMap<>(0);
    map.put("test-property", new ValueWithColumnNumber(Optional.of("1"), 1));

    Float result = ConversionUtils.floatFromMap(map, "test-property", 2, new RuntimeException());
    assertEquals(result, 1);

    RuntimeException runtimeException = new RuntimeException();
    map.put("test-property", new ValueWithColumnNumber(Optional.of("test"), 1));

    result = ConversionUtils.floatFromMap(map, "test-property", 2, runtimeException);
    assertNull(result);

    assertEquals(1, runtimeException.getSuppressed().length);
    Throwable throwable = runtimeException.getSuppressed()[0];
    assertInstanceOf(FieldException.class, throwable);
    FieldException fieldException = (FieldException) throwable;
    assertEquals("test-property", fieldException.getProperty());
    assertEquals(1, fieldException.getColumn());
    assertEquals(2, fieldException.getRow());
    assertEquals("Invalid float format", fieldException.getMessage());
  }

  @Test
  void integerFromMap() {
    Map<String, ValueWithColumnNumber> map = new HashMap<>(0);
    map.put("test-property", new ValueWithColumnNumber(Optional.of("1"), 1));
    
    Integer result = ConversionUtils.integerFromMap(map, "test-property", 2, new RuntimeException());
    assertEquals(result, 1);
    
    RuntimeException runtimeException = new RuntimeException();
    map.put("test-property", new ValueWithColumnNumber(Optional.of("test"), 1));
    
    result = ConversionUtils.integerFromMap(map, "test-property", 2, runtimeException);
    assertNull(result);
    
    assertEquals(1, runtimeException.getSuppressed().length);
    Throwable throwable = runtimeException.getSuppressed()[0];
    assertInstanceOf(FieldException.class, throwable);
    FieldException fieldException = (FieldException) throwable;
    assertEquals("test-property", fieldException.getProperty());
    assertEquals(1, fieldException.getColumn());
    assertEquals(2, fieldException.getRow());
    assertEquals("Invalid integer format", fieldException.getMessage());
  }

  @Test
  void pathFromMap() {
    Map<String, ValueWithColumnNumber> map = new HashMap<>(0);
    map.put("test-property", new ValueWithColumnNumber(Optional.of("test"), 1));

    Path result = ConversionUtils.pathFromMap(map, "test-property", 2, new RuntimeException());
    assertEquals(result, Path.of("test"));
  }

  @Test
  void localDateFromMap() {
    Map<String, ValueWithColumnNumber> map = new HashMap<>(0);
    map.put("test-property", new ValueWithColumnNumber(Optional.of("2024-01-01"), 1));

    LocalDate result = ConversionUtils.localDateFromMap(map, "test-property", 2, new RuntimeException());
    assertEquals(result, LocalDate.parse("2024-01-01"));

    RuntimeException runtimeException = new RuntimeException();
    map.put("test-property", new ValueWithColumnNumber(Optional.of("test"), 1));

    result = ConversionUtils.localDateFromMap(map, "test-property", 2, runtimeException);
    assertNull(result);

    assertEquals(1, runtimeException.getSuppressed().length);
    Throwable throwable = runtimeException.getSuppressed()[0];
    assertInstanceOf(FieldException.class, throwable);
    FieldException fieldException = (FieldException) throwable;
    assertEquals("test-property", fieldException.getProperty());
    assertEquals(1, fieldException.getColumn());
    assertEquals(2, fieldException.getRow());
    assertEquals("Invalid date format", fieldException.getMessage());
  }

  @Test
  void localDateTimeFromMap() {
    Map<String, ValueWithColumnNumber> map = new HashMap<>(0);
    map.put("test-property", new ValueWithColumnNumber(Optional.of("2024-01-01T01:00:00"), 1));

    LocalDateTime result = ConversionUtils.localDateTimeFromMap(map, DefaultTimeTranslator.builder().time("test-property").build(), 2, new RuntimeException());
    assertEquals(result, LocalDateTime.parse("2024-01-01T01:00:00"));

    RuntimeException runtimeException = new RuntimeException();
    map.put("test-property", new ValueWithColumnNumber(Optional.of("test"), 1));

    result = ConversionUtils.localDateTimeFromMap(map, DefaultTimeTranslator.builder().time("test-property").build(), 2, runtimeException);
    assertNull(result);

    assertEquals(1, runtimeException.getSuppressed().length);
    Throwable throwable = runtimeException.getSuppressed()[0];
    assertInstanceOf(FieldException.class, throwable);
    FieldException fieldException = (FieldException) throwable;
    assertEquals("test-property", fieldException.getProperty());
    assertEquals(1, fieldException.getColumn());
    assertEquals(2, fieldException.getRow());
    assertEquals("Invalid date time format", fieldException.getMessage());
    
    map.put("test-property", new ValueWithColumnNumber(Optional.of("2024-01-01"), 1));
    map.put("test-other-property", new ValueWithColumnNumber(Optional.of("12:00:00"), 2));

    runtimeException = new RuntimeException();
    result = ConversionUtils.localDateTimeFromMap(map, new TimeTranslator() {}, 2, runtimeException);
    assertNull(result);
    assertEquals(0, runtimeException.getSuppressed().length);
    
    result = ConversionUtils.localDateTimeFromMap(map, DateTimeSeparatedTimeTranslator.builder()
            .date("test-property")
            .time("test-other-property")
        .build(), 2, new RuntimeException());
    
    assertEquals(LocalDateTime.parse("2024-01-01T12:00:00"), result);
    
    runtimeException = new RuntimeException();
    map.put("test-other-property", new ValueWithColumnNumber(Optional.of("-"), 2));
    result = ConversionUtils.localDateTimeFromMap(map, DateTimeSeparatedTimeTranslator.builder()
        .date("test-property")
        .time("test-other-property")
        .build(), 2, runtimeException);
    assertEquals(LocalDateTime.parse("2024-01-01T00:00:00"), result);

    assertEquals(1, runtimeException.getSuppressed().length);
    throwable = runtimeException.getSuppressed()[0];
    assertInstanceOf(FieldException.class, throwable);
    fieldException = (FieldException) throwable;
    assertEquals("test-other-property", fieldException.getProperty());
    assertEquals(2, fieldException.getColumn());
    assertEquals(2, fieldException.getRow());
    assertEquals("Invalid time format", fieldException.getMessage());

    runtimeException = new RuntimeException();
    map.remove("test-other-property");
    result = ConversionUtils.localDateTimeFromMap(map, DateTimeSeparatedTimeTranslator.builder()
        .date("test-property")
        .time("test-other-property")
        .build(), 2, runtimeException);
    assertEquals(LocalDateTime.parse("2024-01-01T00:00:00"), result);

    assertEquals(0, runtimeException.getSuppressed().length);

    runtimeException = new RuntimeException();
    map.remove("test-property");
    result = ConversionUtils.localDateTimeFromMap(map, DateTimeSeparatedTimeTranslator.builder()
        .date("test-property")
        .time("test-other-property")
        .build(), 2, runtimeException);
    assertNull(result);

    assertEquals(0, runtimeException.getSuppressed().length);
  }

  @Test
  void propertyFromMap() {
    Map<String, ValueWithColumnNumber> map = Map.of(
        "test-property", new ValueWithColumnNumber(Optional.of("test"), 1)
    );
    
    ValueWithColumnNumber valueWithColumnNumber = ConversionUtils.propertyFromMap(
        map,
        "test-property"
    );
    
    assertEquals(1, valueWithColumnNumber.column());
    assertEquals("test", valueWithColumnNumber.value().orElseThrow());
    
    valueWithColumnNumber = ConversionUtils.propertyFromMap(
        map,
        "test-other-property"
    );
    assertTrue(valueWithColumnNumber.value().isEmpty());
    assertNull(valueWithColumnNumber.column());
  }

  @Test
  void stringFromMap() {
    Map<String, ValueWithColumnNumber> map = new HashMap<>(0);
    map.put("test-property", new ValueWithColumnNumber(Optional.of("test  "), 1));
    
    String result = ConversionUtils.stringFromMap(
        map, "test-property"
    );
    
    assertEquals("test", result);
    
    map.put("test-property", new ValueWithColumnNumber(Optional.of(""), 1));
    
    result = ConversionUtils.stringFromMap(
        map, "test-property"
    );
    
    assertNull(result);

    map.put("test-property", new ValueWithColumnNumber(Optional.empty(), 1));

    result = ConversionUtils.stringFromMap(
        map, "test-property"
    );

    assertNull(result);

    map.remove("test-property");

    result = ConversionUtils.stringFromMap(
        map, "test-property"
    );

    assertNull(result);
  }

  @Test
  void objectFromMap() throws NotFoundException, DatastoreException {
    Map<String, ValueWithColumnNumber> map = new HashMap<>(0);
    map.put("test-property", new ValueWithColumnNumber(Optional.of("ship-1"), 1));

    ShipRepository repository = mock(ShipRepository.class);
    try {
      when(repository.getByUniqueField("ship-1")).thenReturn(Ship.builder()
              .name("ship-1")
          .build());
    } catch (DatastoreException | NotFoundException e) {
      throw new RuntimeException(e);
    }

    Ship ship = ConversionUtils.objectFromMap(
        map, "test-property", repository, 1, new RuntimeException()
    );
    assertNotNull(ship);
    assertEquals("ship-1", ship.getName());
    
    when(repository.getByUniqueField("ship-1")).thenThrow(
        new NotFoundException("ship-1 not found")
    );

    RuntimeException runtimeException = new RuntimeException();
    ship = ConversionUtils.objectFromMap(
        map, "test-property", repository, 1, runtimeException
    );
    assertNull(ship);
    assertEquals(1, runtimeException.getSuppressed().length);
    Throwable throwable = runtimeException.getSuppressed()[0];
    assertInstanceOf(FieldException.class, throwable);
    FieldException fieldException = (FieldException) throwable;
    assertEquals("ship-1 not found", fieldException.getMessage());

    ship = ConversionUtils.objectFromMap(
        map, "test-other-property", repository, 1, runtimeException
    );
    assertNull(ship);
  }

  @Test
  void delimitedObjectsFromMap() throws NotFoundException, DatastoreException {
    Map<String, ValueWithColumnNumber> map = new HashMap<>(0);
    map.put("test-property", new ValueWithColumnNumber(Optional.of("ship-1;ship-2"), 1));

    ShipRepository repository = mock(ShipRepository.class);
    when(repository.getByUniqueField("ship-1")).thenReturn(Ship.builder()
        .name("ship-1")
        .build());

    when(repository.getByUniqueField("ship-2")).thenReturn(Ship.builder()
        .name("ship-2")
        .build());

    List<Ship> ships = ConversionUtils.delimitedObjectsFromMap(
        map, "test-property", 1, new RuntimeException(), repository
    );
    assertEquals(2, ships.size());
    assertEquals(Set.of("ship-1", "ship-2"), ships.stream().map(Ship::getName).collect(Collectors.toSet()));

    when(repository.getByUniqueField("ship-2")).thenThrow(
        new NotFoundException("ship-2 not found")
    );

    RuntimeException runtimeException = new RuntimeException();
    ships = ConversionUtils.delimitedObjectsFromMap(
        map, "test-property", 1, runtimeException, repository
    );
    assertEquals(1, ships.size());
    assertEquals(1, runtimeException.getSuppressed().length);
    Throwable throwable = runtimeException.getSuppressed()[0];
    assertInstanceOf(FieldException.class, throwable);
    FieldException fieldException = (FieldException) throwable;
    assertEquals("ship-2 not found", fieldException.getMessage());
    
    ships = ConversionUtils.delimitedObjectsFromMap(map, "test-other-property", 1, new RuntimeException(), repository);
    assertEquals(0, ships.size());
  }
}