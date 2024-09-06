jar -xf ./jars/pace-cli-0.1.1-SNAPSHOT.jar org/sqlite/native/Mac/aarch64/libsqlitejdbc.dylib
jar -xf ./jars/pace-cli-0.1.1-SNAPSHOT.jar org/sqlite/native/Mac/x86_64/libsqlitejdbc.dylib

/usr/bin/codesign -s $4 -vvvv --timestamp --options runtime --prefix $3 --keychain $1 --force org/sqlite/native/Mac/aarch64/libsqlitejdbc.dylib
/usr/bin/codesign -s $4 -vvvv --timestamp --options runtime --prefix $3 --keychain $1 --force org/sqlite/native/Mac/x86_64/libsqlitejdbc.dylib


jar -uf ./jars/pace-cli-0.1.1-SNAPSHOT.jar org/sqlite/native/Mac/aarch64/libsqlitejdbc.dylib
jar -uf ./jars/pace-cli-0.1.1-SNAPSHOT.jar org/sqlite/native/Mac/x86_64/libsqlitejdbc.dylib