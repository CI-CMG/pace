package edu.colorado.cires.pace.data.object.dataset.base.metadata;

import lombok.Getter;

/**
 * QualityLevel outlines the types of possible quality levels
 */
@Getter
public enum QualityLevel {
  
  unverified("Unverified"),
  good("Good"),
  compromised("Compromised"),
  unusable("Unusable");

  private final String name;

  /**
   * Sets the name of the quality level
   * @param name quality level to set
   */
  QualityLevel(String name) {
    this.name = name;
  }

  /**
   * Returns new quality level based on input name
   * @param name quality level in string
   * @return QualityLevel representing provided string
   */
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
