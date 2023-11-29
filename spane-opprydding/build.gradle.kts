private val testcontainersVersion = "1.18.3"
private val cloudSqlVersion = "1.11.2"
private val postgresqlVersion = "42.6.0"
private val hikariVersion = "5.0.1"
private val kotliqueryVersion = "1.9.0"
private val flywayVersion = "9.3.0"
private val junitVersion = "5.9.0"
private val rapidsAndRiversVersion = "2023050308441683096263.f5a276d7bd28"

val mainClass = "no.nav.helse.opprydding.App"

dependencies {
    api("com.github.navikt:rapids-and-rivers:$rapidsAndRiversVersion")

    implementation("com.google.cloud.sql:postgres-socket-factory:$cloudSqlVersion")
    implementation("org.postgresql:postgresql:$postgresqlVersion")
    implementation("com.github.seratch:kotliquery:$kotliqueryVersion")
    implementation("com.zaxxer:HikariCP:$hikariVersion")

    testImplementation(project(":spane-mediators")) // for å få  tilgang på db/migrations-filene
    testImplementation("org.flywaydb:flyway-core:$flywayVersion")
    testImplementation("org.testcontainers:postgresql:$testcontainersVersion") {
        exclude("com.fasterxml.jackson.core")
    }
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    implementation(kotlin("stdlib-jdk8"))
}

tasks {
    named<Jar>("jar") {
        archiveBaseName.set("app")

        manifest {
            attributes["Main-Class"] = "no.nav.helse.opprydding.App"
            attributes["Class-Path"] = configurations.runtimeClasspath.get().joinToString(separator = " ") {
                it.name
            }
        }
    }
    val copyDeps by registering(Sync::class) {
        from(configurations.runtimeClasspath)
        into("build/libs")
    }
    named("assemble") {
        dependsOn(copyDeps)
    }
}
