package edu.colorado.cires.pace.translator;

import java.util.Optional;
import org.jetbrains.annotations.Nullable;

/**
 * Holds both value and column number
 * @param value value to hold
 * @param column column to holds
 */
public record ValueWithColumnNumber(Optional<@Nullable String> value, @Nullable Integer column) {}
