package edu.colorado.cires.pace.data.object;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NAME, include = As.EXTERNAL_PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = CSVTranslatorFieldImpl.class, name = "field"),
    @JsonSubTypes.Type(value = CSVResourceTranslatorField.class, name = "resource")
})
public interface CSVTranslatorField extends TabularTranslationField {
}
