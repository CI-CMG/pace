package edu.colorado.cires.pace.translator;

public record ObjectWithRowException<O>(O object, int row, java.lang.Throwable throwable) {}
