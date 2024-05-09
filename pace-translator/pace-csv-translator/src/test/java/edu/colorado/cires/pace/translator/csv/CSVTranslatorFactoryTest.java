package edu.colorado.cires.pace.translator.csv;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.pace.data.object.CSVTranslator;
import edu.colorado.cires.pace.data.object.CSVTranslatorField;
import edu.colorado.cires.pace.data.object.Ship;
import edu.colorado.cires.pace.translator.FieldNameFactory;
import java.util.List;
import org.junit.jupiter.api.Test;

class CSVTranslatorFactoryTest {

  @Test
  void createTranslator() {
    CSVTranslator translator = CSVTranslatorFactory.createTranslator("test-csv-translator", () -> FieldNameFactory.getDefaultDeclaredFields(
        Ship.class));

    assertEquals("test-csv-translator", translator.getName());
    assertEquals(List.of("uuid", "name"), translator.getFields().stream().map(CSVTranslatorField::getPropertyName).toList());
    assertEquals(List.of(1, 2), translator.getFields().stream().map(CSVTranslatorField::getColumnNumber).toList());
  }
}