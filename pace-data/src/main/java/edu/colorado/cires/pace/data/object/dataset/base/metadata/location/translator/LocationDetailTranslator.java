package edu.colorado.cires.pace.data.object.dataset.base.metadata.location.translator;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * LocationDetailTranslator provides the outline for JSON files of location translators
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = StationaryMarineLocationTranslator.class, name = "stationary marine"),
    @JsonSubTypes.Type(value = MultipointStationaryMarineLocationTranslator.class, name = "multipoint stationary marine"),
    @JsonSubTypes.Type(value = MobileMarineLocationTranslator.class, name = "mobile marine"),
    @JsonSubTypes.Type(value = StationaryTerrestrialLocationTranslator.class, name = "stationary terrestrial")
})
public interface LocationDetailTranslator {}
