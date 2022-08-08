package no.nav.helse

import no.nav.helse.Pseudovedtaksperiode.Companion.finnEiere
import no.nav.helse.Pseudovedtaksperiode.Companion.håndter
import no.nav.helse.TestHjelper.Companion.inspektør
import no.nav.helse.TestHjelper.Companion.lagSporing
import no.nav.helse.TestHjelper.Companion.lagSporingSyfosSykId
import no.nav.helse.TestHjelper.Companion.lagSubsumsjon
import no.nav.helse.TestHjelper.Companion.lagVedtaksPeriode
import no.nav.helse.TestHjelper.Companion.vedtakFattet
import no.nav.helse.TestHjelper.Companion.vedtaksperiodeForkastet
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate

class PseudovedtaksperiodeTest {


    @Test
    fun `subsumsjon med sykmelidngsid finner rett eier (pvp)`() {
        val sporingEier = lagSporing(sykmeldingId = listOf("s1"))
        val sporingIkkeEier = lagSporing(sykmeldingId = listOf("s2"))

        val pvpEier = lagVedtaksPeriode(2, sporing = sporingEier)
        val pvpIkkeEier = lagVedtaksPeriode(5, sporing = sporingIkkeEier)
        val nySubsumsjon = lagSubsumsjon(sporing = sporingEier)

        val pvps = mutableListOf(pvpEier, pvpIkkeEier)


        val resultat = pvps.finnEiere(nySubsumsjon)
        assertEquals(pvpEier, resultat.first())
    }

    @Test
    fun `subsumsjon med sykmelidngsid fra kilde syfosmregler finner rett eier (pvp)`() {
        val sporingEier = lagSporingSyfosSykId(sykmeldingId = listOf("s1"))
        val sporingIkkeEier = lagSporingSyfosSykId(sykmeldingId = listOf("s2"))

        val pvpEier = lagVedtaksPeriode(2, sporing = sporingEier)
        val pvpIkkeEier = lagVedtaksPeriode(5, sporing = sporingIkkeEier)
        val nySubsumsjon = lagSubsumsjon(sporing = sporingEier)

        val pvps = mutableListOf(pvpEier, pvpIkkeEier)


        val resultat = pvps.finnEiere(nySubsumsjon)
        assertEquals(pvpEier, resultat.first())
    }

    @Test
    fun `subsumsjon med søknadsid og sykmeldingid fra team syfosmregler finner rett eier`() {
        val sporingEier = lagSporingSyfosSykId(sykmeldingId = listOf("s1"), søknadId = listOf("sø1"))
        val sporingIkkeEier = lagSporing(sykmeldingId = listOf("s2"), søknadId = listOf("sø2"))

        val pvpEier = lagVedtaksPeriode(2, sporing = sporingEier)
        val pvpIkkeEier = lagVedtaksPeriode(5, sporing = sporingIkkeEier)
        val nySubsumsjon = lagSubsumsjon(sporing = sporingEier)

        val pvps = mutableListOf(pvpEier, pvpIkkeEier)

        val resultat = pvps.finnEiere(nySubsumsjon)
        assertEquals(pvpEier, resultat.first())
    }

    @Test
    fun `subsumsjon med søknadsid finner rett eier`() {
        val sporingEier = lagSporing(sykmeldingId = listOf("s1"), søknadId = listOf("sø1"))
        val sporingIkkeEier = lagSporing(sykmeldingId = listOf("s2"), søknadId = listOf("sø2"))

        val pvpEier = lagVedtaksPeriode(2, sporing = sporingEier)
        val pvpIkkeEier = lagVedtaksPeriode(5, sporing = sporingIkkeEier)
        val nySubsumsjon = lagSubsumsjon(sporing = sporingEier)

        val pvps = mutableListOf(pvpEier, pvpIkkeEier)

        val resultat = pvps.finnEiere(nySubsumsjon)
        assertEquals(pvpEier, resultat.first())
    }



