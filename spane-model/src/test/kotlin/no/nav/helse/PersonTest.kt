package no.nav.helse

import no.nav.helse.TestHjelper.Companion.lagSubsumsjon
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.util.*

internal class PersonTest {

    val person = Person()
    lateinit var sykmeldingUUID: UUID
    lateinit var søknadUUID: UUID
    lateinit var vedtaksperiodeUUID: UUID

    @Test
    fun `håndter subsumsjon`() {
        sendSykmeldingSubsumsjon()
        sendSøknadSubsumsjon()
        sendVedtakSubsumsjon()
        assertEquals(1, person.antallVedtaksperioder())
    }

    fun sendSykmeldingSubsumsjon() {
        sykmeldingUUID = UUID.randomUUID()
        val sykmeldingSubsumsjon = lagSubsumsjon(sporing = mapOf("sykmelding" to sykmeldingUUID))
        person.håndter(sykmeldingSubsumsjon)
    }

    fun sendSøknadSubsumsjon() {
        søknadUUID = UUID.randomUUID()
        val subsumsjon = lagSubsumsjon(
            sporing = mapOf(
                "sykmelding" to sykmeldingUUID,
                "soknad" to søknadUUID
            )
        )
        person.håndter(subsumsjon)
    }
    fun sendVedtakSubsumsjon() {
        vedtaksperiodeUUID = UUID.randomUUID()
        val subsumsjon = lagSubsumsjon(
            sporing = mapOf(
                "sykmelding" to sykmeldingUUID,
                "soknad" to søknadUUID,
                "vedtaksperiode" to vedtaksperiodeUUID
            )
        )
        person.håndter(subsumsjon)
    }

//    fun lagSubsumsjon(
//        paragraf: String = "8-11",
//        sporing: Map<String, Any> = mapOf("sykmelding" to UUID.randomUUID()),
//        tidsstempel: ZonedDateTime = 1.januar(2022),
//    ): Subsumsjon {
//        return Subsumsjon(
//            "id", "3", "sub", "kildee", "3",
//            "1234567890", sporing, tidsstempel, "loven", "3",
//            paragraf, null, null, null, emptyMap(), emptyMap(), "GODKJENT"
//        )
//    }
}