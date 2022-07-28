package no.nav.helse

import no.nav.helse.TestHjelper.Companion.januar
import org.junit.jupiter.api.Test
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
        assertSporing(sykmeldingUUID, søknadUUID, vedtaksperiodeIndeks = 1)
        assertSporing(sykmeldingUUID, søknadUUID, vedtaksperiodeUUID, vedtaksperiodeIndeks = 2)
    }


    @Test
    fun `subsumsjon med søkID blir lagt inn i eksisterende vedtaksperiode`() {
        sendSykmeldingSubsumsjon(3)
        sendSøknadSubsumsjon()

        /*
        TODO, når steg 4 blir implementert vil denne gå i stykker
         - antallPseudoVedtaksperioder() skal ta inn 1 (da den overflødige pvpen blir slettet om steg 4 er implementert.
         - assertPseudoVedtaksperiodeLengde() skal ta inn 0,4, da ny pvp vil være på indeks 0 etter overflødig er slettet
         */
        assertAntallVedtaksPerioder(2)
        assertPseudoVedtaksperiodeLengde(1, 4)
    }

    @Test
    fun `eksisterende subsumsjoner blir dubplisert `() {
        sendSykmeldingSubsumsjon(3)
        sendSøknadSubsumsjon()
        sendSøknadSubsumsjon()

        /*
        TODO, når steg 4 blir implementert vil denne gå i stykker
         - antallPseudoVedtaksperioder() skal ta inn 2 (da den overflødige pvpen blir slettet om steg 4 er implementert.
         - assertPseudoVedtaksperiodeLengde() skal ta inn 0,4 og 1,4 , da nye pvp vil være på indeks 0 og 1 etter overflødig er slettet
         */

        assertAntallVedtaksPerioder(3)
        assertPseudoVedtaksperiodeLengde(1, 4)
        assertPseudoVedtaksperiodeLengde(2, 4)
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

