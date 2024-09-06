jar -xf ./jars/pace-cli-0.1.1-SNAPSHOT.jar org/sqlite/native/Mac/aarch64/libsqlitejdbc.dylib
jar -xf ./jars/pace-cli-0.1.1-SNAPSHOT.jar org/sqlite/native/Mac/x86_64/libsqlitejdbc.dylib
ls org

/usr/bin/codesign -s <signing-identity> -vvvv --timestamp --options runtime --prefix <identifier-prefix> --keychain <keychain> --force <file-path>

jar -uf ./jars/pace-cli-0.1.1-SNAPSHOT.jar <path-to-dylib>
jar -uf ./jars/pace-cli-0.1.1-SNAPSHOT.jar <path-to-dylib>