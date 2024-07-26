package edu.colorado.cires.pace.data.object.dataset.base.metadata;

import lombok.Getter;

@Getter
public enum QualityLevel {
  
  unverified("Unverified"),
  good("Good"),
  compromised("Compromised"),
  unusable("Unusable");

  private final String name;

  QualityLevel(String name) {
    this.name = name;
  }
  
  public static QualityLevel fromName(String name) {
    return switch (name) {
      case "Unverified" -> QualityLevel.unverified;
      case "Good" -> QualityLevel.good;
      case "Compromised" -> QualityLevel.compromised;
      case "Unusable" -> QualityLevel.unusable;
      default -> throw new IllegalStateException("Unsupported quality level value: " + name);
    };
  }

}
