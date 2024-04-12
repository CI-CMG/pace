package edu.colorado.cires.pace.data.validation;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class ConstraintViolation {
  private final String property;
  private final String message;
}
