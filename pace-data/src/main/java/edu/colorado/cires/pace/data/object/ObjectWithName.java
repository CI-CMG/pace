package edu.colorado.cires.pace.data.object;

public interface ObjectWithName extends ObjectWithUniqueField {
  
  String getName();

  
  @Override
  default String getUniqueField() {
    return getName();
  }
}
