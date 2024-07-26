package edu.colorado.cires.pace.data.object.dataset.base.metadata.location;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = StationaryMarineLocation.class, name = "stationary marine"),
    @JsonSubTypes.Type(value = MultiPointStationaryMarineLocation.class, name = "multipoint stationary marine"),
    @JsonSubTypes.Type(value = MobileMarineLocation.class, name = "mobile marine"),
    @JsonSubTypes.Type(value = StationaryTerrestrialLocation.class, name = "stationary terrestrial")
})
public interface LocationDetail {

}
