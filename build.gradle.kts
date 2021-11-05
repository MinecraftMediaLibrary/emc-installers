plugins {
    java
    `java-library`
}

group = "io.github.pulsebeat02"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {

    compileOnlyApi("com.google.guava:guava:31.0.1-jre")
    testRuntimeOnly("com.google.guava:guava:31.0.1-jre")

    compileOnlyApi("uk.co.caprica:vlcj:4.7.1")
    testRuntimeOnly("uk.co.caprica:vlcj:4.7.1")

    compileOnlyApi("uk.co.caprica:vlcj-natives:4.1.0")
    testRuntimeOnly("uk.co.caprica:vlcj-natives:4.1.0")

    compileOnlyApi("net.java.dev.jna:jna:5.9.0")
    testRuntimeOnly("net.java.dev.jna:jna:5.9.0")

    compileOnlyApi("net.java.dev.jna:jna-platform:5.9.0")
    testRuntimeOnly("net.java.dev.jna:jna-platform:5.9.0")

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}