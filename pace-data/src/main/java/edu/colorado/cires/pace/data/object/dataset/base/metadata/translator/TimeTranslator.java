package edu.colorado.cires.pace.data.object.dataset.base.metadata.translator;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = DefaultTimeTranslator.class, name = "default"),
    @JsonSubTypes.Type(value = DateTimeSeparatedTimeTranslator.class, name = "date time separated"),
    @JsonSubTypes.Type(value = DateOnlyTimeTranslator.class, name = "date only")
})
public interface TimeTranslator {
  @NotBlank
  String getTime();
  @NotBlank
  String getTimeZone();

}
