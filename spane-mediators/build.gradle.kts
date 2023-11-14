val jacksonVersion = "2.15.3"


dependencies {
    implementation(project(":spane-model"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    implementation("com.github.seratch:kotliquery:1.9.0")
    implementation("org.postgresql:postgresql:42.6.0")

    implementation("com.zaxxer:HikariCP:5.0.1")
}

tasks {

    jar {
        archiveFileName.set("app.jar")
        mustRunAfter(":spane-visning:compileJava",":spane-visning:npm_run_build")


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
