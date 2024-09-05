ls
jar -xf ../pace-cli/target/pace-cli-0.1.1-SNAPSHOT.jar BOOT-INF/lib/*

for filename in /BOOT-INF/lib/*; do
  if [ -f "$filename" ]; then
    fileList=$(jar -tf filename | grep .dylib)
    echo fileList
  fi
done

