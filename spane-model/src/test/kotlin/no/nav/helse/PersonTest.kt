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

        assertInput(9)
    }
}

