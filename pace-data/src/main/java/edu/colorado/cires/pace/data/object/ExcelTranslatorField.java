package edu.colorado.cires.pace.data.object;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import jakarta.validation.constraints.Positive;

@JsonTypeInfo(use = Id.NAME, include = As.EXTERNAL_PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ExcelTranslatorFieldImpl.class, name = "field"),
    @JsonSubTypes.Type(value = ExcelResourceTranslatorField.class, name = "resource")
})
public interface ExcelTranslatorField extends TabularTranslationField {
  @Positive
  int getSheetNumber();
}
