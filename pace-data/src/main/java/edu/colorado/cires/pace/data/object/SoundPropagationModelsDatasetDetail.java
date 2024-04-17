package edu.colorado.cires.pace.data.object;

import jakarta.validation.constraints.Positive;

public interface SoundPropagationModelsDatasetDetail extends DatasetDetail, SoftwareDescription, AudioTimeRange {
  @Positive
  Float getModeledFrequency();
}
