set -ex

echo Extracting dylib files
jar -xf ./jars/pace-cli-0.1.1-SNAPSHOT.jar org/sqlite/native/Mac/aarch64/libsqlitejdbc.dylib
jar -xf ./jars/pace-cli-0.1.1-SNAPSHOT.jar org/sqlite/native/Mac/x86_64/libsqlitejdbc.dylib

echo Signing dylib files
security list-keychains -d user -s "$(security list-keychains -d user | sed -e s/\"//g)" "edu.colorado.cires.pace.cli.$1"
/usr/bin/codesign -s 'Developer ID Application: University of Colorado Boulder (8JR6566HZ6)' -vvvv --timestamp --options runtime --prefix 'edu.colorado.cires.pace.cli.' --keychain "$1" --force org/sqlite/native/Mac/aarch64/libsqlitejdbc.dylib
/usr/bin/codesign -s 'Developer ID Application: University of Colorado Boulder (8JR6566HZ6)' -vvvv --timestamp --options runtime --prefix 'edu.colorado.cires.pace.cli.' --keychain "$1" --force org/sqlite/native/Mac/x86_64/libsqlitejdbc.dylib

echo Repacking dylib files
jar -uf ./jars/pace-cli-0.1.1-SNAPSHOT.jar org/sqlite/native/Mac/aarch64/libsqlitejdbc.dylib
jar -uf ./jars/pace-cli-0.1.1-SNAPSHOT.jar org/sqlite/native/Mac/x86_64/libsqlitejdbc.dylib