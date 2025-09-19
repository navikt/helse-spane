val logbackClassicVersion = "1.5.12"
val logbackEncoderVersion = "8.0"
val jacksonVersion = "2.18.3"
val kafkaVersion = "3.9.0"
val ktorVersion = "3.2.3"
val flywayVersion = "11.5.0"
val tbdLibsVersion = "2025.09.19-13.31-61342e73"
val awaitilityVersion = "4.2.0"
val hikariCPVersion = "6.3.0"
val postgresqlVersion = "42.7.5"

dependencies {
    implementation(project(":spane-model"))

    implementation("org.apache.kafka:kafka-clients:$kafkaVersion")
    implementation("ch.qos.logback:logback-classic:$logbackClassicVersion")
    implementation("net.logstash.logback:logstash-logback-encoder:$logbackEncoderVersion")

    implementation("org.flywaydb:flyway-database-postgresql:$flywayVersion")
    implementation("com.zaxxer:HikariCP:$hikariCPVersion")
    implementation("com.github.seratch:kotliquery:1.9.0")
    implementation("org.postgresql:postgresql:$postgresqlVersion")

    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-cio:$ktorVersion")
    implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")

    api("io.micrometer:micrometer-registry-prometheus:1.12.3")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

    testImplementation("io.ktor:ktor-client-cio:$ktorVersion")
    testImplementation("org.awaitility:awaitility:$awaitilityVersion")
    testImplementation("com.github.navikt.tbd-libs:kafka-test:$tbdLibsVersion")
    testImplementation("com.github.navikt.tbd-libs:postgres-testdatabaser:$tbdLibsVersion")
}

tasks {

    jar {
        archiveFileName.set("app.jar")
        mustRunAfter(":spane-visning:compileTestJava",":spane-visning:compileJava",
            ":spane-visning:test",
            ":spane-visning:processTestResources",
            ":spane-visning:jar",
            ":spane-visning:npm_run_build")


        manifest {
            attributes["Main-Class"] = "no.nav.helse.AppKt"
            attributes["Class-Path"] = configurations.runtimeClasspath.get().joinToString(separator = " ") {
                it.name
            }
        }

        from({ project(":spane-visning").layout.buildDirectory.get() }) {
            into("static")
        }

        doLast {
            configurations.runtimeClasspath.get()
                .filter { it.name != "app.jar" }
                .forEach {
                    val file = File("${layout.buildDirectory.get()}/libs/${it.name}")
                    if (!file.exists()) it.copyTo(file)
                }
        }
    }
}
