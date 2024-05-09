package edu.colorado.cires.pace.translator.excel;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.pace.data.object.ExcelTranslator;
import edu.colorado.cires.pace.data.object.ExcelTranslatorField;
import edu.colorado.cires.pace.data.object.Ship;
import edu.colorado.cires.pace.translator.FieldNameFactory;
import java.util.List;
import org.junit.jupiter.api.Test;

class ExcelTranslatorFactoryTest {

  @Test
  void createTranslator() {
    ExcelTranslator translator = ExcelTranslatorFactory.createTranslator("test-excel-translator", () -> FieldNameFactory.getDefaultDeclaredFields(Ship.class));
    
    assertEquals("test-excel-translator", translator.getName());
    assertEquals(List.of("uuid", "name"), translator.getFields().stream().map(ExcelTranslatorField::getPropertyName).toList());
    assertEquals(List.of(1, 2), translator.getFields().stream().map(ExcelTranslatorField::getColumnNumber).toList());
    assertEquals(List.of(1, 1), translator.getFields().stream().map(ExcelTranslatorField::getSheetNumber).toList());
  }
}