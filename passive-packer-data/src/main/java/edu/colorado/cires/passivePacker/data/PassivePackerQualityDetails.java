package edu.colorado.cires.passivePacker.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode
@Builder(toBuilder = true)
@Jacksonized
public class PassivePackerQualityDetails {
  @JsonProperty("analyst")
  private final String analyst;
  @JsonProperty("analyst_uuid")
  private final UUID analystUuid;
  @JsonProperty("quality_details")
  private final List<PassivePackerQualityEntry> qualityDetails;
  @JsonProperty("method")
  private final String method;
  @JsonProperty("objectives")
  private final String objectives;
  @JsonProperty("abstract")
  private final String description;

}
