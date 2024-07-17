package edu.colorado.cires.pace.datastore.sqlite;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.colorado.cires.pace.data.object.FileType;
import edu.colorado.cires.pace.data.object.Instrument;
import edu.colorado.cires.pace.datastore.Datastore;
import edu.colorado.cires.pace.datastore.DatastoreException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InstrumentSQLiteDatastoreTest extends SQLiteDatastoreTest<Instrument> {
  
  private static final Datastore<FileType> fileTypeDatastore = mock(Datastore.class);
  
  private static final FileType wavFileType = FileType.builder()
      .uuid(UUID.randomUUID())
      .type("wav")
      .build();
  private static final FileType aifFileType = FileType.builder()
      .uuid(UUID.randomUUID())
      .type("aif")
      .build();
  private static final FileType aiffFileType = FileType.builder()
      .uuid(UUID.randomUUID())
      .type("aiff")
      .build();
  private static final FileType csvFileType = FileType.builder()
      .uuid(UUID.randomUUID())
      .type(".csv")
      .build();
  static {
    try {
      when(fileTypeDatastore.findByUniqueField("wav")).thenReturn(Optional.of(wavFileType));
      when(fileTypeDatastore.findByUniqueField("aif")).thenReturn(Optional.of(aifFileType));
      when(fileTypeDatastore.findByUniqueField("aiff")).thenReturn(Optional.of(aiffFileType));
      when(fileTypeDatastore.findByUniqueField(".csv")).thenReturn(Optional.of(csvFileType));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected SQLiteDatastore<Instrument> createDatastore(Path dbPath) {
    return new InstrumentSQLiteDatastore(dbPath, fileTypeDatastore);
  }

  @Override
  protected int getExpectedRowCount() {
    return 9;
  }

  @Override
  protected void verifyObject(Instrument object) {
    assertNull(object.getUuid());
    assertNotNull(object.getName());
    
    assertNotNull(object.getFileTypes());
    object.getFileTypes().forEach(Assertions::assertNotNull);
  }
  
  @Test
  void testFileTypeNotFound() {
    Datastore<Instrument> instrumentDatastore = new InstrumentSQLiteDatastore(getDBPath(), mock(FileTypeSQLiteDatastore.class));
    RuntimeException runtimeException = assertThrows(RuntimeException.class, instrumentDatastore::findAll);
    assertEquals("file type wav not found", runtimeException.getCause().getMessage());
    assertInstanceOf(DatastoreException.class, runtimeException.getCause());
  }
}
