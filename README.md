# PACE - Passive Acoustic Collection Engine
The Passive Acoustic Collection Engine (PACE) tool provides a programmatic approach to packaging passive acoustic data for submission to NCEI. PACE is designed for users with a  need to submit large numbers of datasets to NCEI and/or users who manage metadata in a system such as Tethys or a relational database. PACE can be controlled via a GUI or from the command line (CLI) and supports user-created metadata spreadsheets or direct integration with database systems. PACE creates data packages like those created by PassivePacker without the need for PassivePacker’s manual metadata entry.

  - [Releases](#releases)
  - [Supported Platforms](#supported-platforms)
  - [GUI](#graphical-user-interface)
    - [GUI Guides](#gui-guides)
    - [Installing the Application](#installing-the-application)
  - [CLI](#command-line-interface)
    - [Installation](#installation)
      - [MacOS](#macos)
      - [Linux](#linux)
      - [Windows](#windows)
    - [Example usage](#example-usage)
  - [Developer Notes](#developer-notes)
    - [System Requirements](#system-requirements)
    - [Installing Dependencies](#installing-dependencies)
    - [Additional Maven profiles](#additional-maven-profiles)
  - [PACE Templates](#pace-templates)

## Releases
- Latest Release: [1.0.3](https://github.com/CI-CMG/pace/releases/tag/v1.0.3)
- Development Release: [trunk-latest](https://github.com/CI-CMG/pace/releases/tag/trunk-latest)

## Supported Platforms
| Platform | CLI | GUI |
|:-----------------:|:-----------------:|:-----------------:|
| Windows (x86)  | ✅  | ✅  |
| Windows (ARM)  | ❌  | ❌  |
| Linux (x86)  | ✅  | ✅  |
| Linux (ARM)  | ❌  | ❌  |
| MacOS (x86)  | ✅  | ✅  |
| MacOS (ARM)  | ✅  | ✅  |

## Graphical User Interface
### GUI Guides
For a video walkthrough of the PACE GUI, go to the following [link](https://www.youtube.com/watch?v=v8NlUkiZFG8).
For an in-depth PACE GUI guide, go to the following [link](https://github.com/CI-CMG/pace/blob/trunk/docs/PACE%20GUI%20Guide.pdf).

### Installing the Application
1. Go to this [link](https://github.com/CI-CMG/pace/releases)
2. Look under the most recent release version which is not trunk-latest (currently v1.0.3)
3. Find the files which start with "pace-gui"
4. Click the relevant version to download:\
   a. Older Mac (Intel CPU): "pace-gui-macOS-X64"\
   b. Newer Mac (M1, M2, or M3 CPU): "pace-gui-macOS-ARM64"\
   c. Windows: "pace-gui-Windows-X64-(version number).msi"\
   d. Debian-Based Linux: "pace-gui-Linux-X64-(version number).deb"\
   e. RedHat-Based Linux: "pace-gui-Linux-X64-(version number).rpm"

## Command Line Interface
### Installation
#### MacOS
1. Download file using ``curl``: ``curl -L -O https://github.com/CI-CMG/pace/releases/download/trunk-latest/pace-cli-macOS-ARM64-1.0.3-SNAPSHOT.pkg``
2. Use ``installer`` to install ``pace-cli`` (requires sudo): ``installer -pkg pace-cli-macOS-ARM64-1.0.3-SNAPSHOT.pkg -target /``
3. Add the following line to ``.zshrc`` or ``.bash_profile`` depending on your shell preference: ``export PATH="/Applications/pace-cli.app/Contents/MacOS:$PATH"``
4. Open new terminal window
5. Verify installation: ``pace-cli --version``
#### Linux
1. Download file using ``curl``: ``curl -L -O https://github.com/CI-CMG/pace/releases/download/trunk-latest/pace-cli-Linux-X64-1.0.3-SNAPSHOT.zip``
2. Unzip file: ``unzip pace-cli-Linux-X64-1.0.3-SNAPSHOT.zip``
3. Add the following line to ``.bashrc``: ``export PATH="$HOME/pace-cli-Linux-X64/bin:$PATH"``
4. Set ``pace-cli`` as executable: ``chmod +x $HOME/pace-cli-Linux-X64/bin/pace-cli``
5. Open new terminal window
6. Verify installation: ``pace-cli --version``
#### Windows
1. Download file using `curl`: `curl -L -O https://github.com/CI-CMG/pace/releases/download/trunk-latest/pace-cli-Windows-X64-1.0.3-SNAPSHOT.msi`
2. Execute downloaded file and follow installer prompts: `msiexec \i  pace-cli-Windows-X64-1.0.3-SNAPSHOT.msi`
3. Add PATH listing in Windows environment variable menu: ``%USERPROFILE%\AppData\Local\pace-cli``
4. Restart command prompt
5. Verify installation: `pace-cli --version`
### Example usage
Please refer to our [jupyter notebook](docs/CLI_Example_Usage.ipynb) for examples of how to use PACE's CLI.
If you cannot open the jupyter notebook, you can view the non-interactive version [here](docs/CLI_Example_Usage.html).

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
 
## PACE Templates
For a further understanding of PACE's internal JSON schema or for a look at possible alternate PACE work cycles, go to the following [link](https://github.com/CI-CMG/pace-templates) which provides PACE templates.
