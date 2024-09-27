package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import java.io.IOException;
import java.nio.file.Path;

/**
 * PackageJsonDatastore extends JsonDatastore and returns packageId
 * as the unique field name
 */
public class PackageJsonDatastore extends JsonDatastore<Package> {

  /**
   * Initializes a package JSON datastore
   *
   * @param storagePath location of datastore
   * @param objectMapper relevant mapper
   * @throws IOException in case of input or output error
   */
  public PackageJsonDatastore(Path storagePath, ObjectMapper objectMapper) throws IOException {
    super(storagePath.resolve("packages"), objectMapper, Package.class, Package::getPackageId);
  }

  /**
   * Returns the unique field name
   *
   * @return String packageId
   */
  @Override
  public String getUniqueFieldName() {
    return "packageId";
  }
}
