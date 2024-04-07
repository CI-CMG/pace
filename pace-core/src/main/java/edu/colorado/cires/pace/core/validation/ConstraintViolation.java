package edu.colorado.cires.pace.core.validation;

public record ConstraintViolation(String property, String message) {}
