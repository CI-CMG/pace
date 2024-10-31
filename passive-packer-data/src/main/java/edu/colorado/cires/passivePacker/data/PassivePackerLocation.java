package edu.colorado.cires.passivePacker.data;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * PassivePackerLocation provides builder structure for PassivePackerLocation objects
 * and provides functionality for passing object to inheriting location builder
 */
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

  /**
   * Passes location to inheriting builder
   * @param inheritingTypeBuilder builder to pass to
   * @return Location object of inheriting type
   * @param <L> Location type
   * @param <B> Builder type
   */
  public <L extends PassivePackerLocation, B extends PassivePackerLocation.PassivePackerLocationBuilder<L, ?>> L toInheritingType(B inheritingTypeBuilder) {
    return inheritingTypeBuilder
        .deployType(getDeployType())
        .build();
  }

}
