import java.nio.file.Paths

val jacksonVersion = "2.12.5"
val hikariVersion = "5.0.1"
val kotliqueryVersion = "1.7.0"
val postgresqlVersion = "42.3.3"



dependencies {
    implementation(project(":spane-model"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    implementation("com.github.seratch:kotliquery:$kotliqueryVersion")
    implementation("org.postgresql:postgresql:$postgresqlVersion")

    implementation("com.zaxxer:HikariCP:$hikariVersion")
}

tasks {

    jar {
        archiveFileName.set("app.jar")
        mustRunAfter(":spane-visning:npm_run_build")


        manifest {
            attributes["Main-Class"] = "no.nav.helse.AppKt"
            attributes["Class-Path"] = configurations.runtimeClasspath.get().joinToString(separator = " ") {
                it.name
            }
        }

        from({ Paths.get(project(":spane-visning").buildDir.path) }) {
            into("static")
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
