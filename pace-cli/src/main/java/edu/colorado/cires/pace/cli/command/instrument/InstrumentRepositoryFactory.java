package edu.colorado.cires.pace.cli.command.instrument;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.cli.command.dataset.PackageRepositoryFactory;
import edu.colorado.cires.pace.data.object.fileType.FileType;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.datastore.Datastore;
import edu.colorado.cires.pace.datastore.sqlite.InstrumentSQLiteDatastore;
import edu.colorado.cires.pace.repository.InstrumentRepository;
import edu.colorado.cires.pace.datastore.json.FileTypeJsonDatastore;
import edu.colorado.cires.pace.datastore.json.InstrumentJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

public final class InstrumentRepositoryFactory {
  
  public static InstrumentJsonDatastore createJsonDatastore(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new InstrumentJsonDatastore(datastoreDirectory, objectMapper);
  }
  
  public static InstrumentSQLiteDatastore createSQLiteDatastore(Path sqliteFile, Datastore<FileType> fileTypeDatastore) {
    return new InstrumentSQLiteDatastore(sqliteFile, fileTypeDatastore);
  }

  public static InstrumentRepository createJsonRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new InstrumentRepository(
        createJsonDatastore(datastoreDirectory, objectMapper),
        new FileTypeJsonDatastore(datastoreDirectory, objectMapper),
        PackageRepositoryFactory.createJsonDatastore(datastoreDirectory, objectMapper)
    );
  }
  
  public static InstrumentRepository createSQLiteRepository(Path sqliteFile, Path datastoreDirectory, ObjectMapper objectMapper, Datastore<Package> packageDatastore) throws IOException {
    FileTypeJsonDatastore fileTypeJsonDatastore = new FileTypeJsonDatastore(datastoreDirectory, objectMapper);
    return new InstrumentRepository(
        createSQLiteDatastore(sqliteFile, fileTypeJsonDatastore),
        fileTypeJsonDatastore,
        packageDatastore
    );
  }

}
