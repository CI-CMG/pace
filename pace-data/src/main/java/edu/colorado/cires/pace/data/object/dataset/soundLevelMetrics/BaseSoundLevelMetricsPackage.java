package edu.colorado.cires.pace.data.object.dataset.soundLevelMetrics;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.AnalysisDescription;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.AudioTimeRange;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.DataQuality;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.SoftwareDescription;

public interface BaseSoundLevelMetricsPackage<T> extends AnalysisDescription, DataQuality<T>, SoftwareDescription, AudioTimeRange {

}