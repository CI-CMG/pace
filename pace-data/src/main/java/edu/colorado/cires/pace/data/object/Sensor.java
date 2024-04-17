package edu.colorado.cires.pace.data.object;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = AudioSensor.class, name = "audio"),
    @JsonSubTypes.Type(value = DepthSensor.class, name = "depth"),
    @JsonSubTypes.Type(value = OtherSensor.class, name = "other")
})
public interface Sensor extends ObjectWithName {
  @NotNull @Valid
  Position getPosition();
  String getDescription();
}
