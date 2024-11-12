plugins {
    kotlin("jvm") version "2.0.21"
}

val junitJupiterVersion = "5.10.2"
val jvmTargetVersion = 21
val gsonVersion = "2.9.0"
val kafkaVersion = "3.6.0"
val ktorVersion = "2.3.12"
val awaitilityVersion = "4.2.0"
val kafkaEEVersion = "3.2.4"
val testcontainersVersion = "1.19.5"
val flywayVersion = "8.5.7"

allprojects {
    group = "no.nav.helse"
    version = properties["version"] ?: "local-build"

    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenCentral()
        maven("https://packages.confluent.io/maven/")
        maven("https://jitpack.io")
    }

    dependencies {
        implementation("org.apache.kafka:kafka-clients:$kafkaVersion")
        implementation("org.slf4j:slf4j-api:2.0.9")
        implementation("ch.qos.logback:logback-classic:1.4.14")
        implementation("net.logstash.logback:logstash-logback-encoder:7.4")

        implementation("io.ktor:ktor-server-core:$ktorVersion")
        implementation("io.ktor:ktor-server-cio:$ktorVersion")
        implementation("io.ktor:ktor-client-cio:$ktorVersion")

        implementation("io.ktor:ktor-server-metrics-micrometer:$ktorVersion")

        implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
        implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
        implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")

        implementation("org.flywaydb:flyway-core:$flywayVersion")


        testImplementation("org.awaitility:awaitility:$awaitilityVersion")
        testImplementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
        testImplementation("org.junit.jupiter:junit-jupiter-params:$junitJupiterVersion")
        testImplementation("no.nav:kafka-embedded-env:$kafkaEEVersion")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
        testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
        testImplementation("org.testcontainers:testcontainers:$testcontainersVersion")
        testImplementation("org.testcontainers:junit-jupiter:$testcontainersVersion")

        testImplementation("org.testcontainers:postgresql:$testcontainersVersion") {
            exclude("com.fasterxml.jackson.core")
        }


        api("io.micrometer:micrometer-registry-prometheus:1.12.3")

    }

    tasks {
        kotlin {
            jvmToolchain(jvmTargetVersion)
        }
    }

}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    tasks {
        withType<Test> {
            useJUnitPlatform()
            testLogging {
                events("skipped", "failed")
            }
        }
    }

    dependencies {
        implementation("com.google.code.gson:gson:$gsonVersion")
        testImplementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
        testImplementation("org.junit.jupiter:junit-jupiter-params:$junitJupiterVersion")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
    }
}