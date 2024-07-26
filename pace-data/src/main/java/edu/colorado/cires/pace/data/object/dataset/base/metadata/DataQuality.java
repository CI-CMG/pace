package edu.colorado.cires.pace.data.object.dataset.base.metadata;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DataQualityEntry;
import java.util.List;

public interface DataQuality {
  
  String getQualityAnalyst();
  String getQualityAnalysisObjectives();
  String getQualityAnalysisMethod();
  String getQualityAssessmentDescription();
  List<DataQualityEntry> getQualityEntries();
  
  DataQuality setQualityAnalyst(String qualityAnalyst);

}
