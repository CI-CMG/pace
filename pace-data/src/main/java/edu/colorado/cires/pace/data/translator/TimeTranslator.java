package edu.colorado.cires.pace.data.translator;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotNull;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = DefaultTimeTranslator.class, name = "default"),
    @JsonSubTypes.Type(value = DateTimeSeparatedTimeTranslator.class, name = "date time separated"),
    @JsonSubTypes.Type(value = DateTranslator.class, name = "date")
})
public interface TimeTranslator {
  @NotNull
  String getTimeZone();

}
