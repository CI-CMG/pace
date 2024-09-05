jar -xf ./jars/pace-cli-0.1.1-SNAPSHOT.jar BOOT-INF/lib
ls

for filename in /BOOT-INF/lib/*; do
  echo "$filename"
  #if [ -f "$filename" ]; then
  fileList=$(jar -tf "$filename" | grep .dylib)
  echo "$fileList"
  #fi
done

