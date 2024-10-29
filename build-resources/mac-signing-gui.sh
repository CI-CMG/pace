set -ex

ls jars

echo Extracting gui dylib files
jar -xf $2 com/formdev/flatlaf/natives/libflatlaf-macos-arm64.dylib
jar -xf $2 com/formdev/flatlaf/natives/libflatlaf-macos-x86_64.dylib

echo Signing gui dylib files
/usr/bin/codesign -s 'Developer ID Application: University of Colorado Boulder (8JR6566HZ6)' -vvvv --timestamp --options runtime --prefix 'edu.colorado.cires.pace.gui.' --keychain "$1" --force com/formdev/flatlaf/natives/libflatlaf-macos-arm64.dylib
/usr/bin/codesign -s 'Developer ID Application: University of Colorado Boulder (8JR6566HZ6)' -vvvv --timestamp --options runtime --prefix 'edu.colorado.cires.pace.gui.' --keychain "$1" --force com/formdev/flatlaf/natives/libflatlaf-macos-x86_64.dylib

echo Repacking gui dylib files
jar -uf $2 com/formdev/flatlaf/natives/libflatlaf-macos-arm64.dylib
jar -uf $2 com/formdev/flatlaf/natives/libflatlaf-macos-x86_64.dylib