    @Test
    fun `subsumsjon med vedtaksperiodeid finner rett eier`() {
        val sporingEier =
            lagSporing(sykmeldingId = listOf("s1"), søknadId = listOf("sø1"), vedtaksperiodeId = listOf("v1"))
        val sporingIkkeEier =
            lagSporing(sykmeldingId = listOf("s2"), søknadId = listOf("sø2"), vedtaksperiodeId = listOf("v2"))

        val pvpEier = lagVedtaksPeriode(2, sporing = sporingEier)
        val pvpIkkeEier = lagVedtaksPeriode(5, sporing = sporingIkkeEier)
        val nySubsumsjon = lagSubsumsjon(sporing = sporingEier)

        val pvps = mutableListOf(pvpEier, pvpIkkeEier)

        val resultat = pvps.finnEiere(nySubsumsjon)
        assertEquals(pvpEier, resultat.first())
    }


    @Test
    fun `subsumsjon med sykmeldingid blir lagt til i vedtaksperiode med samme sporing`() {
        val sporingEier = lagSporing(sykmeldingId = listOf("s1"))
        val sporingIkkeEier = lagSporing(sykmeldingId = listOf("s2"))

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
        val sporingEier = lagSporing(sykmeldingId = listOf("s1"), søknadId = listOf("sø1"))
        val sporingIkkeEier = lagSporing(sykmeldingId = listOf("s2"), søknadId = listOf("sø2"))

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
        val sporingEier =
            lagSporing(sykmeldingId = listOf("s1"), søknadId = listOf("sø1"), vedtaksperiodeId = listOf("v1"))
        val sporingIkkeEier =
            lagSporing(sykmeldingId = listOf("s2"), søknadId = listOf("sø2"), vedtaksperiodeId = listOf("v2"))

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
        val sporingPVP1 = lagSporing(sykmeldingId = listOf("s1"))
        val sporingPVP2 = lagSporing(sykmeldingId = listOf("s2"))
        val sporingNySubsumsjon = lagSporing(sykmeldingId = listOf("s1"), søknadId = listOf("sø1"))

        val pvpEier = lagVedtaksPeriode(2, sporing = sporingPVP1)
        val pvpIkkeEier = lagVedtaksPeriode(5, sporing = sporingPVP2)

        val nySubsumsjon = lagSubsumsjon(sporing = sporingNySubsumsjon)

        val pvps = mutableListOf(pvpEier, pvpIkkeEier)

        pvps.finnEiere(nySubsumsjon)

        assertEquals(3, pvps.size)
    }

    @Test
    fun `subsumsjon med vid uten eier oppretter ny vedtaksperiode `() {
        val sporingPvp1 = lagSporing(sykmeldingId = listOf("s1"))
        val sporingSubsumsjon1 =
            lagSporing(sykmeldingId = listOf("s2"), søknadId = listOf("sø2"), vedtaksperiodeId = listOf("v1"))

        val pvp1 = lagVedtaksPeriode(2, sporing = sporingPvp1)
        val nySubsumsjon1 = lagSubsumsjon(sporing = sporingSubsumsjon1)

        val pvps = mutableListOf(pvp1)

        pvps.håndter(nySubsumsjon1)

        assertEquals(2, pvps.size)
    }

    //Steg 2
    @Test
    fun `ny vedtaksperiode lages og eksisterende subsumsjoner dupliseres inn`() {
        val sporingPVP1 = lagSporing(sykmeldingId = listOf("s1"))
        val sporingNySubsumsjon1 = lagSporing(sykmeldingId = listOf("s1"), søknadId = listOf("sø1"))
        val sporingNySubsumsjon2 = lagSporing(sykmeldingId = listOf("s1"), søknadId = listOf("sø2"))

        val pvpEier = lagVedtaksPeriode(2, sporing = sporingPVP1)
        val pvps = mutableListOf(pvpEier)
        pvps.håndter(lagSubsumsjon(sporing = sporingNySubsumsjon1))
        pvps.håndter(lagSubsumsjon(sporing = sporingNySubsumsjon2))

        assertEquals(2, pvps.size)
        assertEquals(3, pvps[0].antallSubsumsjoner())
        assertEquals(3, pvps[1].antallSubsumsjoner())
    }


