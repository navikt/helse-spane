package no.nav.helse

import no.nav.helse.SporingEnum.*
import no.nav.helse.TestHjelper.Companion.assertVedtaksperioderAntallOgLengde
import no.nav.helse.TestHjelper.Companion.lagSubsumsjon
import no.nav.helse.TestHjelper.Companion.lagVedtaksPeriode
import no.nav.helse.Vedtaksperiode.Companion.etablerEierskap
import no.nav.helse.Vedtaksperiode.Companion.nyHåndter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class VedtaksperiodeTest {



    @Test
    fun `subsumsjon blir duplisert og lagt til i alle rette vedtaksperioder`() {
        val børDupliseresSporing = mapOf(
            "sykmelding" to "relevant",
        )
        val relevantSporing = mapOf(
            "sykmelding" to "relevant",
            "soknad" to "relevant",
            "vedtaksperiode" to "relevant"
        )
        val ikkeRelevantSporing = mapOf(
            "sykmelding" to "ikke-relevant",
            "soknad" to "ikke-relevant",
            "vedtaksperiode" to "ikke-relevant"
        )


        val vedtaksperioder = mutableListOf(
            lagVedtaksPeriode(3, sporing = relevantSporing),
            lagVedtaksPeriode(3, sporing = relevantSporing),
            lagVedtaksPeriode(2, sporing = ikkeRelevantSporing)
        )

        vedtaksperioder.nyHåndter(lagSubsumsjon(sporing = børDupliseresSporing))

        assertEquals(4, vedtaksperioder[0].antallSubsumsjoner())
        assertEquals(4, vedtaksperioder[1].antallSubsumsjoner())
        assertEquals(2, vedtaksperioder[2].antallSubsumsjoner())
    }

    @Test
    fun `subsumsjon blir lagt inn rett sted basert på søknad `() {
        val nySubsumsjon = mapOf(
            "sykmelding" to "relevant",
            "soknad" to "relevant"
        )
        val relevantSporing = mapOf(
            "sykmelding" to "ikke-relevant",
            "soknad" to "relevant",
            "vedtaksperiode" to "relevant"
        )
        val ikkeRelevantSporing = mapOf(
            "sykmelding" to "ikke-relevant",
            "soknad" to "ikke-relevant",
            "vedtaksperiode" to "ikke-relevant"
        )


        val vedtaksperioder = mutableListOf(
            lagVedtaksPeriode(3, sporing = relevantSporing),
            lagVedtaksPeriode(2, sporing = ikkeRelevantSporing)
        )

        vedtaksperioder.nyHåndter(lagSubsumsjon(sporing = nySubsumsjon))

        assertEquals(4, vedtaksperioder[0].antallSubsumsjoner())
        assertEquals(2, vedtaksperioder[1].antallSubsumsjoner())
    }



    /* NYE TESTCASE */

    @Test
    fun `subsumsjon finner rett eier (pvp)`() {
        val sporingEier = mapOf("sykmelding" to "s1")
        val sporingIkkeEier = mapOf("sykmelding" to "s2")

        val pvpEier = lagVedtaksPeriode(2, sporing = sporingEier)
        val pvpIkkeEier = lagVedtaksPeriode(2, sporing = sporingIkkeEier)
        val nySubsumsjon = lagSubsumsjon(sporing= sporingEier)

        val pvps = mutableListOf(pvpEier,pvpIkkeEier)

        val resultat = pvps.etablerEierskap(nySubsumsjon)
        assertEquals(pvpEier, resultat)
    }


    @Test
    fun `subsumsjon blir lagt til i rett vedtaksperiode testcase 1`() {
        val sporingSykmelding = mapOf("sykmelding" to "s1")

        val vedtaksperiode = lagVedtaksPeriode(2, sporing = sporingSykmelding)
        val nySubsumsjon = lagSubsumsjon(sporing= sporingSykmelding)

        val pvps = mutableListOf(vedtaksperiode)


        pvps.nyHåndter(nySubsumsjon)

        assertEquals(1, pvps.size)
        assertEquals(3, pvps[0].antallSubsumsjoner())
    }

    @Test
    fun `subsumsjon finner rett eiere (pvp) duplisering`() {
        val sporingSubsumsjon = mapOf("sykmelding" to "s1")
        val sporingIkkeEier = mapOf("sykmelding" to "s2")
        val sporingEier = mapOf("sykmelding" to "s1", "soknad" to "sø1")
        val sporingOgsåEier = mapOf("sykmelding" to "s1", "soknad" to "sø2")

        val pvpEier = lagVedtaksPeriode(2, sporing = sporingEier)
        val pvpIkkeEier = lagVedtaksPeriode(2, sporing = sporingIkkeEier)
        val pvpOgsåEier = lagVedtaksPeriode(2, sporing = sporingOgsåEier)

        val nySubsumsjon = lagSubsumsjon(sporing= sporingSubsumsjon)

        val pvps = mutableListOf(pvpEier,pvpIkkeEier, pvpOgsåEier)

        pvps.nyHåndter(nySubsumsjon)
        assertEquals(3, pvps.size)
        assertEquals(3, pvps[0].antallSubsumsjoner())
        assertEquals(3, pvps[2].antallSubsumsjoner())
    }
    @Test
    fun `subsumsjon blir duplisert og lagt til i rette vedtaksperioder`() {
        // denne gjør det samme som testen over så er muligens overfløoidg?????

        val sporingSubsumsjon = mapOf("sykmelding" to "s1")
        val sporingIkkeEier = mapOf("sykmelding" to "s2")
        val sporingEier = mapOf("sykmelding" to "s1", "soknad" to "sø1")
        val sporingOgsåEier = mapOf("sykmelding" to "s1", "soknad" to "sø2")

        val pvpEier = lagVedtaksPeriode(2, sporing = sporingEier)
        val pvpIkkeEier = lagVedtaksPeriode(2, sporing = sporingIkkeEier)
        val pvpOgsåEier = lagVedtaksPeriode(2, sporing = sporingOgsåEier)

        val nySubsumsjon = lagSubsumsjon(sporing= sporingSubsumsjon)

        val pvps = mutableListOf(pvpEier,pvpIkkeEier, pvpOgsåEier)


        pvps.nyHåndter(nySubsumsjon)
        assertEquals(3, pvps.size)
        assertEquals(3, pvps[0].antallSubsumsjoner())
        assertEquals(3, pvps[2].antallSubsumsjoner())

    }



}