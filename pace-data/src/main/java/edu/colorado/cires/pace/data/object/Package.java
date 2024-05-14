package edu.colorado.cires.pace.data.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonView;
import edu.colorado.cires.pace.data.SoundPropagationModelsPackage;
import jakarta.validation.constraints.NotNull;
import java.nio.file.Path;
import java.util.UUID;

@JsonTypeInfo(use = Id.NAME, include = As.EXTERNAL_PROPERTY, property = "datasetType")
@JsonSubTypes({
    @Type(value = AudioPackage.class, name = "audio"),
    @Type(value = CPODPackage.class, name = "CPOD"),
    @Type(value = DetectionsPackage.class, name = "detections"),
    @Type(value = SoundClipsPackage.class, name = "sound clips"),
    @Type(value = SoundLevelMetricsPackage.class, name = "sound level metrics"),
    @Type(value = SoundPropagationModelsPackage.class, name = "sound propagation models"),
})
public interface Package extends ObjectWithUniqueField {

  @Override
  @JsonView(Package.class)
  UUID getUuid();
  @JsonView(Package.class)
  Path getTemperaturePath();
  @JsonView(Package.class)
  Path getBiologicalPath();
  @JsonView(Package.class)
  Path getOtherPath();
  @JsonView(Package.class)
  Path getDocumentsPath();
  @JsonView(Package.class)
  Path getCalibrationDocumentsPath();
  @JsonView(Package.class)
  Path getNavigationPath();
  @JsonView(Package.class)
  @NotNull 
  Path getSourcePath();
  @JsonIgnore
  String getPackageId();
  @JsonIgnore
  LocationDetail getLocationDetail();

}
