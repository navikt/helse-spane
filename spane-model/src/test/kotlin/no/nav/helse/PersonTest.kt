package no.nav.helse

import no.nav.helse.TestHjelper.Companion.januar
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.UUID


internal class PersonTest : AbstractPersonTest() {

    @Test
    fun `sjekk sporing`() {
        sendSykmeldingSubsumsjon()
        sendSøknadSubsumsjon()
        sendVedtakSubsumsjon()

        /*
        TODO, når steg 4 blir implementert vil denne gå i stykker
         - vedtaksperiodeIndeks vil være 0 hver gang
         - Nå endrer den seg siden vi lager nye pvper,
           uten å rydde opp i pvper som bare har subsumsjoner som finnes i andre pvper (som er steg 4)
         - løsning bør bare være å fjerne det ekstra parameteret
         */

        assertSporing(sykmeldingUUID)
        assertSporing(sykmeldingUUID)
        assertSporing(sykmeldingUUID, søknadUUID, vedtaksperiodeUUID)
    }


    @Test
    fun `subsumsjon med søkID blir lagt inn i eksisterende vedtaksperiode`() {
        sendSykmeldingSubsumsjon(3)
        sendSøknadSubsumsjon()

        assertAntallVedtaksPerioder(1)
        assertPseudoVedtaksperiodeLengde(0, 4)
    }

    @Test
    fun `eksisterende subsumsjoner blir dubplisert `() {
        sendSykmeldingSubsumsjon(3)
        sendSøknadSubsumsjon()
        sendSøknadSubsumsjon()

        assertAntallVedtaksPerioder(2)
        assertPseudoVedtaksperiodeLengde(0, 4)
        assertPseudoVedtaksperiodeLengde(1, 4)
    }

    @Test
    fun `håndter vedtak_fattet`() {
        sendSykmeldingSubsumsjon()
        sendSøknadSubsumsjon()
        sendVedtakSubsumsjon()
        sendVedtakFattet()
        assertAntallVedtaksPerioder(1)
        assertTilstand("VEDTAK_FATTET")
        assertSkjæringstidspunkt(LocalDate.now())
    }

    @Test
    fun `håndter subsumsjon`() {
        val id = UUID.randomUUID().toString()
        val versjon = "3"
        val eventName = "sub"
        val kilde = "kildee"
        val versjonAvKode = "3"
        val fnr = "1234567890"
        val tidsstempel = 1.januar(2022)
        val lovverk = "loven"
        val lovverksversjon = "3"
        val paragraf = "8-11"
        val utfall = "GODKJENT"

        sendSubsumsjon(id)

        val input = mapOf("skjæringtidspunkt" to "2018-01-01")
        val output = mapOf("antallOpptjeningsdager" to "28")

        assertMap(0, input, "input")
        assertMap(0, output, "output")
        assertString(0, id, "id")
        assertString(0, versjon, "versjon")
        assertString(0, eventName, "eventName")
        assertString(0, kilde, "kilde")
        assertString(0, versjonAvKode, "versjonAvKode")
        assertString(0, fnr, "fødselsnummer")
        assertTidsstempel(0, tidsstempel)
        assertString(0, lovverk, "lovverk")
        assertString(0, lovverksversjon, "lovverksversjon")
        assertString(0, paragraf, "paragraf")
        assertString(0, utfall, "utfall")
    }
}

