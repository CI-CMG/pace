package edu.colorado.cires.pace.data.object;

import java.time.LocalDateTime;
import java.util.List;

public interface Channel {
  
  Sensor getSensor();
  LocalDateTime getStartTime();
  LocalDateTime getEndTime();
  List<SampleRate> getSampleRates();
  List<DutyCycle> getDutyCycles();
  List<Gain> getGains();

}
