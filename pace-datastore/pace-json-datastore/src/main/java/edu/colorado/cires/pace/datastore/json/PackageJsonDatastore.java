package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.Package;
import java.io.IOException;
import java.nio.file.Path;

public class PackageJsonDatastore extends JsonDatastore<Package> {

  public PackageJsonDatastore(Path storagePath, ObjectMapper objectMapper) throws IOException {
    super(storagePath.resolve("packages.json"), objectMapper, Package.class, Package::getPackageId, new TypeReference<>() {});
  }

  @Override
  public String getUniqueFieldName() {
    return "packageId";
  }
}
