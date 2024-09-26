package edu.colorado.cires.pace.data.object.dataset.base;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import edu.colorado.cires.pace.data.object.dataset.audio.AudioPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.CPODPackage;
import edu.colorado.cires.pace.data.object.dataset.detections.DetectionsPackage;
import edu.colorado.cires.pace.data.object.dataset.soundClips.SoundClipsPackage;
import edu.colorado.cires.pace.data.object.dataset.soundLevelMetrics.SoundLevelMetricsPackage;
import edu.colorado.cires.pace.data.object.dataset.soundPropagationModels.SoundPropagationModelsPackage;
import edu.colorado.cires.pace.data.validation.ValidPackageIdentifiers;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * Package provides the JSON format for packages
 */
@JsonTypeInfo(use = Id.NAME, include = As.EXTERNAL_PROPERTY, property = "type")
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
@ValidPackageIdentifiers
public class Package extends BasePackage {

  /**
   * Returns a new package with the provided uuid set
   *
   * @param uuid field for assigning uuid to new object
   * @return Package with provided uuid set
   */
  public Package setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  /**
   * Returns a new package with the provided visibility set
   *
   * @param visible boolean which indicates whether to make the object visible
   *                or invisible
   * @return Package with provided visibility set
   */
  public Package setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
