plugins {
    java
    `java-library`
    `maven-publish`
    id("com.github.hierynomus.license-base") version "0.16.1"
}

group = "io.github.pulsebeat02"
version = "v1.1.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnlyApi("com.google.guava:guava:31.1-jre")
    testRuntimeOnly("com.google.guava:guava:31.1-jre")
    compileOnlyApi("uk.co.caprica:vlcj:4.7.1")
    testRuntimeOnly("uk.co.caprica:vlcj:4.7.1")
    compileOnlyApi("uk.co.caprica:vlcj-natives:4.5.0")
    testRuntimeOnly("uk.co.caprica:vlcj-natives:4.5.0")
    compileOnlyApi("net.java.dev.jna:jna:5.10.0")
    testRuntimeOnly("net.java.dev.jna:jna:5.10.0")
    compileOnlyApi("net.java.dev.jna:jna-platform:5.11.0")
    testRuntimeOnly("net.java.dev.jna:jna-platform:5.11.0")
}

sourceSets {
    main {
        java {
            srcDir("src/main/java")
        }
        resources {
            srcDir("src/main/resources")
        }
    }
}

tasks {
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }
    publish {
        dependsOn(clean)
        dependsOn(build)
    }
}

publishing {
    repositories {
        maven {
            setUrl("https://pulsebeat02.jfrog.io/artifactory/minecraftmedialibrary/")
            credentials {
                username = ""
                password = ""
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

subprojects {

    apply(plugin = "com.github.hierynomus.license-base")

    license {
        header = rootProject.file("LICENSE")
        encoding = "UTF-8"
        mapping("java", "SLASHSTAR_STYLE")
        includes(listOf("**/*.java", "**/*.kts"))
    }

    task<Wrapper>("wrapper") {
        gradleVersion = "7.3.3"
    }
}