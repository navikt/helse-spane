plugins {
    kotlin("jvm") version "2.0.21"
}

val junitJupiterVersion = "5.11.3"
val gsonVersion = "2.9.0"
val kafkaVersion = "3.6.0"
val ktorVersion = "3.0.1"
val awaitilityVersion = "4.2.0"
val kafkaEEVersion = "3.2.4"
val testcontainersVersion = "1.19.5"
val flywayVersion = "8.5.7"

allprojects {
    group = "no.nav.helse"
    version = properties["version"] ?: "local-build"

    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        val githubPassword: String? by project
        mavenCentral()
        /* ihht. https://github.com/navikt/utvikling/blob/main/docs/teknisk/Konsumere%20biblioteker%20fra%20Github%20Package%20Registry.md
            så plasseres github-maven-repo (med autentisering) før nav-mirror slik at github actions kan anvende førstnevnte.
            Det er fordi nav-mirroret kjører i Google Cloud og da ville man ellers fått unødvendige utgifter til datatrafikk mellom Google Cloud og GitHub
         */
        maven {
            url = uri("https://maven.pkg.github.com/navikt/maven-release")
            credentials {
                username = "x-access-token"
                password = githubPassword
            }
        }
        maven("https://github-package-registry-mirror.gc.nav.no/cached/maven-release")
        maven("https://packages.confluent.io/maven/")
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
        testImplementation("no.nav:kafka-embedded-env:$kafkaEEVersion")
        testImplementation("org.testcontainers:testcontainers:$testcontainersVersion")
        testImplementation("org.testcontainers:junit-jupiter:$testcontainersVersion")

        testImplementation("org.testcontainers:postgresql:$testcontainersVersion") {
            exclude("com.fasterxml.jackson.core")
        }


        api("io.micrometer:micrometer-registry-prometheus:1.12.3")

        testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }

    kotlin {
        jvmToolchain {
            languageVersion.set(JavaLanguageVersion.of("21"))
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
        testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }
}