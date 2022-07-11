package no.nav.helse

import org.junit.jupiter.api.Test
import java.time.ZonedDateTime
import org.junit.jupiter.api.Assertions.*
import java.util.*

internal class PersonTest {


    @Test
    fun `håndter subsumsjon`() {
        val sykmeldingUUID = UUID.randomUUID()
        val søknadUUID = UUID.randomUUID()
        val vedtaksperiodeUUID = UUID.randomUUID()

        val sykmeldingSubsumsjon = lagSubsumsjon(sporing = mapOf("sykmelding" to sykmeldingUUID))
        val søknadSubsumsjon =
            lagSubsumsjon(sporing = mapOf("sykmelding" to sykmeldingUUID, "soknad" to søknadUUID))
        val vedtaksperiodeSubsumsjon = lagSubsumsjon(
            sporing = mapOf(
                "sykmelding" to sykmeldingUUID,
                "soknad" to søknadUUID,
                "vedtaksperiode" to vedtaksperiodeUUID
            )
        )
        val person = Person()
        person.håndter(sykmeldingSubsumsjon)
        person.håndter(søknadSubsumsjon)
        person.håndter(vedtaksperiodeSubsumsjon)
        assertEquals(1, person.antallVedtaksperioder())
    }


    fun lagSubsumsjon(
        paragraf: String = "8-11",
        sporing: Map<String, Any> = mapOf("sykmelding" to UUID.randomUUID()),
        tidsstempel: ZonedDateTime = 1.januar(2022),
    ): Subsumsjon {
        return Subsumsjon(
            "id", "3", "sub", "kildee", "3",
            "1234567890", sporing, tidsstempel, "loven", "3",
            paragraf, null, null, null, emptyMap(), emptyMap(), "GODKJENT"
        )
    }
}