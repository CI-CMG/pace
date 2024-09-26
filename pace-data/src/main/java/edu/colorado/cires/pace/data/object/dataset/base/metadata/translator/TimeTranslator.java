package edu.colorado.cires.pace.data.object.dataset.base.metadata.translator;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;

/**
 * TimeTranslator provides the structure for JSON translators for time
 * and provides getters for time fields
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = DefaultTimeTranslator.class, name = "default"),
    @JsonSubTypes.Type(value = DateTimeSeparatedTimeTranslator.class, name = "date time separated"),
    @JsonSubTypes.Type(value = DateOnlyTimeTranslator.class, name = "date only")
})
public interface TimeTranslator {

  /**
   * Returns time
   * @return String time
   */
  @NotBlank
  String getTime();

  /**
   * Returns time zone
   * @return String time zone
   */
  @NotBlank
  String getTimeZone();

}
