package edu.colorado.cires.passivePacker.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
@JsonTypeInfo(use = Id.DEDUCTION, defaultImpl = PassivePackerDepthSensor.class)
@JsonSubTypes({
    @Type(PassivePackerAudioSensor.class),
    @Type(PassivePackerDepthSensor.class),
    @Type(PassivePackerOtherSensor.class)
})
public abstract class PassivePackerSensor {
  @JsonProperty("type")
  private final String type;
  @JsonProperty("number")
  private final String number;
  @JsonProperty("id")
  private final String id;
  @JsonProperty("name")
  private final String name;
  @JsonProperty("pos_x")
  private final String positionX;
  @JsonProperty("pos_y")
  private final String positionY;
  @JsonProperty("pos_z")
  private final String positionZ;
  @JsonProperty("description")
  private final String description;

  public <S extends PassivePackerSensor, B extends PassivePackerSensor.PassivePackerSensorBuilder<S, ?>> S toInheritingType(B inheritingTypeBuilder) {
    return inheritingTypeBuilder
        .type(getType())
        .number(getNumber())
        .id(getId())
        .name(getName())
        .positionX(getPositionX())
        .positionY(getPositionY())
        .positionZ(getPositionZ())
        .description(getDescription())
        .build();
  }
}
