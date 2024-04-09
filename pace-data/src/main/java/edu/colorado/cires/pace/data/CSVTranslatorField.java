package edu.colorado.cires.pace.data;

public record CSVTranslatorField(String propertyName, int columnNumber) implements TabularTranslationField {}
