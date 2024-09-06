jar -xf ./jars/pace-cli-0.1.1-SNAPSHOT.jar org/sqlite/native/Mac/aarch64/libsqlitejdbc.dylib
jar -xf ./jars/pace-cli-0.1.1-SNAPSHOT.jar org/sqlite/native/Mac/x86_64/libsqlitejdbc.dylib
ls

for filename in /BOOT-INF/lib/*.jar; do
  #echo "$filename"
  if [ -f "$filename" ]; then
    fileList=$(jar -tf "$filename" | grep .dylib)
    echo "$fileList"
  fi
done
