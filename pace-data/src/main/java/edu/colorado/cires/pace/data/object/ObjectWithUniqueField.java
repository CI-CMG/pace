package edu.colorado.cires.pace.data.object;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface ObjectWithUniqueField extends ObjectWithUUID {
  
  @JsonIgnore
  String getUniqueField();

}
