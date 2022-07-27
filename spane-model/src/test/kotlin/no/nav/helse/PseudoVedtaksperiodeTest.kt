package no.nav.helse

import no.nav.helse.TestHjelper.Companion.lagSubsumsjon
import no.nav.helse.TestHjelper.Companion.lagVedtaksPeriode
import no.nav.helse.PseudoVedtaksperiode.Companion.finnEiere
import no.nav.helse.PseudoVedtaksperiode.Companion.håndter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class PseudoVedtaksperiodeTest {


    @Test
    fun `subsumsjon finner rett eier (pvp)`() {
        val sporingEier = mapOf("sykmelding" to listOf("s1"))
        val sporingIkkeEier = mapOf("sykmelding" to listOf("s2"))

        val pvpEier = lagVedtaksPeriode(2, sporing = sporingEier)
        val pvpIkkeEier = lagVedtaksPeriode(2, sporing = sporingIkkeEier)
        val nySubsumsjon = lagSubsumsjon(sporing= sporingEier)

        val pvps = mutableListOf(pvpEier,pvpIkkeEier)


        val resultat = pvps.finnEiere(nySubsumsjon)
        assertEquals(pvpEier, resultat.first())
    }


    @Test
    fun `subsumsjon blir lagt til i rett vedtaksperiode testcase 1`() {
        val sporingSykmelding = mapOf("sykmelding" to listOf("s1"))

        val vedtaksperiode = lagVedtaksPeriode(2, sporing = sporingSykmelding)
        val nySubsumsjon = lagSubsumsjon(sporing= sporingSykmelding)

        val pvps = mutableListOf(vedtaksperiode)

        pvps.håndter(nySubsumsjon)

        assertEquals(1, pvps.size)
        assertEquals(3, pvps[0].antallSubsumsjoner())
    }

    @Test
    fun `subsumsjon finner rett eiere (pvp) duplisering`() {
        val sporingSubsumsjon = mapOf("sykmelding" to listOf("s1"))
        val sporingIkkeEier = mapOf("sykmelding" to listOf("s2"))
        val sporingEier = mapOf("sykmelding" to listOf("s1"), "soknad" to listOf("sø1"))
        val sporingOgsåEier = mapOf("sykmelding" to listOf("s1"), "soknad" to listOf("sø2"))

        val pvpEier = lagVedtaksPeriode(2, sporing = sporingEier)
        val pvpIkkeEier = lagVedtaksPeriode(2, sporing = sporingIkkeEier)
        val pvpOgsåEier = lagVedtaksPeriode(2, sporing = sporingOgsåEier)

        val nySubsumsjon = lagSubsumsjon(sporing= sporingSubsumsjon)

        val pvps = mutableListOf(pvpEier,pvpIkkeEier, pvpOgsåEier)

        pvps.håndter(nySubsumsjon)
        pvps.forEach { println(it.antallSubsumsjoner()) }
        assertEquals(3, pvps.size)
        assertEquals(3, pvps[0].antallSubsumsjoner())
        assertEquals(3, pvps[2].antallSubsumsjoner())
    }
    @Test
    fun `subsumsjon blir duplisert og lagt til i rette vedtaksperioder`() {
        val sporingSubsumsjon = mapOf("sykmelding" to listOf("s1"))
        val sporingIkkeEier = mapOf("sykmelding" to listOf("s2"))
        val sporingEier = mapOf("sykmelding" to listOf("s1"), "soknad" to listOf("sø1"))
        val sporingOgsåEier = mapOf("sykmelding" to listOf("s1"), "soknad" to listOf("sø2"))

        val pvpEier = lagVedtaksPeriode(2, sporing = sporingEier)
        val pvpIkkeEier = lagVedtaksPeriode(2, sporing = sporingIkkeEier)
        val pvpOgsåEier = lagVedtaksPeriode(2, sporing = sporingOgsåEier)

        val nySubsumsjon = lagSubsumsjon(sporing= sporingSubsumsjon)

        val pvps = mutableListOf(pvpEier,pvpIkkeEier, pvpOgsåEier)


        pvps.håndter(nySubsumsjon)
        assertEquals(3, pvps.size)
        assertEquals(3, pvps[0].antallSubsumsjoner())
        assertEquals(3, pvps[2].antallSubsumsjoner())
    }
    @Test
    @Disabled("Ikke lagt til ennå")
    fun `case 2, finner ingen eier`() {
        val sporingPVP1 = mapOf("sykmelding" to listOf("s1"))
        val sporingPVP2 = mapOf("sykmelding" to listOf("s2"))
        val sporingNySubsumsjon = mapOf("sykmelding" to listOf("s1"), "soknad" to listOf("sø1"))

        val pvpEier = lagVedtaksPeriode(2, sporing = sporingPVP1)
        val pvpIkkeEier = lagVedtaksPeriode(2, sporing = sporingPVP2)

        val nySubsumsjon = lagSubsumsjon(sporing= sporingNySubsumsjon)

        val pvps = mutableListOf(pvpEier,pvpIkkeEier)

        val resultat = pvps.finnEiere(nySubsumsjon)

        assertEquals(listOf<PseudoVedtaksperiode>(),resultat)
    }
    @Test
    @Disabled("Ikke lagt til ennå")
    fun `case 2, ny pvp blir lagd og relevante subsumsjoner blir lagt til og `() {
        val sporingPVP1 = mapOf("sykmelding" to listOf("s1"))
        val sporingPVP2 = mapOf("sykmelding" to listOf("s2"))
        val sporingNySubsumsjon = mapOf("sykmelding" to listOf("s1"), "soknad" to listOf("sø1"))

        val pvpEier = lagVedtaksPeriode(2, sporing = sporingPVP1)
        val pvpIkkeEier = lagVedtaksPeriode(2, sporing = sporingPVP2)

        val nySubsumsjon = lagSubsumsjon(sporing= sporingNySubsumsjon)

        val pvps = mutableListOf(pvpEier,pvpIkkeEier)

        pvps.håndter(nySubsumsjon)

        assertEquals(3, pvps[2].antallSubsumsjoner())
    }
    @Test
    @Disabled("ikke begynt på case 2 ennå")
    fun `case 2, rydder opp gammel pvp, bare nye er igjen`() {
        // denne og den over vil stort sett bare feile sammen
        val sporingPVP1 = mapOf("sykmelding" to listOf("s1"))
        val sporingPVP2 = mapOf("sykmelding" to listOf("s2"))
        val sporingNySubsumsjon = mapOf("sykmelding" to listOf("s1"), "soknad" to listOf("sø1"))

        val pvpEier = lagVedtaksPeriode(2, sporing = sporingPVP1)
        val pvpIkkeEier = lagVedtaksPeriode(2, sporing = sporingPVP2)

        val nySubsumsjon = lagSubsumsjon(sporing= sporingNySubsumsjon)

        val pvps = mutableListOf(pvpEier,pvpIkkeEier)

        pvps.håndter(nySubsumsjon)

        assertEquals(2, pvps.size)
    }
}