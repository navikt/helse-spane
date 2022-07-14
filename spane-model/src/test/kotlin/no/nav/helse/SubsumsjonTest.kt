package no.nav.helse

import no.nav.helse.Subsumsjon.Companion.erRelevant
import no.nav.helse.Subsumsjon.Companion.finnAlle
import no.nav.helse.Subsumsjon.Companion.sorterPåTid
import no.nav.helse.TestHjelper.Companion.februar
import no.nav.helse.TestHjelper.Companion.januar
import no.nav.helse.TestHjelper.Companion.lagSubsumsjon
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.*

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
        val sporing = mapOf("sykmelding" to "aaa-bbb-ccc")
        val subsumsjon = lagSubsumsjon(sporing = sporing)
        val subsumsjoner = mutableListOf(
            lagSubsumsjon(sporing = sporing),
            lagSubsumsjon(sporing = sporing),
            lagSubsumsjon(sporing = sporing)
        )
        assertTrue(subsumsjoner.erRelevant(subsumsjon))
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
        assertFalse(subsumsjoner.erRelevant(subsumsjon))
    }
}