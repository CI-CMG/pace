package edu.colorado.cires.pace.translator;

public record ObjectWithRowConversionException<O>(O object, int row, RowConversionException rowConversionException) {}
