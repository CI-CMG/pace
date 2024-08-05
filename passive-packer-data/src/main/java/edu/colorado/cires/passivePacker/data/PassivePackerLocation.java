package edu.colorado.cires.passivePacker.data;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
@Jacksonized
@JsonTypeInfo(use = Id.DEDUCTION)
@JsonSubTypes({
    @Type(PassivePackerStationaryMarineLocation.class),
    @Type(PassivePackerMobileMarineLocation.class),
    @Type(PassivePackerMultipointStationaryMarineLocation.class),
    @Type(PassivePackerStationaryTerrestrialLocation.class)
})
public class PassivePackerLocation {
  
  private final String deployType;
  
  public <L extends PassivePackerLocation, B extends PassivePackerLocation.PassivePackerLocationBuilder<L, ?>> L toInheritingType(B inheritingTypeBuilder) {
    return inheritingTypeBuilder
        .deployType(getDeployType())
        .build();
  }

}
