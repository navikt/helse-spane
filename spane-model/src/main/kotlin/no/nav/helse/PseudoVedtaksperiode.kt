package no.nav.helse

import no.nav.helse.Subsumsjon.Companion.eier
import no.nav.helse.Subsumsjon.Companion.relevante
import no.nav.helse.Subsumsjon.Companion.sporingIder
import no.nav.helse.Subsumsjon.Companion.søknadsIder

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
            val pvpEiere = finnEiere(subsumsjon)
            pvpEiere.forEach { it.leggTil(subsumsjon) }

            pvpEiere.forEach {
                val relevante = this.relevanteSubsumsjoner(it)
                // it er vedtaksperioden
                // er det noen subs i denne vedtaksperioden som skal fjernes fra it - aka har samme søknads id som jeg eier
                //it.søknadsIder()
                // if it.søknadsIDer() in pvpEiere.søknadsIder(): fjern fra it
                // it.skalfjernes(
                it.leggTil(*relevante.toTypedArray())

            }

            // TODO punkt 3 Fjern subsumsjoner fra andre pvper som her er søknader jeg eier
            // TODO punkt 4 hvis pvper inneholder meg, fjern meg
        }
    }

    private fun leggTil(vararg subsumsjoner: Subsumsjon) {
        subsumsjoner.forEach {
            if (it !in this.subsumsjoner) this.subsumsjoner.add(it)
        }
    }

    private fun alleIder() = subsumsjoner.sporingIder()

    private fun søknadsIder() = subsumsjoner.søknadsIder()


    fun antallSubsumsjoner(): Int {
        return subsumsjoner.size
    }

    fun accept(visitor: VedtaksperiodeVisitor) {
        visitor.preVisitSubsumsjoner()
        subsumsjoner.forEach { it.accept(visitor) }
        visitor.postVisitSubsumsjoner()
    }
}
