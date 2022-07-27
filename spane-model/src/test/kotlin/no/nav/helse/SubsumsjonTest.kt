package no.nav.helse

import no.nav.helse.Subsumsjon.Companion.finnAlle
import no.nav.helse.Subsumsjon.Companion.harEierskap
import no.nav.helse.Subsumsjon.Companion.sorterPåTid
import no.nav.helse.TestHjelper.Companion.februar
import no.nav.helse.TestHjelper.Companion.januar
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
    fun `sorter på tid`() {
        val subsumsjoner = listOf(
            lagSubsumsjon(tidsstempel = 31.januar(2022)),
            lagSubsumsjon(tidsstempel = 3.januar(2022)),
            lagSubsumsjon(tidsstempel = 4.februar(2022))
        )

        val sortert = listOf(
            lagSubsumsjon(tidsstempel = 3.januar(2022)),
            lagSubsumsjon(tidsstempel = 31.januar(2022)),
            lagSubsumsjon(tidsstempel = 4.februar(2022))
        )
        assertEquals(sortert, subsumsjoner.sorterPåTid())
    }

    @Test
    fun `avgjør om subsumsjon er relevant`() {
        val sporing = mapOf("sykmelding" to "1")
        val subsumsjon = lagSubsumsjon(sporing = sporing)
        val subsumsjoner = mutableListOf(
            lagSubsumsjon(sporing = sporing),
            lagSubsumsjon(sporing = sporing),
            lagSubsumsjon(sporing = sporing)
        )
        assertTrue(subsumsjoner.harEierskap(subsumsjon, SporingEnum.SYKMELDING))
    }

    @Test
    fun `avgjør om subsumsjon er relevant etter søknadsid`() {
        val sporing = mapOf("sykmelding" to "1")
        val sporing2 = mapOf("sykmelding" to "1", "søknad" to "1")
        val subsumsjon = lagSubsumsjon(sporing = sporing2)
        val subsumsjoner = mutableListOf(
            lagSubsumsjon(sporing = sporing),
            lagSubsumsjon(sporing = sporing),
            lagSubsumsjon(sporing = sporing)
        )
        assertTrue(subsumsjoner.harEierskap(subsumsjon, SporingEnum.SØKNAD))
    }


    @Test
    fun `avgjør at subsumsjon ikke er relevant`() {
        val sporing = mapOf("sykmelding" to "aaa-bbb-ccc")
        val sporing2 = mapOf("sykmelding" to "bbb-bbb-ccc")
        val subsumsjon = lagSubsumsjon(sporing = sporing2)
        val subsumsjoner = mutableListOf(
            lagSubsumsjon(sporing = sporing),
            lagSubsumsjon(sporing = sporing),
            lagSubsumsjon(sporing = sporing)
        )
        assertFalse(subsumsjoner.harEierskap(subsumsjon, SporingEnum.SYKMELDING))
    }

    @Test
    fun `avgjør om subsumsjon skal dupliseres`() {
        val sporingDupliser = mapOf("sykmelding" to "aaa-bbb-ccc")
        val sporingSoknad = mapOf("sykmelding" to "aaa-bbb-ccc", "soknad" to "bbb-bbb-ccc")
        val sporingVedtaksperiode = mapOf(
            "sykmelding" to "aaa-bbb-ccc",
            "soknad" to "bbb-bbb-ccc",
            "vedtaksperiode" to "abc-abc-abc"
        )
        val subsumsjonVedtaksperiode = lagSubsumsjon(sporing = sporingVedtaksperiode)
        val subsumsjonSøknad = lagSubsumsjon(sporing = sporingSoknad)
        val subsumsjonDup = lagSubsumsjon(sporing = sporingDupliser)
        assertEquals(SporingEnum.VEDTAKSPERIODE, subsumsjonVedtaksperiode.finnSøkeParameter())
        assertEquals(SporingEnum.SØKNAD, subsumsjonSøknad.finnSøkeParameter())
        assertEquals(SporingEnum.SYKMELDING, subsumsjonDup.finnSøkeParameter())
    }


    @Test
    fun `avgjør om subsumsjon med vedtaksperiode ikke er relevant`() {
        val sporing = mapOf("sykmelding" to "aaa-bbb-ccc", "soknad" to "bbb-bbb-ccc")
        val sporing1 = mapOf("sykmelding" to "abb-bbb-ccc", "soknad" to "bbb-bbb-ccc")
        val sporing2 = mapOf("sykmelding" to "abb-bbb-ccc", "vedtaksperiode" to "abc-abc-abc")
        val subsumsjon = lagSubsumsjon(sporing = sporing)
        val subsumsjoner = mutableListOf(
            lagSubsumsjon(sporing = sporing1),
            lagSubsumsjon(sporing = sporing2)
        )
        assertTrue(subsumsjoner.harEierskap(subsumsjon, SporingEnum.SØKNAD))
    }
}
