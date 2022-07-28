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
    fun `subsumsjon med sykmelidngsid finner rett eier (pvp)`() {
        val sporingEier = mapOf("sykmelding" to listOf("s1"))
        val sporingIkkeEier = mapOf("sykmelding" to listOf("s2"))

        val pvpEier = lagVedtaksPeriode(2, sporing = sporingEier)
        val pvpIkkeEier = lagVedtaksPeriode(5, sporing = sporingIkkeEier)
        val nySubsumsjon = lagSubsumsjon(sporing = sporingEier)

        val pvps = mutableListOf(pvpEier, pvpIkkeEier)


        val resultat = pvps.finnEiere(nySubsumsjon)
        assertEquals(pvpEier, resultat.first())
    }

    @Test
    fun `subsumsjon med søknadsid finner rett eier`() {
        val sporingEier = mapOf("sykmelding" to listOf("s1"), "soknad" to listOf("sø1"))
        val sporingIkkeEier = mapOf("sykmelding" to listOf("s2"), "soknad" to listOf("sø2"))

        val pvpEier = lagVedtaksPeriode(2, sporing = sporingEier)
        val pvpIkkeEier = lagVedtaksPeriode(5, sporing = sporingIkkeEier)
        val nySubsumsjon = lagSubsumsjon(sporing = sporingEier)

        val pvps = mutableListOf(pvpEier, pvpIkkeEier)

        val resultat = pvps.finnEiere(nySubsumsjon)
        assertEquals(pvpEier, resultat.first())
    }

    @Test
    fun `subsumsjon med vedtaksperiodeid finner rett eier`() {
        val sporingEier = mapOf("sykmelding" to listOf("s1"), "soknad" to listOf("sø1"), "vedtaksperiode" to listOf("v1"))
        val sporingIkkeEier = mapOf("sykmelding" to listOf("s2"), "soknad" to listOf("sø2"),  "vedtaksperiode" to listOf("v2"))

        val pvpEier = lagVedtaksPeriode(2, sporing = sporingEier)
        val pvpIkkeEier = lagVedtaksPeriode(5, sporing = sporingIkkeEier)
        val nySubsumsjon = lagSubsumsjon(sporing = sporingEier)

        val pvps = mutableListOf(pvpEier, pvpIkkeEier)

        val resultat = pvps.finnEiere(nySubsumsjon)
        assertEquals(pvpEier, resultat.first())
    }


    @Test
    fun `subsumsjon med sykmeldingid blir lagt til i vedtaksperiode med samme sporing`() {
        val sporingEier = mapOf("sykmelding" to listOf("s1"))
        val sporingIkkeEier = mapOf("sykmelding" to listOf("s2"))

        val pvpEier = lagVedtaksPeriode(2, sporing = sporingEier)
        val pvpIkkeEier = lagVedtaksPeriode(5, sporing = sporingIkkeEier)
        val nySubsumsjon = lagSubsumsjon(sporing = sporingEier)

        val pvps = mutableListOf(pvpEier, pvpIkkeEier)


        pvps.håndter(nySubsumsjon)

        assertEquals(2, pvps.size)
        assertEquals(3, pvps[0].antallSubsumsjoner())
    }

    @Test
    fun `subsumsjon med søknadid blir lagt til i vedtaksperiode med samme sporing`() {
        val sporingEier = mapOf("sykmelding" to listOf("s1"), "soknad" to listOf("sø1"))
        val sporingIkkeEier = mapOf("sykmelding" to listOf("s2"), "soknad" to listOf("sø2"))

        val pvpEier = lagVedtaksPeriode(2, sporing = sporingEier)
        val pvpIkkeEier = lagVedtaksPeriode(5, sporing = sporingIkkeEier)
        val nySubsumsjon = lagSubsumsjon(sporing = sporingEier)

        val pvps = mutableListOf(pvpEier, pvpIkkeEier)

        pvps.håndter(nySubsumsjon)

        assertEquals(2, pvps.size)
        assertEquals(3, pvps[0].antallSubsumsjoner())
    }

    @Test
    fun `subsumsjon med vedtaksperiodeid blir lagt til i vedtaksperiode med samme sporing`() {
        val sporingEier = mapOf("sykmelding" to listOf("s1"), "soknad" to listOf("sø1"), "vedtaksperiode" to listOf("v1"))
        val sporingIkkeEier = mapOf("sykmelding" to listOf("s2"), "soknad" to listOf("sø2"),  "vedtaksperiode" to listOf("v2"))

        val pvpEier = lagVedtaksPeriode(2, sporing = sporingEier)
        val pvpIkkeEier = lagVedtaksPeriode(5, sporing = sporingIkkeEier)
        val nySubsumsjon = lagSubsumsjon(sporing = sporingEier)

        val pvps = mutableListOf(pvpEier, pvpIkkeEier)

        pvps.håndter(nySubsumsjon)

        assertEquals(2, pvps.size)
        assertEquals(3, pvps[0].antallSubsumsjoner())
    }

    @Test
    fun `subsumsjon med søid uten eier oppretter ny vedtaksperiode`() {
        val sporingPVP1 = mapOf("sykmelding" to listOf("s1"))
        val sporingPVP2 = mapOf("sykmelding" to listOf("s2"))
        val sporingNySubsumsjon = mapOf("sykmelding" to listOf("s1"), "soknad" to listOf("sø1"))

        val pvpEier = lagVedtaksPeriode(2, sporing = sporingPVP1)
        val pvpIkkeEier = lagVedtaksPeriode(5, sporing = sporingPVP2)

        val nySubsumsjon = lagSubsumsjon(sporing = sporingNySubsumsjon)

        val pvps = mutableListOf(pvpEier, pvpIkkeEier)

        pvps.finnEiere(nySubsumsjon)

        assertEquals(3, pvps.size)
    }

    //Steg 2
    @Test
    fun `ny vedtaksperiode lages og eksisterende subsumsjoner dupliseres inn`(){
        val sporingPVP1 = mapOf("sykmelding" to listOf("s1"))
        val sporingNySubsumsjon1 = mapOf("sykmelding" to listOf("s1"), "soknad" to listOf("sø1"))
        val sporingNySubsumsjon2 = mapOf("sykmelding" to listOf("s1"), "soknad" to listOf("sø2"))

        val pvpEier = lagVedtaksPeriode(2, sporing = sporingPVP1)
        val pvps = mutableListOf(pvpEier)
        pvps.håndter(lagSubsumsjon(sporing = sporingNySubsumsjon1))
        pvps.håndter(lagSubsumsjon(sporing = sporingNySubsumsjon2))

        assertEquals(3, pvps.size) //Skal være 2, men det skjer ikke før clean up (steg 3) kommer på plass
        assertEquals(3, pvps[1].antallSubsumsjoner())
        assertEquals(3, pvps[2].antallSubsumsjoner())
    }


    @Test
    fun `subsumsjon blir duplisert og lagt til i rette vedtaksperioder`() {
        val sporingSubsumsjon = mapOf("sykmelding" to listOf("s1"))
        val sporingIkkeEier = mapOf("sykmelding" to listOf("s2"))
        val sporingEier1 = mapOf("sykmelding" to listOf("s1"), "soknad" to listOf("sø1"))
        val sporingEier2 = mapOf("sykmelding" to listOf("s1"), "soknad" to listOf("sø2"))

        val pvpEier = lagVedtaksPeriode(2, sporing = sporingEier1)
        val pvpIkkeEier = lagVedtaksPeriode(2, sporing = sporingIkkeEier)
        val pvpOgsåEier = lagVedtaksPeriode(2, sporing = sporingEier2)

        val nySubsumsjon = lagSubsumsjon(sporing = sporingSubsumsjon)

        val pvps = mutableListOf(pvpEier, pvpIkkeEier, pvpOgsåEier)

        pvps.håndter(nySubsumsjon)
        assertEquals(3, pvps.size)
        assertEquals(3, pvps[0].antallSubsumsjoner())
        assertEquals(3, pvps[2].antallSubsumsjoner())
    }

    @Test
    fun `subsumsjon med vid etablerer ny pvp og subsumsjoner med samme søid blir fjernet fra andre pvper `() {
        val sporingPvp1 = mapOf("sykmelding" to listOf("s1"))
        val sporingSubsumsjon1 = mapOf("sykmelding" to listOf("s1"), "soknad" to listOf("sø1"))
        val sporingSubsumsjon2 = mapOf("sykmelding" to listOf("s1"), "soknad" to listOf("sø1"), "vedtaksperiode" to listOf("v1"))

        val pvp1 = lagVedtaksPeriode(2, sporing = sporingPvp1)
        val nySubsumsjon1 = lagSubsumsjon(sporing = sporingSubsumsjon1)
        val nySubsumsjon2 = lagSubsumsjon(sporing = sporingSubsumsjon2)

        val pvps = mutableListOf(pvp1)

        pvps.håndter(nySubsumsjon1)
        pvps.håndter(nySubsumsjon2)

        assertEquals(3, pvps.size) //todo: skal egentlig være 2 npr cleanup blir implementert
        assertEquals(2, pvps[1].antallSubsumsjoner())
        assertEquals(4, pvps[2].antallSubsumsjoner())
    }

    @Test
    @Disabled("Ikke lagt til ennå etter punkt 4 er implementert")
    fun `relevante subsumsjoner dubliseres inn i ny pvp og gammel pvp slettes`() {
        val sporingPVP1 = mapOf("sykmelding" to listOf("s1"))
        val sporingPVP2 = mapOf("sykmelding" to listOf("s2"))
        val sporingNySubsumsjon = mapOf("sykmelding" to listOf("s1"), "soknad" to listOf("sø1"))

        val pvp1 = lagVedtaksPeriode(2, sporing = sporingPVP1)
        val pvp2 = lagVedtaksPeriode(5, sporing = sporingPVP2)

        val nySubsumsjon = lagSubsumsjon(sporing = sporingNySubsumsjon)

        val pvps = mutableListOf(pvp1, pvp2)

        pvps.håndter(nySubsumsjon)

        //Sjekker at det er to vedtaksperioder
        assertEquals(2, pvps.size)

        //Sjekker at eksisterende subsumsjoner fra pvp1 dupliseres inn i ny pvp
        assertEquals(3, pvps[1].antallSubsumsjoner())
    }

}