package no.nav.helse

import no.nav.helse.SporingEnum.*
import no.nav.helse.Subsumsjon.Companion.finnAlleUtenSøknadId
import no.nav.helse.Subsumsjon.Companion.harEierskap

class Vedtaksperiode(
    private val subsumsjoner: MutableList<Subsumsjon>
    // liste med alle subsumsjoner med kun sykemelding - ikke begrenset til hver vedtaksperiode, men det totale antallet.
) {


    internal companion object {
        fun MutableList<Vedtaksperiode>.lagNyVedtaksperiode(subsumsjoner: MutableList<Subsumsjon>) {
            this.add(Vedtaksperiode(subsumsjoner))
        }

        //TODO: Refaktorer
        fun MutableList<Vedtaksperiode>.seEtterVedtaksperiodeID(subsumsjon: Subsumsjon, søk: SporingEnum) {
            if (this.none { it.subsumsjoner.harEierskap(subsumsjon, søk) }) this.seEtterSøknadsID(subsumsjon, SØKNAD)
        }

        fun MutableList<Vedtaksperiode>.vedtaksperiodeID(subsumsjon: Subsumsjon, søk: SporingEnum): Boolean {
          return this.none { it.subsumsjoner.harEierskap(subsumsjon, søk)}
        }

        //Sjekk om vedtaksperiode er relevant. Hvis vedtaksperiode er relevant, legg til i lista
        //Hvis vedtaksperiode ikke er relevant

        //TODO: Refaktorer
        fun MutableList<Vedtaksperiode>.seEtterSøknadsID(subsumsjon: Subsumsjon, søk: SporingEnum) {

            if (this.none { it.subsumsjoner.harEierskap(subsumsjon, søk) }) {
                this.seEtterSykmeldingsID(subsumsjon, SYKMELDING)
            }
            /*
            fun isValidIdentifier(s: String): Boolean {
                fun isCaractererValido(ch: Char) = ch == '_' || ch.isLetterOrDigit()

                if (s.takeWhile { !isCaractererValido(it) }.isNullOrEmpty()) return true
            }

             */
        }

        fun MutableList<Vedtaksperiode>.hentVedtaksperiodeMedSykmeldingID(subsumsjon: Subsumsjon, søk: SporingEnum): MutableList<Vedtaksperiode> {
            val vedtaksperioder = mutableListOf<Vedtaksperiode>()
            forEach {
                if (it.subsumsjoner.harEierskap(subsumsjon, søk)) vedtaksperioder.add(it)
            }
            return vedtaksperioder
        }



        //TODO: Refaktorer
        fun MutableList<Vedtaksperiode>.seEtterSykmeldingsID(subsumsjon: Subsumsjon, søk: SporingEnum) {

            // hvis subsumsjonen har bare sykemeldingsid og ingen ting annet
            if (subsumsjon.finnSøkeParameter() == SYKMELDING){
                var fantMatch = false
                forEach {
                    fantMatch = it.subsumsjoner.harEierskap(subsumsjon, søk)
                    //todo: BREAK hvis true
                }
                if (!fantMatch) {
                    this.lagNyVedtaksperiode(mutableListOf(subsumsjon))
                }

            } else {
                if (this.none { it.subsumsjoner.harEierskap(subsumsjon, søk) } ) {
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



        fun MutableList<Vedtaksperiode>.etablerEierskap(subsumsjon: Subsumsjon) : Vedtaksperiode {
            val sporing = subsumsjon.finnSøkeParameter()
            forEach {
                if(it.subsumsjoner.harEierskap(subsumsjon, sporing)) it.subsumsjoner += subsumsjon; return it
            }
            this.add(Vedtaksperiode(mutableListOf(subsumsjon)))
            return this[this.lastIndex]
        }

        fun MutableList<Vedtaksperiode>.dupliserSubsumsjon(subsumsjon: Subsumsjon) {
            forEach {
                if(it.subsumsjoner.harEierskap(subsumsjon, SYKMELDING)) it.subsumsjoner += subsumsjon
            }
        }


        fun MutableList<Vedtaksperiode>.nyHåndter(subsumsjon: Subsumsjon) {
            // returnerer dersom subsumsjonen kun har sykemeldingsparameter
            if (subsumsjon.finnSøkeParameter() == SYKMELDING){
                dupliserSubsumsjon(subsumsjon)
                return
            }
            // Punkt 1, er det noen PVPer som eier subsumsjon?
            val pvpEier = etablerEierskap(subsumsjon)






            this.forEach {
                //pvpEier.subsumsjoner.harEierskap()
                // TODO punkt 2 Er det noen andre PVPer som har subsumsjoner som er relevante for meg?
                // Ja? Legg til i meg
            }

            // Todo punkt 3 Fjern subsumsjoner fra andre pvper som her er søknader jeg eier.
        }

        fun MutableList<Vedtaksperiode>.håndter(subsumsjon: Subsumsjon) {
            // er det en PVP som eier meg? - er helt lik

            /*
            for each {
                if it.subsumsjoner.erHeltLik(subsumsjon, subsumsjon.finnSøkeParameter()) - må lage en funksjon erHeltLik som sjekker allt parameterne etter en subsumsjon som er helt lik
                - ja - legg inn og returner. kall en metode finnRelevante fra person med innsendt subsumsjon og vedtaksperiode
                - nei - lag ny vedtaksperiode og gjør samme som på ja


            }

            kall en privat metode erRelevant inne i vedtaksperiodeklassen på hver vedtaksperiode med vår nye vedtaksperiode som parameter
            inne i erRelevant inne i vedtaksperiodeklassen:
            sjekk søkeparameter på subsumsjon:
            - se etter subsumsjoner med samme vedtaksperiode og rekurser nedover med søknadsid og sykepengeid. - legg inn alle som matcher i nye vedtaksperioden


            kall metode fjernRelevante fra person, som fjerner alle subsumsjoner fra andre pvper som har samme søknadsid som meg.


             */






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
