package edu.colorado.cires.pace.data.object.sensor.base;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.colorado.cires.pace.data.object.base.ObjectWithName;
import edu.colorado.cires.pace.data.object.sensor.audio.AudioSensor;
import edu.colorado.cires.pace.data.object.sensor.depth.DepthSensor;
import edu.colorado.cires.pace.data.object.sensor.other.OtherSensor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = AudioSensor.class, name = "audio"),
    @JsonSubTypes.Type(value = DepthSensor.class, name = "depth"),
    @JsonSubTypes.Type(value = OtherSensor.class, name = "other")
})
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public abstract class Sensor extends ObjectWithName {
  private final String description;
}
