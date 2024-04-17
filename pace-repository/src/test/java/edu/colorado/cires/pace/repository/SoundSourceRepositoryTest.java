package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.SoundSource;
import edu.colorado.cires.pace.data.validation.ValidationException;
import java.util.function.Function;

public class SoundSourceRepositoryTest extends CrudRepositoryTest<SoundSource> {

  @Override
  protected CRUDRepository<SoundSource> createRepository() {
    return new SoundSourceRepository(createDatastore());
  }

  @Override
  protected Function<SoundSource, String> uniqueFieldGetter() {
    return SoundSource::getName;
  }

  @Override
  protected SoundSource createNewObject(int suffix) throws ValidationException {
    return SoundSource.builder()
        .name(String.format("name-%s", suffix))
        .scientificName(String.format("scientific-name-%s", suffix))
        .build();
  }

  @Override
  protected SoundSource copyWithUpdatedUniqueField(SoundSource object, String uniqueField) throws ValidationException {
    return object.toBuilder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected void assertObjectsEqual(SoundSource expected, SoundSource actual, boolean checkUUID) {
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    }
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getScientificName(), actual.getScientificName());
  }
}
