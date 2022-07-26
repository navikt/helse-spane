package no.nav.helse

import no.nav.helse.SporingEnum.*

class Vedtaksperiode(
    private val subsumsjoner: MutableList<Subsumsjon> = mutableListOf()
) {
    private var alleRelevanteSykmeldingsIDer = mutableListOf<String>()
    private var alleRelevanteSøknadsIDer = mutableListOf<String>()
    private var vedtaksperiodeId: String = ""

    init {
        if (subsumsjoner.isNotEmpty()) {
            for (subsumsjon in subsumsjoner) {
                oppdaterIDer(subsumsjon)
            }
        }
    }

    internal companion object {

        fun MutableList<Vedtaksperiode>.finnEiere(subsumsjon: Subsumsjon): MutableList<Vedtaksperiode> {
            val sporing = subsumsjon.finnSøkeParameter()
            val eiere = mutableListOf<Vedtaksperiode>()
            forEach {
                if (it.sjekkEierskap(subsumsjon, sporing)) {
                    eiere.add(it)
                }
            }
            return eiere
        }

        fun MutableList<Vedtaksperiode>.nyHåndter(subsumsjon: Subsumsjon) {
            // Punkt 1, er det noen PVPer som eier subsumsjon?
            val pvpEiere = finnEiere(subsumsjon)

            // Hvis 1 eller flere PVPer eier subsumsjonen
            if (pvpEiere.isNotEmpty()) {
                for (eier in pvpEiere) {
                    eier.leggTilSubsumsjon(subsumsjon)
                    // legg til subsumsjon i pvp
                }
            } else {
                // hvis nei, ny PvP
                this.add(Vedtaksperiode(mutableListOf(subsumsjon)))
            }

            this.forEach {
                if (pvpEiere.size > 2) {
                    TODO("Heller sette inn duplisiering sjekk i start av nyHåndter for å unngå sjekk her?")
                }
//                pvpEiere[0].vedtaksperiodeId == it.vedtaksperiodeId

                // TODO punkt 2 Er det noen andre PVPer som har subsumsjoner som er relevante for meg?
                // Ja? Legg til i meg
            }

            // Todo punkt 3 Fjern subsumsjoner fra andre pvper som her er søknader jeg eier.
        }
    }

    private fun leggTilSubsumsjon(subsumsjon: Subsumsjon) {
        subsumsjoner.add(subsumsjon)
        oppdaterIDer(subsumsjon)
    }

    private fun oppdaterIDer(subsumsjon: Subsumsjon) {
        val nyVedtaksperiodeId = subsumsjon.hentId(VEDTAKSPERIODE)
        val nySøknadsId = subsumsjon.hentId(SØKNAD)
        val nySykmeldingsId = subsumsjon.hentId(SYKMELDING)

        if (nyVedtaksperiodeId != null) {
            if (vedtaksperiodeId.isEmpty()) {
                vedtaksperiodeId = nyVedtaksperiodeId
            } else if (nyVedtaksperiodeId != vedtaksperiodeId) {
                println("Fant en vedtaksperiodeID ($nyVedtaksperiodeId) som ikke matcher eksisterende ($vedtaksperiodeId), to vedtaksperioder for samme pvp")
                return
            }
        }
        if (nySøknadsId != null && nySøknadsId !in alleRelevanteSøknadsIDer) {
            alleRelevanteSøknadsIDer.add(nySøknadsId)
        }
        if (nySykmeldingsId != null && nySykmeldingsId !in alleRelevanteSykmeldingsIDer) {
            alleRelevanteSykmeldingsIDer.add(nySykmeldingsId)
        }
    }

    fun sjekkEierskap(subsumsjon: Subsumsjon, sporing: SporingEnum): Boolean {
        return when (sporing) {
            VEDTAKSPERIODE -> {
                subsumsjon.sjekkEierskap(sporing, listOf(vedtaksperiodeId))
            }
            SØKNAD -> {
                subsumsjon.sjekkEierskap(sporing, alleRelevanteSøknadsIDer)
            }
            SYKMELDING -> {
                subsumsjon.sjekkEierskap(sporing, alleRelevanteSykmeldingsIDer)
            }
        }
    }

    fun antallSubsumsjoner(): Int {
        return subsumsjoner.size
    }

    fun accept(visitor: VedtaksperiodeVisitor) {
        visitor.preVisitSubsumsjoner()
        subsumsjoner.forEach { it.accept(visitor) }
        visitor.postVisitSubsumsjoner()
    }
}
