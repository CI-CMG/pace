package edu.colorado.cires.pace.data.object;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface DetectionsDatasetDetail extends DatasetDetail, DataQuality, SoftwareDescription, AnalysisDescription {
  @NotNull @Valid
  DetectionType getSoundSource();
}
