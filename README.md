# PACE - Passive Acoustic Collection Engine

- [PACE - Passive Acoustic Collection Engine](#pace---passive-acoustic-collection-engine)
  - [Releases](#releases)
  - [Supported Platforms](#supported-platforms)
  - [CLI](#cli)
    - [Installation](#installation)
      - [MacOS](#macos)
      - [Linux](#linux)
      - [Windows](#windows)
    - [Example usage](#example-usage)
  - [Developer Notes](#developer-notes)
    - [System Requirements](#system-requirements)
    - [Installing Dependencies](#installing-dependencies)
    - [Additional Maven profiles](#additional-maven-profiles)

## Releases
- Latest Release: [0.1.0](https://github.com/CI-CMG/pace/releases/tag/v0.1.0)
- Development Release: [trunk-latest](https://github.com/CI-CMG/pace/releases/tag/trunk-latest)

## Supported Platforms
| Platform | CLI | GUI |
|:-----------------:|:-----------------:|:-----------------:|
| Windows (x86)  | ✅  | ✅  |
| Windows (ARM)  | ❌  | ❌  |
| Linux (x86)  | ✅  | ✅  |
| Linux (ARM)  | ❌  | ❌  |
| MacOS (ARM)  | ✅  | ✅  |
| MacOS (ARM)  | ✅  | ✅  |

## CLI
### Installation
#### MacOS
1. Download file using ``curl``: ``curl -L -O https://github.com/CI-CMG/pace/releases/download/trunk-latest/pace-cli-macOS-ARM64-0.1.1-SNAPSHOT.pkg``
2. Use ``installer`` to install ``pace-cli`` (requires sudo): ``installer -pkg pace-cli-macOS-ARM64-0.1.1-SNAPSHOT.pkg -target /``
3. Add the following line to ``.zshrc`` or ``.bash_profile`` depending on your shell preference: ``export PATH="/Applications/pace-cli.app/Contents/MacOS:$PATH"``
4. Open new terminal window
5. Verify installation: ``pace-cli --version``
#### Linux
1. Download file using ``curl``: ``curl -L -O https://github.com/CI-CMG/pace/releases/download/trunk-latest/pace-cli-Linux-X64-0.1.1-SNAPSHOT.zip``
2. Unzip file: ``unzip pace-cli-Linux-X64-0.1.1-SNAPSHOT.zip``
3. Add the following line to ``.bashrc``: ``export PATH="$HOME/pace-cli-Linux-X64/bin:$PATH"``
4. Set ``pace-cli`` as executable: ``chmod +x $HOME/pace-cli-Linux-X64/bin/pace-cli``
5. Open new terminal window
6. Verify installation: ``pace-cli --version``
#### Windows
1. Download file using `curl`: `curl -L -O https://github.com/CI-CMG/pace/releases/download/trunk-latest/pace-cli-Windows-X64-0.1.1-SNAPSHOT.msi`
2. Execute downloaded file and follow installer prompts: `msiexec \i  pace-cli-Windows-X64-0.1.1-SNAPSHOT.msi`
3. Add PATH listing in Windows environment variable menu: ``%USERPROFILE%\AppData\Local\pace-cli``
4. Restart command prompt
5. Verify installation: `pace-cli --version`
### Example usage
Please refer to our [jupyter notebook](docs/CLI%20Example%20Usage.ipynb) for examples of how to use PACE's CLI.
If you cannot open the jupyter notebook, you can view the non-interactive version [here](docs/CLI%20Example%20Usage.html).

## Developer Notes
### System Requirements
- Java 17
- Maven 3.9.6
### Installing Dependencies
`mvn clean install` (will also run tests)
### Additional Maven profiles
- dep-check: Scans dependencies for security vulnerabilities using [dependency-check-maven](https://mvnrepository.com/artifact/org.owasp/dependency-check-maven)
  - `mvn clean install -Pdep-check`
- coverage: Asserts project-wide line and branch coverage is at least 90% using [jacoco-maven-plugin](https://mvnrepository.com/artifact/org.jacoco/jacoco-maven-plugin)
  - `mvn clean install -Pcoverage`

**Note:** Maven profiles can be combined within one command (`mvn clean install -Pdep-check -Pcoverage`)