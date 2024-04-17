package edu.colorado.cires.pace.data.object;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class QualityLevelTest {
  
  @Test
  void testResolveQualityLevelFromName() {
    assertEquals(QualityLevel.unverified, QualityLevel.fromName("Unverified"));
    assertEquals(QualityLevel.good, QualityLevel.fromName("Good"));
    assertEquals(QualityLevel.compromised, QualityLevel.fromName("Compromised"));
    assertEquals(QualityLevel.unusable, QualityLevel.fromName("Unusable"));
    
    Exception exception = assertThrows(IllegalStateException.class, () -> QualityLevel.fromName("good"));
    assertEquals("Unsupported quality level value: good", exception.getMessage());
  }

}
