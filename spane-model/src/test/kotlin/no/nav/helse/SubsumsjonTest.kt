package no.nav.helse

import no.nav.helse.Subsumsjon.Companion.finnAlle
import no.nav.helse.Subsumsjon.Companion.eier
import no.nav.helse.TestHjelper.Companion.lagSporing
import no.nav.helse.TestHjelper.Companion.lagSubsumsjon
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class SubsumsjonTest {


    @Test
    fun `filtrer på paragraf`() {
        val subsumsjoner = listOf(lagSubsumsjon("8-11"), lagSubsumsjon("8-13"), lagSubsumsjon("8-13"))
        val resultat = subsumsjoner.finnAlle("8-11")
        assertEquals(1, resultat.size)
    }


    @Test
    fun `avgjør om subsumsjon er relevant`() {
        val sporing = lagSporing(sykmeldingId = listOf("1"))
        val subsumsjon = lagSubsumsjon(sporing = sporing)
        val subsumsjoner = mutableListOf(
            lagSubsumsjon(sporing = sporing),
            lagSubsumsjon(sporing = sporing),
            lagSubsumsjon(sporing = sporing)
        )
        assertTrue(subsumsjoner.eier(subsumsjon))
    }

    @Test
    fun `avgjør om subsumsjon er relevant etter søknadsid`() {
        val sporing = lagSporing(sykmeldingId = listOf("1"))
        val sporing2 = lagSporing(sykmeldingId = listOf("1"), søknadId =  listOf("1"))
        val subsumsjon = lagSubsumsjon(sporing = sporing2)
        val subsumsjoner = mutableListOf(
            lagSubsumsjon(sporing = sporing),
            lagSubsumsjon(sporing = sporing),
            lagSubsumsjon(sporing = sporing)
        )
        assertTrue(subsumsjoner.eier(subsumsjon))
    }


    @Test
    fun `avgjør at subsumsjon ikke er relevant`() {
        val sporing = lagSporing(sykmeldingId = listOf("1"))
        val sporing2 = lagSporing(sykmeldingId = listOf("2"))
        val subsumsjon = lagSubsumsjon(sporing = sporing2)
        val subsumsjoner = mutableListOf(
            lagSubsumsjon(sporing = sporing),
            lagSubsumsjon(sporing = sporing),
            lagSubsumsjon(sporing = sporing)
        )
        assertFalse(subsumsjoner.eier(subsumsjon))
    }

    @Test
    fun `avgjør om subsumsjon med vedtaksperiode ikke er relevant`() {
        val sporing = lagSporing(sykmeldingId = listOf("1"), søknadId =  listOf("2"))
        val sporing1 = lagSporing(sykmeldingId = listOf("1"), søknadId =  listOf("2"))
        val sporing2 = lagSporing(sykmeldingId = listOf("1"), søknadId =  listOf("2"), vedtaksperiodeId =  listOf("3"))
        val subsumsjon = lagSubsumsjon(sporing = sporing)
        val subsumsjoner = mutableListOf(
            lagSubsumsjon(sporing = sporing1),
            lagSubsumsjon(sporing = sporing2)
        )
        assertTrue(subsumsjoner.eier(subsumsjon))
    }
}
