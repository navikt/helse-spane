package no.nav.helse

import no.nav.helse.SporingEnum.*
import no.nav.helse.TestHjelper.Companion.assertVedtaksperioderAntallOgLengde
import no.nav.helse.TestHjelper.Companion.lagSubsumsjon
import no.nav.helse.TestHjelper.Companion.lagVedtaksPeriode
import no.nav.helse.Vedtaksperiode.Companion.finnEiere
import no.nav.helse.Vedtaksperiode.Companion.seEtterVedtaksperiodeID
import no.nav.helse.Vedtaksperiode.Companion.håndter
import no.nav.helse.Vedtaksperiode.Companion.nyHåndter
import no.nav.helse.Vedtaksperiode.Companion.seEtterSykmeldingsID
import no.nav.helse.Vedtaksperiode.Companion.seEtterSøknadsID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class VedtaksperiodeTest {

    @Test
    fun `subsumsjon med vedtaksperiodeID blir lagt i eksisterende vedtaksperiode etter vedtaksperiodeID`() {
        val eksisterendeSubsumsjonSporing = mapOf(
            "sykmelding" to "s1",
            "soknad" to "sø1",
            "vedtaksperiode" to "v1"
        )
        val nySubsumsjononSporing = mapOf(
            "sykmelding" to "s1",
            "soknad" to "sø1",
            "vedtaksperiode" to "v1"
        )
        val vedtaksperioder = mutableListOf(lagVedtaksPeriode(3, sporing = eksisterendeSubsumsjonSporing))

        vedtaksperioder.seEtterVedtaksperiodeID(lagSubsumsjon(sporing = nySubsumsjononSporing), VEDTAKSPERIODE)

        assertVedtaksperioderAntallOgLengde(vedtaksperioder, 1, 4)
    }

    @Test
    fun `subsumsjon med vedtaksperiodeID blir lagt i eksisterende vedtaksperiode etter søknadsID`() {
        val eksisterendeSubsumsjonSporing = mapOf(
            "sykmelding" to "s1",
            "soknad" to "sø1"
        )
        val nySubsumsjononSporing = mapOf(
            "sykmelding" to "s1",
            "soknad" to "sø1",
            "vedtaksperiode" to "v1"
        )
        val vedtaksperioder = mutableListOf(lagVedtaksPeriode(3, sporing = eksisterendeSubsumsjonSporing))

        vedtaksperioder.seEtterVedtaksperiodeID(lagSubsumsjon(sporing = nySubsumsjononSporing), VEDTAKSPERIODE)

        assertVedtaksperioderAntallOgLengde(vedtaksperioder, 1, 4)

    }

    @Test
    fun `subsumsjon med vedtaksperiodeID blir lagt i eksisterende vedtaksperiode etter sykmeldingsID`() {
        val eksisterendeSubsumsjonSporing = mapOf(
            "sykmelding" to "s1"
        )
        val nySubsumsjononSporing = mapOf(
            "sykmelding" to "s1",
            "soknad" to "sø1",
            "vedtaksperiode" to "v1"
        )
        val vedtaksperioder = mutableListOf(lagVedtaksPeriode(3, sporing = eksisterendeSubsumsjonSporing))

        vedtaksperioder.seEtterVedtaksperiodeID(lagSubsumsjon(sporing = nySubsumsjononSporing), VEDTAKSPERIODE)

        assertVedtaksperioderAntallOgLengde(vedtaksperioder, 1, 4)
    }

    @Test
    fun `subsumsjon med søknadsID blir lagt i eksisterende vedtaksperiode med vedtaksperiode ID etter søknadsID`() {
        val eksisterendeSubsumsjonSporing = mapOf(
            "sykmelding" to "s1",
            "soknad" to "sø1",
            "vedtaksperiode" to "v1"
        )
        val nySubsumsjononSporing = mapOf(
            "sykmelding" to "s1",
            "soknad" to "sø1"
        )
        val vedtaksperioder = mutableListOf(lagVedtaksPeriode(3, sporing = eksisterendeSubsumsjonSporing))

        vedtaksperioder.seEtterSøknadsID(lagSubsumsjon(sporing = nySubsumsjononSporing), SØKNAD)

        assertVedtaksperioderAntallOgLengde(vedtaksperioder, 1, 4)
    }
    @Test
    fun `subsumsjon med søknadsID blir lagt i eksisterende vedtaksperiode etter søknadsID`() {
        val eksisterendeSubsumsjonSporing = mapOf(
            "sykmelding" to "s1",
            "soknad" to "sø1"
        )
        val nySubsumsjononSporing = mapOf(
            "sykmelding" to "s1",
            "soknad" to "sø1"
        )
        val vedtaksperioder = mutableListOf(lagVedtaksPeriode(3, sporing = eksisterendeSubsumsjonSporing))

        vedtaksperioder.seEtterSøknadsID(lagSubsumsjon(sporing = nySubsumsjononSporing), SØKNAD)

        assertVedtaksperioderAntallOgLengde(vedtaksperioder, 1, 4)
    }

    @Test
    fun `subsumsjon med søknadsID blir lagt i eksisterende vedtaksperiode etter sykmeldingsID`() {
        val eksisterendeSubsumsjonSporing = mapOf(
            "sykmelding" to "s1"
        )
        val nySubsumsjononSporing = mapOf(
            "sykmelding" to "s1",
            "soknad" to "sø1",
            "vedtaksperiode" to "v1"
        )
        val vedtaksperioder = mutableListOf(lagVedtaksPeriode(3, sporing = eksisterendeSubsumsjonSporing))

        vedtaksperioder.seEtterSøknadsID(lagSubsumsjon(sporing = nySubsumsjononSporing), SØKNAD)

        assertVedtaksperioderAntallOgLengde(vedtaksperioder, 1, 4)
    }

    //


    @Test
    fun `subsumsjon med sykmeldingsID blir lagt i eksisterende vedtaksperiode med vedtaksperiodeID etter sykmeldingsID`() {
        val eksisterendeSubsumsjonSporing = mapOf(
            "sykmelding" to "s1",
            "soknad" to "sø1",
            "vedtaksperiode" to "v1"
        )
        val nySubsumsjononSporing = mapOf(
            "sykmelding" to "s1"
        )
        val vedtaksperioder = mutableListOf(lagVedtaksPeriode(3, sporing = eksisterendeSubsumsjonSporing))

        vedtaksperioder.seEtterSykmeldingsID(lagSubsumsjon(sporing = nySubsumsjononSporing), SYKMELDING)

        assertVedtaksperioderAntallOgLengde(vedtaksperioder, 1, 4)
    }
    @Test
    fun `subsumsjon med sykmeldingsID blir lagt i eksisterende vedtaksperiode med søknadsID etter sykmeldingsID`() {
        val eksisterendeSubsumsjonSporing = mapOf(
            "sykmelding" to "s1",
            "soknad" to "sø1"
        )
        val nySubsumsjononSporing = mapOf(
            "sykmelding" to "s1"
        )
        val vedtaksperioder = mutableListOf(lagVedtaksPeriode(3, sporing = eksisterendeSubsumsjonSporing))

        vedtaksperioder.seEtterSykmeldingsID(lagSubsumsjon(sporing = nySubsumsjononSporing), SYKMELDING)

        assertVedtaksperioderAntallOgLengde(vedtaksperioder, 1, 4)
    }
    @Test
    fun `subsumsjon med sykmeldingsID blir lagt i eksisterende vedtaksperiode etter sykmeldingsID`() {
        val eksisterendeSubsumsjonSporing = mapOf(
            "sykmelding" to "s1"
        )
        val nySubsumsjononSporing = mapOf(
            "sykmelding" to "s1"
        )
        val vedtaksperioder = mutableListOf(lagVedtaksPeriode(3, sporing = eksisterendeSubsumsjonSporing))

        vedtaksperioder.seEtterSykmeldingsID(lagSubsumsjon(sporing = nySubsumsjononSporing), SYKMELDING)

        assertVedtaksperioderAntallOgLengde(vedtaksperioder, 1, 4)
    }




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

        vedtaksperioder.håndter(lagSubsumsjon(sporing = børDupliseresSporing))

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

        vedtaksperioder.håndter(lagSubsumsjon(sporing = nySubsumsjon))

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


        val resultat = pvps.finnEiere(nySubsumsjon)
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

        val resultat = pvps.finnEiere(nySubsumsjon)

        TODO("Resultat skal være pvpEier og pvpOgsåEier")
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

        TODO("ny subsumsjon skal ha blitt lagt til i vedtaksperiode pvpEier og pvpOgsåEier")
    }




}