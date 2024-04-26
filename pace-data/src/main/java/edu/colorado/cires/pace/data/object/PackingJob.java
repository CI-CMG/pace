package edu.colorado.cires.pace.data.object;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonView;
import edu.colorado.cires.pace.data.SoundPropagationModelsPackingJob;
import jakarta.validation.constraints.NotNull;
import java.nio.file.Path;

@JsonTypeInfo(use = Id.NAME, include = As.EXTERNAL_PROPERTY, property = "datasetType")
@JsonSubTypes({
    @Type(value = AudioPackingJob.class, name = "audio"),
    @Type(value = CPODPackingJob.class, name = "CPOD"),
    @Type(value = DetectionsPackingJob.class, name = "detections"),
    @Type(value = SoundClipsPackingJob.class, name = "sound clips"),
    @Type(value = SoundLevelMetricsPackingJob.class, name = "sound level metrics"),
    @Type(value = SoundPropagationModelsPackingJob.class, name = "sound propagation models"),
})
public interface PackingJob {
  
  @JsonView(PackingJob.class)
  Path getTemperaturePath();
  @JsonView(PackingJob.class)
  Path getBiologicalPath();
  @JsonView(PackingJob.class)
  Path getOtherPath();
  @JsonView(PackingJob.class)
  Path getDocumentsPath();
  @JsonView(PackingJob.class)
  Path getCalibrationDocumentsPath();
  @JsonView(PackingJob.class)
  Path getNavigationPath();
  @JsonView(PackingJob.class)
  @NotNull 
  Path getSourcePath();

}
