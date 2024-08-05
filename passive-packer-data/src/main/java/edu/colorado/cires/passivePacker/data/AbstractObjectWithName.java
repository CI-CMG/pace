package edu.colorado.cires.passivePacker.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class AbstractObjectWithName {
  @JsonProperty("name")
  private final String name;
  @JsonProperty("uuid")
  private final UUID uuid;
}