    @Test
    fun `subsumsjon blir duplisert og lagt til i rette vedtaksperioder`() {
        val sporingSubsumsjon = lagSporing(sykmeldingId = listOf("s1"))
        val sporingIkkeEier = lagSporing(sykmeldingId = listOf("s2"))
        val sporingEier1 = lagSporing(sykmeldingId = listOf("s1"), søknadId = listOf("sø1"))
        val sporingEier2 = lagSporing(sykmeldingId = listOf("s1"), søknadId = listOf("sø2"))

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
    fun `korrigerende søknad merger to pvper til en`() {
        val sporingSykId1 = lagSporing(sykmeldingId = listOf("s1"))
        val sporingSykId2 = lagSporing(sykmeldingId = listOf("s2"))

        val sporingSøkId1 = lagSporing(sykmeldingId = listOf("s1"), søknadId = listOf("sø1"))
        val sporingSøkId2 = lagSporing(sykmeldingId = listOf("s2"), søknadId = listOf("sø2"))

        val sporingVId1 =
            lagSporing(sykmeldingId = listOf("s1"), søknadId = listOf("sø1"), vedtaksperiodeId = listOf("v1"))

        val korrigerendeSporing =
            lagSporing(sykmeldingId = listOf("s2"), søknadId = listOf("sø2"), vedtaksperiodeId = listOf("v1"))

        val subsumsjon = lagSubsumsjon(sporing = korrigerendeSporing)

        val subsumsjoner1 = mutableListOf(
            lagSubsumsjon(sporing = sporingSykId1),
            lagSubsumsjon(sporing = sporingSykId1),
            lagSubsumsjon(sporing = sporingSøkId1),
            lagSubsumsjon(sporing = sporingVId1)
        )
        val pvp1 = Pseudovedtaksperiode(subsumsjoner1)

        val subsumsjoner2 = mutableListOf(
            lagSubsumsjon(sporing = sporingSykId2),
            lagSubsumsjon(sporing = sporingSykId2),
            lagSubsumsjon(sporing = sporingSøkId2)
        )
        val pvp2 = Pseudovedtaksperiode(subsumsjoner2)

        val pvps = mutableListOf(pvp1, pvp2)

        pvps.håndter(subsumsjon)
        assertEquals(1, pvps.size)
        assertEquals(8, pvps[0].antallSubsumsjoner())
    }

    @Test
    fun `relevante subsumsjone dubliseres inn i ny pvp og gammel pvp slettes`() {
        val sporingPVP1 = lagSporing(sykmeldingId = listOf("s1"))
        val sporingPVP2 = lagSporing(sykmeldingId = listOf("s2"))
        val sporingNySubsumsjon = lagSporing(sykmeldingId = listOf("s1"), søknadId = listOf("sø1"))

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

    @Test
    fun `pseudoVedtaksperiode uten mottatt vedtakFattet har status uavklart`() {
        val pvp = lagVedtaksPeriode(1)
        assertEquals("UAVKLART", pvp.inspektør().tilstand)
    }

    @Test
    fun `pseudoVedtaksperiode mottatt vedtakFattet har status vedtak_fattet`() {
        val pvp = lagVedtaksPeriode(1)
        pvp.håndter(vedtakFattet(pvp))
        assertEquals("VEDTAK_FATTET", pvp.inspektør().tilstand)
    }

    @Test
    fun `pseudoVedtaksperiode mottatt vedtaksperiodeForkastet har status vedtaksperiode_forkastet`() {
        val pvp = lagVedtaksPeriode(1)
        pvp.håndter(vedtaksperiodeForkastet(pvp))
        assertEquals("TIL_INFOTRYGD", pvp.inspektør().tilstand)
    }

    @Test
    fun `pseudoVedtaksperiode har usikkert skjæringstidspunkt til vedtakFattet er mottatt`() {
        val pvp = lagVedtaksPeriode(1, input = mapOf("skjæringstidspunkt" to LocalDate.now().toString()))
        assertTrue(pvp.inspektør().ikkeSikkertSkjæringstidspunkt)
        assertEquals(pvp.inspektør().skjæringstidspunkt, LocalDate.now())
        pvp.håndter(vedtakFattet(pvp))
        assertNotNull(pvp.inspektør().skjæringstidspunkt)
    }
}


