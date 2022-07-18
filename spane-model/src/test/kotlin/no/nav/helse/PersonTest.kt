package no.nav.helse

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
    fun `håndter subsumsjon`() {
        sendSubsumsjon()
        val input = mapOf("skjæringtidspunkt" to "2018-01-01")
        assertInput(0, input)
    }
}

