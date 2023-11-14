plugins {
    kotlin("jvm") version "1.9.10"
}

val junitJupiterVersion = "5.10.1"
val jvmTargetVersion = "17"
val gsonVersion = "2.9.0"
val kafkaVersion = "3.6.0"
val ktorVersion = "2.3.6"
val awaitilityVersion = "4.2.0"
val kafkaEEVersion = "3.2.4"
val testcontainersPostgresqlVersion = "1.19.1"
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
        implementation("ch.qos.logback:logback-classic:1.4.11")
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
        testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
        testImplementation("org.testcontainers:testcontainers:1.17.3")
        testImplementation("org.testcontainers:junit-jupiter:1.17.3")

        testImplementation("org.testcontainers:postgresql:$testcontainersPostgresqlVersion") {
            exclude("com.fasterxml.jackson.core")
        }


        api("io.micrometer:micrometer-registry-prometheus:1.9.0")

    }

    tasks {
        compileKotlin {
            kotlinOptions.jvmTarget = jvmTargetVersion
        }

        compileTestKotlin {
            kotlinOptions.jvmTarget = jvmTargetVersion
        }

        withType<Wrapper> {
            gradleVersion = "8.3"
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