package edu.colorado.cires.pace.translator;

import static edu.colorado.cires.pace.translator.DatasetType.AUDIO;
import static edu.colorado.cires.pace.translator.DatasetType.CPOD;
import static edu.colorado.cires.pace.translator.DatasetType.DETECTIONS;
import static edu.colorado.cires.pace.translator.DatasetType.SOUND_CLIPS;
import static edu.colorado.cires.pace.translator.DatasetType.SOUND_LEVEL_METRICS;
import static edu.colorado.cires.pace.translator.DatasetType.SOUND_PROPAGATION_MODELS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import edu.colorado.cires.pace.data.object.dataset.audio.AudioPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.CPODPackage;
import edu.colorado.cires.pace.data.object.dataset.detections.DetectionsPackage;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.data.object.dataset.soundClips.SoundClipsPackage;
import edu.colorado.cires.pace.data.object.dataset.soundLevelMetrics.SoundLevelMetricsPackage;
import edu.colorado.cires.pace.data.object.dataset.soundPropagationModels.SoundPropagationModelsPackage;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class DatasetTypeTest {

  @ParameterizedTest
  @CsvSource(value = {
      "sound clips,SOUND_CLIPS",
      "audio,AUDIO",
      "CPOD,CPOD",
      "detections,DETECTIONS",
      "sound level metrics,SOUND_LEVEL_METRICS",
      "sound propagation models,SOUND_PROPAGATION_MODELS",
  })
  void fromName(String name, DatasetType expectedDatasetType) {
    assertEquals(expectedDatasetType, DatasetType.fromName(name));
  }
  
  @ParameterizedTest
  @ValueSource(classes = {
      AudioPackage.class,
      CPODPackage.class,
      DetectionsPackage.class,
      SoundClipsPackage.class,
      SoundLevelMetricsPackage.class,
      SoundPropagationModelsPackage.class,
      Package.class
  })
  void fromPackage(Class<? extends Package> clazz) {
    DatasetType expectedType = switch (clazz.getSimpleName()) {
      case "AudioPackage" -> AUDIO;
      case "CPODPackage" -> CPOD;
      case "DetectionsPackage" -> DETECTIONS;
      case "SoundClipsPackage" -> SOUND_CLIPS;
      case "SoundLevelMetricsPackage" -> SOUND_LEVEL_METRICS;
      case "SoundPropagationModelsPackage" -> SOUND_PROPAGATION_MODELS;
      case "Package" -> null;
      default -> throw new IllegalStateException("Unexpected value: " + clazz.getSimpleName());
    };

    if (expectedType == null) {
      Exception exception = assertThrows(IllegalArgumentException.class, () -> DatasetType.fromPackage(mock(clazz)));
      assertTrue(exception.getMessage().contains("Invalid dataset type"));
    } else {
      assertEquals(expectedType, DatasetType.fromPackage(mock(clazz)));
    }
  }
  
  @Test
  void fromNameInvalid() {
    String name = "invalid";
    Exception exception = assertThrows(IllegalArgumentException.class, () -> DatasetType.fromName(name));
    assertEquals(String.format(
        "Invalid dataset type: %s. Was not one of: %s",
        name,
        Arrays.stream(DatasetType.values())
            .map(DatasetType::getName)
            .collect(Collectors.joining(", "))
    ), exception.getMessage());
  }
}