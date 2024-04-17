package edu.colorado.cires.pace.data.object;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = StationaryMarineLocation.class, name = "Stationary Marine"),
    @JsonSubTypes.Type(value = MultiPointStationaryMarineLocation.class, name = "Multipoint Stationary Marine"),
    @JsonSubTypes.Type(value = MobileMarineLocation.class, name = "Mobile Marine"),
    @JsonSubTypes.Type(value = StationaryTerrestrialLocation.class, name = "Stationary Terrestrial")
})
public interface LocationDetail {

}
