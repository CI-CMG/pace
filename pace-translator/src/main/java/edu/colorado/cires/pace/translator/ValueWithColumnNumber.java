package edu.colorado.cires.pace.translator;

import java.util.Optional;

public record ValueWithColumnNumber(Optional<String> value, Integer column) {}
