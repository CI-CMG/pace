package edu.colorado.cires.pace.data.object.dataset.passivePacker;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class PassivePackerQualityEntry extends PassivePackerTimeRange {
  @JsonProperty("start")
  private final LocalDateTime start;
  @JsonProperty("end")
  private final LocalDateTime end;
  @JsonProperty("quality")
  private final String quality;
  @JsonProperty("low_freq")
  private final String lowFreq;
  @JsonProperty("high_freq")
  private final String highFreq;
  @JsonProperty("comments")
  private final String comments;
  @JsonProperty("channels")
  private final String channels;
}
