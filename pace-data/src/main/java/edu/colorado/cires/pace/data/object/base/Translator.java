package edu.colorado.cires.pace.data.object.base;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.colorado.cires.pace.data.object.dataset.audio.translator.AudioPackageTranslator;
import edu.colorado.cires.pace.data.object.sensor.audio.translator.AudioSensorTranslator;
import edu.colorado.cires.pace.data.object.dataset.audio.translator.CPODPackageTranslator;
import edu.colorado.cires.pace.data.object.sensor.depth.translator.DepthSensorTranslator;
import edu.colorado.cires.pace.data.object.detectionType.translator.DetectionTypeTranslator;
import edu.colorado.cires.pace.data.object.dataset.detections.translator.DetectionsPackageTranslator;
import edu.colorado.cires.pace.data.object.fileType.translator.FileTypeTranslator;
import edu.colorado.cires.pace.data.object.instrument.translator.InstrumentTranslator;
import edu.colorado.cires.pace.data.object.contact.organization.translator.OrganizationTranslator;
import edu.colorado.cires.pace.data.object.sensor.other.translator.OtherSensorTranslator;
import edu.colorado.cires.pace.data.object.contact.person.translator.PersonTranslator;
import edu.colorado.cires.pace.data.object.platform.translator.PlatformTranslator;
import edu.colorado.cires.pace.data.object.project.translator.ProjectTranslator;
import edu.colorado.cires.pace.data.object.sea.translator.SeaTranslator;
import edu.colorado.cires.pace.data.object.ship.translator.ShipTranslator;
import edu.colorado.cires.pace.data.object.dataset.soundClips.translator.SoundClipsPackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.soundLevelMetrics.translator.SoundLevelMetricsPackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.soundPropagationModels.translator.SoundPropagationModelsPackageTranslator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * Translator outlines the JSON fields for translators in pace
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ShipTranslator.class, name = "ship"),
    @JsonSubTypes.Type(value = SoundClipsPackageTranslator.class, name = "sound clips package"),
    @JsonSubTypes.Type(value = SoundLevelMetricsPackageTranslator.class, name = "sound level metrics package"),
    @JsonSubTypes.Type(value = SoundPropagationModelsPackageTranslator.class, name = "sound propagation model package"),
    @JsonSubTypes.Type(value = AudioPackageTranslator.class, name = "audio package"),
    @JsonSubTypes.Type(value = AudioSensorTranslator.class, name = "audio sensor"),
    @JsonSubTypes.Type(value = CPODPackageTranslator.class, name = "CPOD package"),
    @JsonSubTypes.Type(value = DepthSensorTranslator.class, name = "depth sensor"),
    @JsonSubTypes.Type(value = DetectionTypeTranslator.class, name = "detection type"),
    @JsonSubTypes.Type(value = DetectionsPackageTranslator.class, name = "detections package"),
    @JsonSubTypes.Type(value = FileTypeTranslator.class, name = "file type"),
    @JsonSubTypes.Type(value = InstrumentTranslator.class, name = "instrument"),
    @JsonSubTypes.Type(value = OrganizationTranslator.class, name = "organization"),
    @JsonSubTypes.Type(value = OtherSensorTranslator.class, name = "other sensor"),
    @JsonSubTypes.Type(value = PersonTranslator.class, name = "person"),
    @JsonSubTypes.Type(value = PlatformTranslator.class, name = "platform"),
    @JsonSubTypes.Type(value = ProjectTranslator.class, name = "project"),
    @JsonSubTypes.Type(value = SeaTranslator.class, name = "sea"),
})
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public abstract class Translator extends ObjectWithName {

}
