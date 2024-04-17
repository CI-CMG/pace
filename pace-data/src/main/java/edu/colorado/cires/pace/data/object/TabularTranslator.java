package edu.colorado.cires.pace.data.object;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public interface TabularTranslator<T extends TabularTranslationField> extends ObjectWithName {
  
  @NotNull @NotEmpty
  List<@Valid T> getFields();

}
