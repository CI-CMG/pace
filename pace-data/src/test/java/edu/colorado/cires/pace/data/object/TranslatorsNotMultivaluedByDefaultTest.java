package edu.colorado.cires.pace.data.object;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TranslatorsNotMultivaluedByDefaultTest {
  
  @Test
  void testTranslatorsNotMultivaluedByDefault() {
    assertFalse(CSVTranslatorFieldImpl.builder().build().isMultiValued());
    assertFalse(CSVResourceTranslatorField.builder().build().isMultiValued());
    assertFalse(ExcelTranslatorFieldImpl.builder().build().isMultiValued());
    assertFalse(ExcelResourceTranslatorField.builder().build().isMultiValued());

    assertTrue(CSVTranslatorFieldImpl.builder()
        .multiValued(true)
        .build().isMultiValued());
    assertTrue(CSVResourceTranslatorField.builder()
        .multiValued(true)
        .build().isMultiValued());
    assertTrue(ExcelTranslatorFieldImpl.builder()
        .multiValued(true)
        .build().isMultiValued());
    assertTrue(ExcelResourceTranslatorField.builder()
        .multiValued(true)
        .build().isMultiValued());

    assertFalse(CSVTranslatorFieldImpl.builder()
        .multiValued(false)
        .build().isMultiValued());
    assertFalse(CSVResourceTranslatorField.builder()
        .multiValued(false)
        .build().isMultiValued());
    assertFalse(ExcelTranslatorFieldImpl.builder()
        .multiValued(false)
        .build().isMultiValued());
    assertFalse(ExcelResourceTranslatorField.builder()
        .multiValued(false)
        .build().isMultiValued());
  } 

}
