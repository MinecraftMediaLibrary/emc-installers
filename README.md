# Static FFmpeg Installer

Static FFmpeg Installer is a lightweight library which downloads
FFmpeg static compiled executables to your respective operating
system. It detects what type of operating system you are using
and installs the proper binary from [ffmpeg-static](https://github.com/eugeneware/ffmpeg-static),
a repository holding reliable and stable ffmpeg builds.

## Setup
1) Add Jitpack to your repositories:

Maven:
```xml
<repository>
  <id>jitpack.io</id>
  <url>https://jitpack.io</url>
</repository>
```

Gradle Groovy:
```groovy
maven { url 'https://jitpack.io' }
```

Gradle Kotlin DSL:
```kotlin
maven("https://jitpack.io")
```

2) Add the repository into your project:

Maven
```xml
<dependency>
  <groupId>com.github.MinecraftMediaLibrary</groupId>
  <artifactId>static-ffmpeg-installer</artifactId>
  <version>master-SNAPSHOT</version>
</dependency>
```

Gradle Groovy:
```groovy
implementation 'com.github.MinecraftMediaLibrary:static-ffmpeg-installer:master-SNAPSHOT'
```

Gradle Kotlin DSL:
```kotlin
implementation("com.github.MinecraftMediaLibrary:static-ffmpeg-installer:master-SNAPSHOT")
```

## Usage
Using the library is pretty easy. Define an `FFmpegInstaller`
using the factory methods:

```java
// Creates an installer with default installation directory
final FFmpegInstaller installer = FFmpegInstaller.create();

// Creates an installer with the installation directory "path/to/dir"
final FFmpegInstaller installer = FFmpegInstaller.create(Paths.get("path/to/dir"));

// Downloads the FFmpeg executable
final Path executable = installer.download();
```

That's it!


