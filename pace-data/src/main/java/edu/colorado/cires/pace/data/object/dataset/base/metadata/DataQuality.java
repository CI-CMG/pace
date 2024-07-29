package edu.colorado.cires.pace.data.object.dataset.base.metadata;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DataQualityEntry;
import java.util.List;

public interface DataQuality<T> {
  
  T getQualityAnalyst();
  String getQualityAnalysisObjectives();
  String getQualityAnalysisMethod();
  String getQualityAssessmentDescription();
  List<DataQualityEntry> getQualityEntries();
  
  DataQuality<T> setQualityAnalyst(T qualityAnalyst);

}
