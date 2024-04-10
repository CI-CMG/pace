package edu.colorado.cires.pace.data.validation;

public record ConstraintViolation(String property, String message) {}
