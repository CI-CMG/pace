package edu.colorado.cires.pace.core.state.repository;

@FunctionalInterface
public interface UniqueFieldProvider<O, U> {
  U getUniqueField(O object);
}
