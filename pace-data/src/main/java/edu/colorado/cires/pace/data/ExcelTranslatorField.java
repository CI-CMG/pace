package edu.colorado.cires.pace.data;

public record ExcelTranslatorField(String propertyName, int columnNumber, int sheetNumber) implements TabularTranslationField {}
