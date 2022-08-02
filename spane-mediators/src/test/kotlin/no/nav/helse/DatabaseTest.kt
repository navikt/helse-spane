package no.nav.helse

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File

internal class DatabaseTest: AbstraktDatabaseTest() {

    @Test
    @Disabled("ikke ferdig skrevet, skal sikkert droppes")
    fun `lagre person i database`() {

        val json = File("/test/kotlin/resources/testPerson.json").inputStream().bufferedReader().use { it.readText() }
        assertPersonLagret(json)

    }

}