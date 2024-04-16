package edu.colorado.cires.pace.data.object;

public interface DatasetDetail {
  
  Platform getPlatform();
  Instrument getInstrument();
  TimeRange getTimeRange();

}
