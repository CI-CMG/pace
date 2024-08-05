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
  // TODO: Add file list mapping (String, value is semi-colon delimited): file-1.txt;file-2.txt;file-3.txt

}
