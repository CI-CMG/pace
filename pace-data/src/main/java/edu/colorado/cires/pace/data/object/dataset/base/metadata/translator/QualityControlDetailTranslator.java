package edu.colorado.cires.pace.data.object.dataset.base.metadata.translator;

import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * QualityControlDetailTranslator holds onto all relevant quality
 * headers for translation
 */
@Data
@Builder(toBuilder = true)
@Jacksonized
public class QualityControlDetailTranslator {

  private final String qualityAnalyst;
  private final String qualityAnalysisObjectives;
  private final String qualityAnalysisMethod;
  private final String qualityAssessmentDescription;
  @Builder.Default
  private final List<DataQualityEntryTranslator> qualityEntryTranslators = Collections.emptyList();

}
