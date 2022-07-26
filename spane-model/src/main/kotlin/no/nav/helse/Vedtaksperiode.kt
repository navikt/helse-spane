package no.nav.helse

import no.nav.helse.SporingEnum.*

class Vedtaksperiode(
    subsumsjon: Subsumsjon? = null
) {
    private var alleRelevanteSykmeldingsIDer = mutableListOf<String>()
    private var alleRelevanteSøknadsIDer = mutableListOf<String>()
    private var vedtaksperiodeId: String = ""
    private val subsumsjoner: MutableList<Subsumsjon> = mutableListOf()


    init {
        if (subsumsjon != null) {
            leggTilSubsumsjon(mutableListOf(subsumsjon))
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

        fun MutableList<Vedtaksperiode>.nyErRelevant(pvpEiere: MutableList<Vedtaksperiode>) {


            if (pvpEiere.size == 0) {
                return
            }
            val eier = pvpEiere[0]

            println(eier.subsumsjoner.size)
            //                // TODO punkt 2 Er det noen andre PVPer som har subsumsjoner som er relevante for meg?
//                // Ja? Legg til i meg

            forEach {
                if (it == eier) {
                    //Siden den looper gjennom alle pvper sammenligner den også med seg selv
                    return
                }

                if (it.vedtaksperiodeId.isNotEmpty()) {
                    if (eier.vedtaksperiodeId == it.vedtaksperiodeId) {
                        eier.leggTilSubsumsjon(it.subsumsjoner)
                    }
                } else if (eier.alleRelevanteSøknadsIDer == it.alleRelevanteSøknadsIDer) {
                    eier.leggTilSubsumsjon(it.subsumsjoner)

                    // SAMME SID OG SØID, IKKE VID
                } else if (eier.alleRelevanteSykmeldingsIDer == it.alleRelevanteSykmeldingsIDer) {
                    eier.leggTilSubsumsjon(it.subsumsjoner)

                    // SAMME SID, IKKE SØID
                } else {
                    println("Fant ingen relevante")
                }
            }

            // TODO mangler cleanup


        }

        fun MutableList<Vedtaksperiode>.nyHåndter(subsumsjon: Subsumsjon) {
            // Punkt 1, er det noen PVPer som eier subsumsjon?
            val pvpEiere = finnEiere(subsumsjon)

            // Hvis 1 eller flere PVPer eier subsumsjonen
            if (pvpEiere.isNotEmpty()) {
                for (eier in pvpEiere) {
                    eier.leggTilSubsumsjon(mutableListOf(subsumsjon))
                    // legg til subsumsjon i pvp
                }
            } else {
                // hvis nei, ny PvP

                this.add(Vedtaksperiode(subsumsjon))
                pvpEiere.add(this[lastIndex])
            }


            this.nyErRelevant(pvpEiere)


//            this.forEach {
//                if (pvpEiere.size > 2) {
//                    TODO("Heller sette inn duplisiering sjekk i start av nyHåndter for å unngå sjekk her?")
//                }
//
//
////                pvpEiere[0].vedtaksperiodeId == it.vedtaksperiodeId
//

//            }

            // Todo punkt 3 Fjern subsumsjoner fra andre pvper som her er søknader jeg eier.
        }
    }

    fun leggTilSubsumsjon(nyeSubsumsjoner: MutableList<Subsumsjon>) {
        for (subsumsjon in nyeSubsumsjoner) {
            this.subsumsjoner.add(subsumsjon)
            oppdaterIDer(subsumsjon)
        }
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
