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
    setOf(
        "com.google.guava:guava:31.1-jre",
        "uk.co.caprica:vlcj:4.8.2",
        "uk.co.caprica:vlcj-natives:4.8.1",
        "net.java.dev.jna:jna:5.12.1",
        "net.java.dev.jna:jna-platform:5.13.0",
        "com.google.code.gson:gson:2.10"
    ).forEach {
        compileOnlyApi(it)
        testRuntimeOnly(it)
    }
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
    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
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
        gradleVersion = "7.5.1"
    }
}