package no.nav.helse

import no.nav.helse.SporingEnum.*
import no.nav.helse.Subsumsjon.Companion.erRelevant
import no.nav.helse.Subsumsjon.Companion.finnAlleUtenSøknadId

class Vedtaksperiode(
    private val subsumsjoner: MutableList<Subsumsjon>
    // liste med alle subsumsjoner med kun sykemelding - ikke begrenset til hver vedtaksperiode, men det totale antallet.
) {

    private val alleRelevanteSykmeldingsIDer = mutableListOf<String>()
    private val alleRelevanteSøknadsIDer = mutableListOf<String>()
    private lateinit var vedtaksperiodeId : String



    fun sjekkEierskap(subsumsjon: Subsumsjon, sporing: SporingEnum) : Boolean {
        return when(sporing){
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


    internal companion object {
        fun MutableList<Vedtaksperiode>.lagNyVedtaksperiode(subsumsjoner: MutableList<Subsumsjon>) {
            this.add(Vedtaksperiode(subsumsjoner))
        }

        //TODO: Refaktorer
        fun MutableList<Vedtaksperiode>.seEtterVedtaksperiodeID(subsumsjon: Subsumsjon, søk: SporingEnum) {
            if (this.none { it.subsumsjoner.erRelevant(subsumsjon, søk) }) this.seEtterSøknadsID(subsumsjon, SØKNAD)
        }

        fun MutableList<Vedtaksperiode>.vedtaksperiodeID(subsumsjon: Subsumsjon, søk: SporingEnum): Boolean {
          return this.none { it.subsumsjoner.erRelevant(subsumsjon, søk)}
        }

        //Sjekk om vedtaksperiode er relevant. Hvis vedtaksperiode er relevant, legg til i lista
        //Hvis vedtaksperiode ikke er relevant

        //TODO: Refaktorer
        fun MutableList<Vedtaksperiode>.seEtterSøknadsID(subsumsjon: Subsumsjon, søk: SporingEnum) {
            if (this.none { it.subsumsjoner.erRelevant(subsumsjon, søk) }) {
                this.seEtterSykmeldingsID(subsumsjon, SYKMELDING)
            }
        }

        fun MutableList<Vedtaksperiode>.hentVedtaksperiodeMedSykmeldingID(subsumsjon: Subsumsjon, søk: SporingEnum): MutableList<Vedtaksperiode> {
            val vedtaksperioder = mutableListOf<Vedtaksperiode>()
            forEach {
                if (it.subsumsjoner.erRelevant(subsumsjon, søk)) vedtaksperioder.add(it)
            }
            return vedtaksperioder
        }



        //TODO: Refaktorer
        fun MutableList<Vedtaksperiode>.seEtterSykmeldingsID(subsumsjon: Subsumsjon, søk: SporingEnum) {

            // TIL DUPLISERING
            // finn vedtaksperioden med samme sykemeldingsid som sub
            // sjekk at denne vedtaksperioden ikke inneholder andres søknadsider
            // hvis den inneholder andre søknadsider: dupliser _IKKE CASSE ENDA
            // hvis den ikke inneholder andre søknadsider: legg inn

            /*
             val subsumsjoner = this.hentAlleSubsumsjonerMedSykemeldingID(subsumsjon, SYKMELDING)
                // dersom det kun er en ved
                this.lagNyVedtaksperiode(subsumsjoner)
                // hvis ikke relevant legg inn i ny vedtaksperiode, og hent alle subsumsjoner med samme sykepengeid som seg selv og dupliser disse
                //hentAlleSubsumsjonerMedSykemeldingID: subsumsjon.sykepengeid
                // lage ny vedtaksperiode med disse
             */

            // hvis subsumsjonen har bare sykemeldingsid og ingen ting annet
            if (subsumsjon.finnSøkeParameter() == SYKMELDING){
                var fantMatch = false
                forEach {
                    fantMatch = it.subsumsjoner.erRelevant(subsumsjon, søk)
                    //todo: BREAK hvis true
                }
                if (!fantMatch) {
                    this.lagNyVedtaksperiode(mutableListOf(subsumsjon))
                }

            } else {
                if (this.none { it.subsumsjoner.erRelevant(subsumsjon, søk) } ) {
                    val alleRelevanteSubsumsjoner = this.hentVedtaksperiodeMedSykmeldingID(subsumsjon, SYKMELDING).hentAlleSubsumsjonerMedRelevantSykmeldingIDUtenSøknadID()
                    if(alleRelevanteSubsumsjoner == null){
                        this.lagNyVedtaksperiode(mutableListOf(subsumsjon))
                    }
                    else {
                        alleRelevanteSubsumsjoner.add(subsumsjon)
                        this.lagNyVedtaksperiode(alleRelevanteSubsumsjoner)
                    }
                }
            }
        }

        fun MutableList<Vedtaksperiode>.hentAlleSubsumsjonerMedRelevantSykmeldingIDUtenSøknadID(): MutableList<Subsumsjon>?{
            forEach {
                return it.subsumsjoner.finnAlleUtenSøknadId()
            }
            return null
        }



        fun MutableList<Vedtaksperiode>.finnEiere(subsumsjon: Subsumsjon) : MutableList<Vedtaksperiode> {
            val sporing = subsumsjon.finnSøkeParameter()
            val eiere = mutableListOf<Vedtaksperiode>()
            forEach {
                if(it.sjekkEierskap(subsumsjon, sporing)) {
                    eiere.add(it)
                }
            }
            return eiere
        }


        fun MutableList<Vedtaksperiode>.nyHåndter(subsumsjon: Subsumsjon) {
            /*
            TOdo mulig refaktorering:
             val sporing = subsumsjon.finnSøkeParameter()
             if(sporing) == SYKEMDLING {
                 dupliserSubsubsjon(sub)
                 return
             */

            // Punkt 1, er det noen PVPer som eier subsumsjon?
            val pvpEiere = finnEiere(subsumsjon)

            // Hvis 1 eller flere PVPer eier subsumsjonen
            if (pvpEiere.isNotEmpty()) {
                for (eier in pvpEiere) {
                    eier.subsumsjoner.add(subsumsjon)
                    // legg til subsumsjon i pvp
                }
            } else {
                // hvis nei, ny PvP
                // TODO Når vi lager en ny vedtaksperiode må vi legge til sporing-ider i:
//                  alleRelevanteSykmeldingsIDer
//                  alleRelevanteSøknadsIDer
//                  vedtaksperiodeId
            }

            this.forEach {
                // TODO punkt 2 Er det noen andre PVPer som har subsumsjoner som er relevante for meg?
                // Ja? Legg til i meg
            }

            // Todo punkt 3 Fjern subsumsjoner fra andre pvper som her er søknader jeg eier.
        }

        fun MutableList<Vedtaksperiode>.håndter(subsumsjon: Subsumsjon) {
            when (subsumsjon.finnSøkeParameter()) {
                VEDTAKSPERIODE -> {
                    this.seEtterVedtaksperiodeID(subsumsjon, VEDTAKSPERIODE)
                    // f eks her - kalle hent vedtaksperiodeider og se om noen matcher.
                    // hent alle
                }
                SØKNAD -> {
                    //this.hvisIkkeRelevantLagNyVedtaksperiode(subsumsjon, SØKNAD)
                    this.seEtterSøknadsID(subsumsjon, SØKNAD)
                    // hvis ikke relevant legg inn i ny vedtaksperiode, og hent alle subsumsjoner med samme sykepengeid som seg selv og dupliser disse
                    //hentAlleSubsumsjonerMedSykemeldingID: subsumsjon.sykepengeid

                    // f eks her - kalle hent søknadsider og se om noen matcher.
                    // flere kan matche - legg inn flere steder her også
                    // dersom den ikke finner noen søknadsideer som matcher skal det ikke lages ny vedtaksperiode, men her skal den dupliseres og legges etter
                }
                SYKMELDING -> {
                    this.seEtterSykmeldingsID(subsumsjon, SYKMELDING)
                }
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
