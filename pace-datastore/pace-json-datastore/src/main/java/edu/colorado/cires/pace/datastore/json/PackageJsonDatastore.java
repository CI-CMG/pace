package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.Package;
import java.io.IOException;
import java.nio.file.Path;

public class PackageJsonDatastore extends JsonDatastore<Package> {

  public PackageJsonDatastore(Path storagePath, ObjectMapper objectMapper) throws IOException {
    super(storagePath.resolve("packages"), objectMapper, Package.class, Package::getPackageId);
  }

  @Override
  public String getUniqueFieldName() {
    return "packageId";
  }
}
