#!/usr/bin/env bash

set -ex

# Delete temporary keychain
security delete-keychain "$MY_KEYCHAIN"

#remove files
rm app_sign.p12.txt
rm app_sign.p12
rm install_sign.p12.txt
rm install_sign.p12
