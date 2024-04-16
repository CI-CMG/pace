package edu.colorado.cires.pace.data.object;

public interface PackJob<DD extends DatasetDetailWithPath, LD extends LocationDetailWithPath, CD extends CalibrationDetailWithPath, DDesc extends DatasetDescriptionWithPath>
 extends Dataset<DD, LD, CD, DDesc> {

  AncillaryData getAncillaryData();
  
}
