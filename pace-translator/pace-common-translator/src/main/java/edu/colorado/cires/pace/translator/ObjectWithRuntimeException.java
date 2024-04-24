package edu.colorado.cires.pace.translator;

public record ObjectWithRuntimeException<O>(O object, RuntimeException runtimeException) {}
