package edu.colorado.cires.pace.data.object;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NAME, include = As.EXTERNAL_PROPERTY, property = "datasetType")
@JsonSubTypes({
    @Type(value = AudioDataset.class, name = "audio"),
    @Type(value = CPodDataset.class, name = "CPOD"),
    @Type(value = DetectionsDataset.class, name = "detections"),
    @Type(value = SoundClipsDataset.class, name = "sound clips"),
    @Type(value = SoundLevelMetricsDataset.class, name = "sound level metrics"),
    @Type(value = SoundPropagationModelsDataset.class, name = "sound propagation models")
})
public interface Dataset extends PackageInfo, PeopleOrganizationsInfo, DatasetDetail, LocationDetail, CalibrationDetail, DatasetDescription {
  LocationDetail getLocationDetail();
}
