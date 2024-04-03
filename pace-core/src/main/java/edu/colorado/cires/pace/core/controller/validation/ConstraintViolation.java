package edu.colorado.cires.pace.core.controller.validation;

public record ConstraintViolation(String property, String message) {}
