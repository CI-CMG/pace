package edu.colorado.cires.pace.data.object;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotNull;
import java.nio.file.Path;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@JsonTypeInfo(use = Id.NAME, include = As.EXTERNAL_PROPERTY, property = "datasetType")
@JsonSubTypes({
    @Type(value = AudioPackage.class, name = "audio"),
    @Type(value = CPODPackage.class, name = "CPOD"),
    @Type(value = DetectionsPackage.class, name = "detections"),
    @Type(value = SoundClipsPackage.class, name = "sound clips"),
    @Type(value = SoundLevelMetricsPackage.class, name = "sound level metrics"),
    @Type(value = SoundPropagationModelsPackage.class, name = "sound propagation models"),
})
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public abstract class Package extends Dataset {

  @JsonView(Package.class)
  private final UUID uuid;
  @JsonView(Package.class)
  private final Path temperaturePath;
  @JsonView(Package.class)
  private final Path biologicalPath;
  @JsonView(Package.class)
  private final Path otherPath;
  @JsonView(Package.class)
  private final Path documentsPath;
  @JsonView(Package.class)
  private final Path calibrationDocumentsPath;
  @JsonView(Package.class)
  private final Path navigationPath;
  @JsonView(Package.class) @NotNull
  private final Path sourcePath;

}
