package edu.colorado.cires.pace.data.object.dataset.base.metadata;

public interface AnalysisDescription extends FrequencyRange {

  Integer getAnalysisTimeZone();
  Integer getAnalysisEffort();
  Float getSampleRate();

}
