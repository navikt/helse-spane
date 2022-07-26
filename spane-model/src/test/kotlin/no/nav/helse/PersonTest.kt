package no.nav.helse

import no.nav.helse.TestHjelper.Companion.januar
import org.junit.jupiter.api.Test


internal class PersonTest : AbstractPersonTest() {


    @Test
    fun `sjekk sporing`() {
        sendSykmeldingSubsumsjon()
        sendSøknadSubsumsjon()
        sendVedtakSubsumsjon()
        assertSporing(0, sykmeldingUUID)
        assertSporing(1, sykmeldingUUID, søknadUUID)
        assertSporing(2, sykmeldingUUID, søknadUUID, vedtaksperiodeUUID)
    }


    @Test
    fun `subsumsjon med søkID blir lagt inn i eksisterende vedtaksperiode`() {
        sendSykmeldingSubsumsjon(3)
        sendSøknadSubsumsjon()

        assertVedtaksperiodeLengder(1, 4)
    }

    @Test
    fun `eksisterende subsumsjoner blir dubplisert `() {
        // skal bli duplisert dersom ny subsumsjon med annen søknadsid kommer inn
        sendSykmeldingSubsumsjon(3)
        sendSøknadSubsumsjon()
        sendSøknadSubsumsjon()

        assertVedtaksperiodeLengder(2, 4, 4)

    }






    @Test
    fun `håndter subsumsjon`() {
        val id = "id"
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
        sendSubsumsjon()

        val input = mapOf("skjæringtidspunkt" to "2018-01-01")
        val output = mapOf("antallOpptjeningsdager" to "28")

        assertMap(0, input, "input")
        assertMap(0, output, "output")
        assertString(0, id,"id")
        assertString(0, versjon,"versjon")
        assertString(0, eventName,"eventName")
        assertString(0, kilde,"kilde")
        assertString(0, versjonAvKode,"versjonAvKode")
        assertString(0, fnr,"fødselsnummer")
        assertTidsstempel(0, tidsstempel)
        assertString(0, lovverk,"lovverk")
        assertString(0, lovverksversjon,"lovverksversjon")
        assertString(0, paragraf,"paragraf")
        assertString(0, utfall, "utfall")
    }
}

