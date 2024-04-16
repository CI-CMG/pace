package edu.colorado.cires.pace.data.object;

import java.util.List;

public interface DataQuality {
  
  Person getQualityAnalyst();
  String getQualityAnalysisObjectives();
  String getQualityAnalysisMethod();
  String getQualityAssessmentDescription();
  List<DataQualityEntry> getQualityEntries();

}
