package edu.colorado.cires.pace.packaging;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.colorado.cires.pace.data.object.contact.organization.Organization;
import edu.colorado.cires.pace.data.object.contact.person.Person;
import edu.colorado.cires.pace.data.object.dataset.audio.AudioPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Channel;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.DutyCycle;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.SampleRate;
import edu.colorado.cires.pace.data.object.dataset.base.ProcessingLevel;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.PackageSensor;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.QualityLevel;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.MarineInstrumentLocation;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.StationaryMarineLocation;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DataQualityEntry;
import edu.colorado.cires.pace.data.object.dataset.passivePacker.PassivePackerPackage;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.NotFoundException;
import edu.colorado.cires.pace.repository.OrganizationRepository;
import edu.colorado.cires.pace.repository.PersonRepository;
import edu.colorado.cires.pace.utilities.SerializationUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class PassivePackerFactoryTest {

  private final ObjectMapper objectMapper = SerializationUtils.createObjectMapper()
      .configure(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS, false) // just for testing
      .setPropertyNamingStrategy(PropertyNamingStrategies.UPPER_SNAKE_CASE);
  
  private final PersonRepository personRepository = mock(PersonRepository.class);
  private final OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
  
  private final PassivePackerFactory factory = new PassivePackerFactory(personRepository, organizationRepository);

  @Test
  void testAudio() throws IOException, NotFoundException, DatastoreException {
    when(personRepository.getByUniqueField("Chuck Anderson")).thenReturn(Person.builder()
            .uuid(UUID.fromString("ad7776a0-6c05-11e2-bcfd-0800200c9a66"))
            .name("Chuck Anderson")
            .organization("NESDIS/NCEI")
            .position("Water Column Data Manager")
            .street("NOAA/NCEI, 325 Broadway")
            .city("Boulder")
            .state("CO")
            .zip("80305")
            .country("USA")
            .email("email")
            .phone("phone")
        .build());
    
    when(organizationRepository.getByUniqueField("NOAA ONMS")).thenReturn(Organization.builder()
            .uuid(UUID.fromString("1e78f564-ac17-4ebc-9a96-5caaecf7663b"))
            .name("NOAA ONMS")
        .build());
    when(organizationRepository.getByUniqueField("BOEM")).thenReturn(Organization.builder()
            .uuid(UUID.fromString("fee4d17a-a7dd-4f13-ade5-817b4f1fc86d"))
            .name("BOEM")
        .build());
    
    assertEquals(
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            objectMapper.readValue(new File("src/test/resources/test_audio.json"), PassivePackerPackage.class)
        ),
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            factory.createPackage(createAudioPackage())
        )
    );
  }

  private AudioPackage createAudioPackage() {
    return AudioPackage.builder()
        .processingLevel(ProcessingLevel.Raw)
        .dataCollectionName("test_audio")
        .publishDate(LocalDate.of(2023, 3, 24))
        .projectName(List.of("project 1"))
        .deploymentAlias("deployment alias")
        .deploymentId("deployment name")
        .site("CI01")
        .alternateSiteName("site alias")
        .title("test title")
        .deploymentDescription("test abstract")
        .purpose("test purpose")
        .channels(List.of(
            Channel.<String>builder()
                .startTime(LocalDateTime.of(2010, 1, 1, 13, 0, 0))
                .endTime(LocalDateTime.of(2011, 1, 1, 13, 0, 0))
                .sensor(PackageSensor.<String>builder()
                    .sensor("sensor 1")
                    .build())
                .sampleRates(List.of(
                    SampleRate.builder()
                        .startTime(LocalDateTime.of(2010, 1, 1, 13, 0, 0))
                        .endTime(LocalDateTime.of(2011, 1, 1, 13, 0, 0))
                        .sampleRate(24f)
                        .sampleBits(16)
                        .build()
                )).gains(Collections.emptyList())
                .dutyCycles(List.of(
                    DutyCycle.builder()
                        .startTime(LocalDateTime.of(2010, 1, 1, 13, 0, 0))
                        .endTime(LocalDateTime.of(2011, 1, 1, 13, 0, 0))
                        .duration(60f)
                        .interval(0f)
                        .build()
                )).build(),
            Channel.<String>builder()
                .startTime(LocalDateTime.of(2010, 1, 1, 13, 0, 0))
                .endTime(LocalDateTime.of(2011, 1, 1, 13, 0, 0))
                .sensor(PackageSensor.<String>builder()
                    .sensor("sensor 2")
                    .build())
                .sampleRates(List.of(
                    SampleRate.builder()
                        .startTime(LocalDateTime.of(2010, 1, 1, 13, 0, 0))
                        .endTime(LocalDateTime.of(2011, 1, 1, 13, 0, 0))
                        .sampleRate(24f)
                        .sampleBits(16)
                        .build()
                )).gains(Collections.emptyList())
                .dutyCycles(List.of(
                    DutyCycle.builder()
                        .startTime(LocalDateTime.of(2010, 1, 1, 13, 0, 0))
                        .endTime(LocalDateTime.of(2011, 1, 1, 13, 0, 0))
                        .duration(60f)
                        .interval(0f)
                        .build()
                ))
                .build(),
            Channel.<String>builder()
                .startTime(LocalDateTime.of(2010, 1, 1, 13, 0, 0))
                .endTime(LocalDateTime.of(2011, 1, 1, 13, 0, 0))
                .sensor(PackageSensor.<String>builder()
                    .sensor("sensor 3")
                    .build())
                .sampleRates(List.of(
                    SampleRate.builder()
                        .startTime(LocalDateTime.of(2010, 1, 1, 13, 0, 0))
                        .endTime(LocalDateTime.of(2011, 1, 1, 13, 0, 0))
                        .sampleRate(24f)
                        .sampleBits(16)
                        .build()
                )).gains(Collections.emptyList())
                .dutyCycles(List.of(
                    DutyCycle.builder()
                        .startTime(LocalDateTime.of(2010, 1, 1, 13, 0, 0))
                        .endTime(LocalDateTime.of(2011, 1, 1, 13, 0, 0))
                        .duration(60f)
                        .interval(0f)
                        .build()
                ))
                .build()
        )).qualityAnalyst("Chuck Anderson")
        .qualityAnalysisMethod("quality method")
        .qualityAnalysisObjectives("quality objectives")
        .qualityAssessmentDescription("quality abstract")
        .qualityEntries(List.of(
            DataQualityEntry.builder()
                .qualityLevel(QualityLevel.good)
                .minFrequency(0f)
                .maxFrequency(12000f)
                .startTime(LocalDateTime.of(2010, 1, 1, 13, 0, 0))
                .endTime(LocalDateTime.of(2011, 1, 1, 13, 0, 0))
                .comments("quality comments")
                .build()
        )).platformName("Glider")
        .instrumentType("DMON")
        .instrumentId("instrument id")
        .deploymentTime(LocalDateTime.of(2010, 1, 1, 13, 0, 0))
        .recoveryTime(LocalDateTime.of(2011, 1, 1, 13, 0, 0))
        .audioStartTime(LocalDateTime.of(2010, 1, 1, 13, 0, 0))
        .audioEndTime(LocalDateTime.of(2011, 1, 1, 13, 0, 0))
        .locationDetail(StationaryMarineLocation.builder()
            .seaArea("Bristol Channel")
            .deploymentLocation(
                MarineInstrumentLocation.builder()
                    .latitude(12d)
                    .longitude(12d)
                    .instrumentDepth(-11f)
                    .seaFloorDepth(-12f)
                    .build()
            ).recoveryLocation(
                MarineInstrumentLocation.builder()
                    .latitude(12d)
                    .longitude(12d)
                    .instrumentDepth(-11f)
                    .seaFloorDepth(-12f)
                    .build()
            ).build())
        .sourcePath(Paths.get("/Volumes/Fortress/test_data/audio_files"))
        .comments("data comment")
        .calibrationDocumentsPath(Paths.get("/Volumes/Fortress/test_data/calibration"))
        .hydrophoneSensitivity(23f)
        .frequencyRange("2-20000")
        .gain(-3f)
        .calibrationDescription("THIS IS DESCRIPTIVE TEXT ABOUT THE CALIBRATION")
        .preDeploymentCalibrationDate(LocalDate.of(2010, 1, 1))
        .scientists(List.of("Chuck Anderson"))
        .sponsors(List.of("NOAA ONMS"))
        .funders(List.of("BOEM"))
        .datasetPackager("Chuck Anderson")
        .build();
  }
}