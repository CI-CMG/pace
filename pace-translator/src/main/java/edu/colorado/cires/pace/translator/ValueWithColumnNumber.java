package edu.colorado.cires.pace.translator;

import java.util.Optional;
import org.jetbrains.annotations.Nullable;

public record ValueWithColumnNumber(Optional<@Nullable String> value, @Nullable Integer column) {}
