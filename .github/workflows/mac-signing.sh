jar -xf pace-0.1.1-SNAPSHOT-exe.jar BOOT-INF/lib/*

for filename in /BOOT-INF/lib/*; do
  fileList=$(jar -tf filename | grep .dylib)
  echo fileList
done

