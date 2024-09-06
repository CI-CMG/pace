set -ex

security find-certificate -a sign-keychain

echo Extracting dylib files
jar -xf ./jars/pace-cli-0.1.1-SNAPSHOT.jar org/sqlite/native/Mac/aarch64/libsqlitejdbc.dylib
jar -xf ./jars/pace-cli-0.1.1-SNAPSHOT.jar org/sqlite/native/Mac/x86_64/libsqlitejdbc.dylib

echo Signing dylib files
/usr/bin/codesign -s "Developer ID Application: $2" -vvvv --timestamp --options runtime --prefix 'edu.colorado.cires.pace.cli.' --keychain $1 --force org/sqlite/native/Mac/aarch64/libsqlitejdbc.dylib
/usr/bin/codesign -s "Developer ID Application: $2" -vvvv --timestamp --options runtime --prefix 'edu.colorado.cires.pace.cli.' --keychain $1 --force org/sqlite/native/Mac/x86_64/libsqlitejdbc.dylib

echo Repacking dylib files
jar -uf ./jars/pace-cli-0.1.1-SNAPSHOT.jar org/sqlite/native/Mac/aarch64/libsqlitejdbc.dylib
jar -uf ./jars/pace-cli-0.1.1-SNAPSHOT.jar org/sqlite/native/Mac/x86_64/libsqlitejdbc.dylib