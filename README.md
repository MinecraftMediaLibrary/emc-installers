# Static EMC Installers

Static EMC Installers is a lightweight library which downloads
an assortment of static compiled executables to your respective 
operating system. It detects what type of operating system you are using
and installs the proper binary.

## Setup
1) Add MinecraftMediaLibrary Repository:

```kotlin
repositories {
    maven("https://pulsebeat02.jfrog.io/artifactory/minecraftmedialibrary/")
}
```

2) Add the repository into your project:

Maven
```kotlin
dependencies {
    implementation("io.github.pulsebeat02:emc-installers:v1.1.0")
}
```

## Usage
Using the library is pretty easy. Define an `FFmpegInstaller`
using the factory methods: (as an example)

```java
// Creates an installer with default installation directory
final FFmpegInstaller installer = FFmpegInstaller.create();

// Creates an installer with the installation directory "path/to/dir"
final FFmpegInstaller installer = FFmpegInstaller.create(Paths.get("path/to/dir"));

// Downloads the FFmpeg executable
final Path executable = installer.download();
```

That's it!


