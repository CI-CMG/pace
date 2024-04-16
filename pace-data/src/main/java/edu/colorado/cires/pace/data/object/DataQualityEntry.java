package edu.colorado.cires.pace.data.object;

public interface DataQualityEntry extends TimeRange, FrequencyRange {
  
  QualityLevel getQualityLevel();
  String getComments();

}
