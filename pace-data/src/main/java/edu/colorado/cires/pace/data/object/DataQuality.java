package edu.colorado.cires.pace.data.object;

import java.util.List;

public interface DataQuality {
  
  String getQualityAnalyst();
  String getQualityAnalysisObjectives();
  String getQualityAnalysisMethod();
  String getQualityAssessmentDescription();
  List<DataQualityEntry> getQualityEntries();

}
