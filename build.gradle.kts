plugins {
    kotlin("jvm") version "2.1.20"
}

val junitJupiterVersion = "5.12.1"

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
        testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }
}
