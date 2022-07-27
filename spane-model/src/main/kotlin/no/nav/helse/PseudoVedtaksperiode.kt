package no.nav.helse

import no.nav.helse.Subsumsjon.Companion.eier
import no.nav.helse.Subsumsjon.Companion.relevante
import no.nav.helse.Subsumsjon.Companion.sporingIder

class PseudoVedtaksperiode(
    private val subsumsjoner: MutableList<Subsumsjon>
) {
    internal companion object {

        fun MutableList<PseudoVedtaksperiode>.finnEiere(subsumsjon: Subsumsjon): List<PseudoVedtaksperiode> {
            return filter {
                it.subsumsjoner.eier(subsumsjon)
            }.ifEmpty {
                this.add(PseudoVedtaksperiode(mutableListOf()))
                listOf(this[this.lastIndex])
            }
        }

        fun MutableList<PseudoVedtaksperiode>.relevanteSubsumsjoner(eier: PseudoVedtaksperiode) =
            this.filterNot { it == eier }.map { it.subsumsjoner.relevante(eier.alleIder()) }.flatten()


        fun MutableList<PseudoVedtaksperiode>.håndter(subsumsjon: Subsumsjon) {

            // Punkt 1, er det noen PVPer som eier subsumsjon?
            val pvpEiere = finnEiere(subsumsjon)
            pvpEiere.forEach { it.leggTil(subsumsjon) }

            // Hvis flere pvpeiere


            pvpEiere.forEach {
                // TODO punkt 2 Er det noen andre PVPer som har subsumsjoner som er relevante for meg?

                val relevante = this.relevanteSubsumsjoner(it)

                it.leggTil(*relevante.toTypedArray())
                // Ja?             }Legg til i meg

                // Todo punkt 3 Fjern subsumsjoner fra andre pvper som her er søknader jeg eier.
            }

        }
    }

    private fun leggTil(vararg subsumsjoner: Subsumsjon) {
        subsumsjoner.forEach {
            if (it !in this.subsumsjoner) this.subsumsjoner.add(it)
        }
    }

    private fun alleIder() = subsumsjoner.sporingIder()


    fun antallSubsumsjoner(): Int {
        return subsumsjoner.size
    }

    fun accept(visitor: VedtaksperiodeVisitor) {
        visitor.preVisitSubsumsjoner()
        subsumsjoner.forEach { it.accept(visitor) }
        visitor.postVisitSubsumsjoner()
    }
}
