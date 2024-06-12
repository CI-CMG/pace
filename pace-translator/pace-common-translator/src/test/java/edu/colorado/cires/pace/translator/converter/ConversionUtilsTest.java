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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

    RuntimeException runtimeException = new RuntimeException();
    LocalDate result = ConversionUtils.localDateFromMap(map, "test-property", 2, runtimeException);
    assertEquals(result, LocalDate.parse("2024-01-01"));

    map.put("test-property", new ValueWithColumnNumber(Optional.of("test"), 1));
    runtimeException = new RuntimeException();
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
  
  @ParameterizedTest
  @ValueSource(strings = {
      "2024-01-01",
      "2024-01-01T01:02:03",
      "2024-01-01 01:02:03",
      "2024-01-01T01:02:03Z",
      "01/01/2024",
      "01/01/2024 01:02:03",
      "1/1/24",
      "1/1/24 01:02:03",
      "1/1/2024",
      "1/1/2024 01:02:03"
  })
  void testDateFormats(String inputDate) {
    RuntimeException runtimeException = new RuntimeException();
    LocalDate result = ConversionUtils.localDateFromMap(
        Map.of(
            "date", new ValueWithColumnNumber(Optional.of(inputDate), 1)
        ),
        "date",
        1,
        runtimeException
    );
    assertNotNull(result);
    assertEquals(LocalDate.parse("2024-01-01"), result);
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "2024-01-01T01:02:03",
      "2024-01-01 01:02:03",
      "2024-01-01T01:02:03Z",
      "01/01/2024 01:02:03",
      "1/1/24 01:02:03",
      "1/1/2024 01:02:03"
  })
  void testDateTimeFormats(String inputDate) {
    RuntimeException runtimeException = new RuntimeException();
    LocalDateTime result = ConversionUtils.localDateTimeFromMap(
        Map.of(
            "date", new ValueWithColumnNumber(Optional.of(inputDate), 1)
        ),
        DefaultTimeTranslator.builder()
            .time("date")
            .build(),
        1,
        runtimeException
    );
    assertNotNull(result);
    assertEquals(LocalDateTime.parse("2024-01-01T01:02:03"), result);
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
  
  @ParameterizedTest
  @ValueSource(strings = {
      "150°35'21\"E",
      "150°35'21\"W",
      "150° 35' 21\" E",
      "150° 35' 21\" W",
      "150D35M21SE",
      "150D35M21SW",
      "150D 35M 21S E",
      "150D 35M 21S W",
      "150d35m21sE",
      "150d35m21sW",
      "150d 35m 21s E",
      "150d 35m 21s W",
      "150.589167",
      "-150.589167",
      "150°35.35'E",
      "150°35.35'W",
      "150° 35.35' E",
      "150° 35.35' W",
      "150D35.35ME",
      "150D35.35MW",
      "150D 35.35M E",
      "150D 35.35M W",
      "150d35.35mE",
      "150d35.35mW",
      "150d 35.35m E",
      "150d 35.35m W",
  })
  void testConvertDMSToDecimalDegreesLongitude(String dmsString) {
    double expected = 150.589167;

    assertEquals(
        (dmsString.endsWith("W") || dmsString.startsWith("-")) ? expected * -1 : expected,
        ConversionUtils.longitudeFromMap(
            Map.of("lon", new ValueWithColumnNumber(Optional.of(dmsString), 2)),
            "lon",
            1,
            new RuntimeException()
        )
    );
  }
  
  @ParameterizedTest
  @ValueSource(strings = {
    "30°35'21\"N",
    "30°35'21\"S",
    "30° 35' 21\" N",
    "30° 35' 21\" S",
    "30D35M21SN",
    "30D35M21SS",
    "30D 35M 21S N",
    "30D 35M 21S S",
    "30d35m21sN",
    "30d35m21sS",
    "30d 35m 21s N",
    "30d 35m 21s S",
    "30.589167",
    "-30.589167",
    "30°35.35'N",
    "30°35.35'S",
    "30° 35.35' N",
    "30° 35.35' S",
    "30D35.35MN",
    "30D35.35MS",
    "30D 35.35M N",
    "30D 35.35M S",
    "30d35.35mN",
    "30d35.35mS",
    "30d 35.35m N",
    "30d 35.35m S",
  })
  void testConvertDMSToDecimalDegreesLatitude(String dmsString) {
    double expected = 30.589167;
    assertEquals(
        (dmsString.endsWith("S") || dmsString.startsWith("-")) ? expected * -1 : expected,
        ConversionUtils.latitudeFromMap(
            Map.of("lat", new ValueWithColumnNumber(Optional.of(dmsString), 2)),
            "lat",
            1,
            new RuntimeException()
        )
    );
  }
}