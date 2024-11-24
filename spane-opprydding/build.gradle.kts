private val cloudSqlVersion = "1.21.0"
private val postgresqlVersion = "42.7.4"
val hikariCPVersion = "6.1.0"
private val kotliqueryVersion = "1.9.0"
private val flywayVersion = "10.21.0"
private val rapidsAndRiversVersion = "2024112412131732446804.1b3dcc636bed"
val tbdLibsVersion = "2024.11.24-12.01-42fdc22d"
val junitJupiterVersion = "5.11.3"

dependencies {
    api("com.github.navikt:rapids-and-rivers:$rapidsAndRiversVersion")

    implementation("com.google.cloud.sql:postgres-socket-factory:$cloudSqlVersion")
    implementation("org.postgresql:postgresql:$postgresqlVersion")
    implementation("com.github.seratch:kotliquery:$kotliqueryVersion")
    implementation("com.zaxxer:HikariCP:$hikariCPVersion")

    testImplementation(project(":spane-mediators")) // for å få  tilgang på db/migrations-filene
    testImplementation("org.flywaydb:flyway-database-postgresql:$flywayVersion")
    implementation(kotlin("stdlib-jdk8"))

    testImplementation("com.github.navikt.tbd-libs:rapids-and-rivers-test:$tbdLibsVersion")
    testImplementation("com.github.navikt.tbd-libs:postgres-testdatabaser:$tbdLibsVersion")

    testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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
