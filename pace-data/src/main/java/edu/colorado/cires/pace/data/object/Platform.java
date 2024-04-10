package edu.colorado.cires.pace.data.object;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class Platform implements ObjectWithName {

  final UUID uuid;
  final String name;

}
