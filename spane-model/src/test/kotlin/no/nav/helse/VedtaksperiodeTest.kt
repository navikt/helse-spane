package no.nav.helse

import no.nav.helse.TestHjelper.Companion.lagSubsumsjon
import no.nav.helse.TestHjelper.Companion.lagVedtaksPeriode
import no.nav.helse.Vedtaksperiode.Companion.finnEiere
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


    @Test
    fun `subsumsjon finner rett eier (pvp)`() {
        val sporingEier = mapOf("sykmelding" to "s1")
        val sporingIkkeEier = mapOf("sykmelding" to "s2")

        val pvpEier = lagVedtaksPeriode(2, sporing = sporingEier)
        val pvpIkkeEier = lagVedtaksPeriode(2, sporing = sporingIkkeEier)
        val nySubsumsjon = lagSubsumsjon(sporing= sporingEier)

        val pvps = mutableListOf(pvpEier,pvpIkkeEier)


        val resultat = pvps.finnEiere(nySubsumsjon)
        assertEquals(pvpEier, resultat[0])
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

        val resultat = pvps.finnEiere(nySubsumsjon)

        assertEquals(listOf(pvpEier, pvpOgsåEier),resultat)
    }
    @Test
    fun `subsumsjon blir duplisert og lagt til i rette vedtaksperioder`() {
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
    fun `case 2, finner ingen eier`() {
        val sporingPVP1 = mapOf("sykmelding" to "s1")
        val sporingPVP2 = mapOf("sykmelding" to "s2")
        val sporingNySubsumsjon = mapOf("sykmelding" to "s1", "soknad" to "sø1")

        val pvpEier = lagVedtaksPeriode(2, sporing = sporingPVP1)
        val pvpIkkeEier = lagVedtaksPeriode(2, sporing = sporingPVP2)

        val nySubsumsjon = lagSubsumsjon(sporing= sporingNySubsumsjon)

        val pvps = mutableListOf(pvpEier,pvpIkkeEier)

        val resultat = pvps.finnEiere(nySubsumsjon)

        assertEquals(listOf<Vedtaksperiode>(),resultat)
    }
    @Test
    fun `case 2, ny pvp blir lagd og relevante subsumsjoner blir lagt til og `() {
        val sporingPVP1 = mapOf("sykmelding" to "s1")
        val sporingPVP2 = mapOf("sykmelding" to "s2")
        val sporingNySubsumsjon = mapOf("sykmelding" to "s1", "soknad" to "sø1")

        val pvpEier = lagVedtaksPeriode(2, sporing = sporingPVP1)
        val pvpIkkeEier = lagVedtaksPeriode(2, sporing = sporingPVP2)

        val nySubsumsjon = lagSubsumsjon(sporing= sporingNySubsumsjon)

        val pvps = mutableListOf(pvpEier,pvpIkkeEier)

        pvps.nyHåndter(nySubsumsjon)

        assertEquals(3, pvps[2].antallSubsumsjoner())
    }
    @Test
    fun `case 2, rydder opp gammel pvp, bare nye er igjen`() {
        // denne og den over vil stort sett bare feile sammen
        val sporingPVP1 = mapOf("sykmelding" to "s1")
        val sporingPVP2 = mapOf("sykmelding" to "s2")
        val sporingNySubsumsjon = mapOf("sykmelding" to "s1", "soknad" to "sø1")

        val pvpEier = lagVedtaksPeriode(2, sporing = sporingPVP1)
        val pvpIkkeEier = lagVedtaksPeriode(2, sporing = sporingPVP2)

        val nySubsumsjon = lagSubsumsjon(sporing= sporingNySubsumsjon)

        val pvps = mutableListOf(pvpEier,pvpIkkeEier)

        pvps.nyHåndter(nySubsumsjon)

        assertEquals(2, pvps.size)
    }




}