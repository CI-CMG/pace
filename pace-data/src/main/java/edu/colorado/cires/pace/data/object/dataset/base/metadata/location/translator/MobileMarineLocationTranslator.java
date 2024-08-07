package edu.colorado.cires.pace.data.object.dataset.base.metadata.location.translator;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class MobileMarineLocationTranslator implements LocationDetailTranslator {
  
  private final String seaArea;
  private final String vessel;
  private final String locationDerivationDescription;
  private final String singleStringFiles;

}
