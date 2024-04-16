package edu.colorado.cires.pace.data.object;

public interface AnalysisDescription extends FrequencyRange {

  Integer getAnalysisTimeZone();
  Integer getAnalysisEffort();
  Float getSampleRate();

}
