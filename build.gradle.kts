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
    val guava = "com.google.guava:guava:31.0.1-jre";
    compileOnlyApi(guava)
    testRuntimeOnly(guava)
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