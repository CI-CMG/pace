package edu.colorado.cires.pace.data.object;

public interface DetectionsDatasetDetail extends DatasetDetail, DataQuality, SoftwareDescription, AnalysisDescription {
  SoundSource getSoundSource();
}
