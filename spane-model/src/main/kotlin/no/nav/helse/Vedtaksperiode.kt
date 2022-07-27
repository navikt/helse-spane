package no.nav.helse

import no.nav.helse.SporingEnum.*
import no.nav.helse.Subsumsjon.Companion.harEierskap

class Vedtaksperiode(
    private val subsumsjoner: MutableList<Subsumsjon>
) {
    internal companion object {

        fun MutableList<Vedtaksperiode>.etablerEierskap(subsumsjon: Subsumsjon): Vedtaksperiode {
            val sporing = subsumsjon.finnSøkeParameter()
            forEach {
                if (it.subsumsjoner.harEierskap(subsumsjon, sporing)) it.subsumsjoner += subsumsjon; return it
            }
            this.add(Vedtaksperiode(mutableListOf(subsumsjon)))
            return this[this.lastIndex]
        }

        fun MutableList<Vedtaksperiode>.dupliserSubsumsjon(subsumsjon: Subsumsjon) {
            forEach {
                if (it.subsumsjoner.harEierskap(subsumsjon, SYKMELDING)) it.subsumsjoner += subsumsjon
            }
        }


        fun MutableList<Vedtaksperiode>.nyHåndter(subsumsjon: Subsumsjon) {
            // returnerer dersom subsumsjonen kun har sykemeldingsparameter
            if (subsumsjon.finnSøkeParameter() == SYKMELDING) {
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
