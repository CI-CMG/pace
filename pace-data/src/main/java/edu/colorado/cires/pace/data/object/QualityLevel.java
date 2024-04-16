package edu.colorado.cires.pace.data.object;

public enum QualityLevel {
  
  unverified("Unverified"),
  good("Good"),
  compromised("Compromised"),
  unusable("Unusable");

  private final String name;

  QualityLevel(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
