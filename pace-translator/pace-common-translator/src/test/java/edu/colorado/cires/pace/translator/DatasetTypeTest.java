package edu.colorado.cires.pace.translator;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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