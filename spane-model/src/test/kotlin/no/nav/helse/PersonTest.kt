package no.nav.helse

import org.junit.jupiter.api.Test
import java.time.ZonedDateTime
import org.junit.jupiter.api.Assertions.*

internal class PersonTest {


    @Test
    fun `håndter subsumsjon - sykmelding`() {
        val subsumsjon = lagSubsumsjon()
        val person = Person()
        person.håndter(subsumsjon)
        assertEquals(1, person.antallVedtaksperioder)
    }



    fun lagSubsumsjon(paragraf: String = "8-11", tidsstempel: ZonedDateTime = 1.januar(2022)): Subsumsjon {
        return Subsumsjon(
            "id", "3", "sub", "kildee", "3",
            "1234567890", emptyMap(), tidsstempel, "loven", "3",
            paragraf, null, null, null, emptyMap(), emptyMap(), "GODKJENT"
        )
    }
}