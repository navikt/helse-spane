private val cloudSqlVersion = "1.21.0"
private val postgresqlVersion = "42.7.7"
val hikariCPVersion = "6.3.0"
private val kotliqueryVersion = "1.9.0"
private val flywayVersion = "11.5.0"
private val rapidsAndRiversVersion = "2025092210081758528498.1d4b108f2c61"
val tbdLibsVersion = "2025.09.19-15.24-1a9c113f"
val junitJupiterVersion = "5.12.1"

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
