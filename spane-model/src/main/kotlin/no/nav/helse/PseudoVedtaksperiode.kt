package no.nav.helse

import no.nav.helse.Subsumsjon.Companion.eier
import no.nav.helse.Subsumsjon.Companion.finnOrgnummer
import no.nav.helse.Subsumsjon.Companion.finnSkjæringstidspunkt
import no.nav.helse.Subsumsjon.Companion.finnVedtaksperiodeId
import no.nav.helse.Subsumsjon.Companion.relevante
import no.nav.helse.Subsumsjon.Companion.sporingIder
import no.nav.helse.Subsumsjon.Companion.subsumsjonerMedSøknadsIder

internal class PseudoVedtaksperiode(
    private val subsumsjoner: MutableList<Subsumsjon>,
    private val vedtak: MutableList<VedtakFattet> = mutableListOf(),
    private var tilstand: Tilstand = Tilstand.UAVKLART
) {
    enum class Tilstand() {
        UAVKLART,
        VEDTAK_FATTET,
        TIL_INFOTRYGD
    }

    internal companion object {
        fun MutableList<PseudoVedtaksperiode>.finnEiere(subsumsjon: Subsumsjon): List<PseudoVedtaksperiode> {
            return filter {
                it.subsumsjoner.eier(subsumsjon)
            }.ifEmpty {
                this.add(PseudoVedtaksperiode(mutableListOf()))
                listOf(this[this.lastIndex])
            }
        }

        fun List<PseudoVedtaksperiode>.relevanteSubsumsjoner(eier: PseudoVedtaksperiode) =
            this.filterNot { it == eier }.map { it.subsumsjoner.relevante(eier.alleIder()) }.flatten()

        fun List<PseudoVedtaksperiode>.fjernSubsumsjoner(subsumsjoner: List<Subsumsjon>) {
            forEach {
                it.fjernSubsumsjoner(subsumsjoner)
            }
        }



        fun MutableList<PseudoVedtaksperiode>.håndter(subsumsjon: Subsumsjon) {

            val pvpEiere = finnEiere(subsumsjon)
            pvpEiere.forEach { it.leggTil(subsumsjon) }

            pvpEiere.forEach {
                val relevante = this.relevanteSubsumsjoner(it)
                it.leggTil(*relevante.toTypedArray())
            }

            pvpEiere.forEach { eier ->
                val subsumsjonerMedSøknadid = eier.subsumsjonerMedSøknadsider()
                this.filter { it != eier }.fjernSubsumsjoner(subsumsjonerMedSøknadid)
            }

            this.filterNot { it in pvpEiere }.forEach {
                pvpEiere.forEach{pvpEier ->
                    if (pvpEier.subsumsjoner.containsAll(it.subsumsjoner)) this.remove(it)
                }
            }
        }
    }

    private fun fjernSubsumsjoner(subsumsjoner: List<Subsumsjon>) {
        this.subsumsjoner.removeAll(subsumsjoner)
    }

    private fun leggTil(vararg subsumsjoner: Subsumsjon) {
        subsumsjoner.forEach {
            if (it !in this.subsumsjoner) this.subsumsjoner.add(it)
        }
    }

    private fun alleIder() = subsumsjoner.sporingIder()

    private fun subsumsjonerMedSøknadsider() = subsumsjoner.subsumsjonerMedSøknadsIder()

    fun antallSubsumsjoner(): Int {
        return subsumsjoner.size
    }

    fun accept(visitor: VedtaksperiodeVisitor) {
        visitor.visitVedtaksperiode(tilstand.toString(), subsumsjoner.finnSkjæringstidspunkt(), subsumsjoner.finnOrgnummer(), subsumsjoner.finnVedtaksperiodeId())
        visitor.preVisitSubsumsjoner(this.subsumsjoner.finnSkjæringstidspunkt(), this.subsumsjoner.finnOrgnummer())
        subsumsjoner.forEach { it.accept(visitor) }
        visitor.postVisitSubsumsjoner()
    }

    fun håndter(vedtakFattet: VedtakFattet) {
        if (vedtakFattet.hørerTil(subsumsjoner.finnVedtaksperiodeId())) {
            vedtak += vedtakFattet
            tilstand = Tilstand.VEDTAK_FATTET
        }
    }
}
