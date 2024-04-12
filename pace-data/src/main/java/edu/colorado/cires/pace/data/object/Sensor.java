package edu.colorado.cires.pace.data.object;

public interface Sensor extends ObjectWithName {
  String getName();
  Position getPosition();
  String getDescription();
}
