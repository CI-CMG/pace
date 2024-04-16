package edu.colorado.cires.pace.data.object;

public interface SoundPropagationModelsDatasetDetail extends DatasetDetail, SoftwareDescription, AudioTimeRange {
  Float getModeledFrequency();
}
