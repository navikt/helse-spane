plugins {
    kotlin("jvm") version "1.7.0"
}

val junitJupiterVersion = "5.8.2"
val jvmTargetVersion = "17"
val gsonVersion = "2.9.0"
val kafkaVersion = "3.1.0"
val ktorVersion = "2.0.2"
val awaitilityVersion = "4.2.0"
val kafkaEEVersion = "3.1.6"

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
        implementation("org.slf4j:slf4j-api:1.7.36")
        implementation("ch.qos.logback:logback-classic:1.2.11")
        implementation("net.logstash.logback:logstash-logback-encoder:7.2")

        implementation("io.ktor:ktor-server-core:$ktorVersion")
        implementation("io.ktor:ktor-server-cio:$ktorVersion")
        implementation("io.ktor:ktor-client-cio:$ktorVersion")

        implementation("io.ktor:ktor-server-metrics-micrometer:$ktorVersion")

        implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
        implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
        implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")
        testImplementation("org.awaitility:awaitility:$awaitilityVersion")
        testImplementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
        testImplementation("org.junit.jupiter:junit-jupiter-params:$junitJupiterVersion")
        testImplementation("no.nav:kafka-embedded-env:$kafkaEEVersion")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")

    }

    tasks {
        compileKotlin {
            kotlinOptions.jvmTarget = jvmTargetVersion
        }

        compileTestKotlin {
            kotlinOptions.jvmTarget = jvmTargetVersion
        }

        withType<Wrapper> {
            gradleVersion = "7.4.2"
        }

    }
    tasks {

        jar {
            archiveFileName.set("app.jar")

            manifest {
                attributes["Main-Class"] = "no.nav.helse.AppKt"
                attributes["Class-Path"] = configurations.runtimeClasspath.get().joinToString(separator = " ") {
                    it.name
                }
            }
            doLast {
                configurations.runtimeClasspath.get()
                    .filter { it.name != "app.jar" }
                    .forEach {
                        val file = File("$buildDir/libs/${it.name}")
                        if (!file.exists())
                            it.copyTo(file)
                    }
            }
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