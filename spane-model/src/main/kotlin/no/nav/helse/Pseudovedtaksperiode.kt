package no.nav.helse

import no.nav.helse.Subsumsjon.Companion.eier
import no.nav.helse.Subsumsjon.Companion.finnOrgnummer
import no.nav.helse.Subsumsjon.Companion.finnSkjæringstidspunkt
import no.nav.helse.Subsumsjon.Companion.finnVedtaksperiodeId
import no.nav.helse.Subsumsjon.Companion.relevante
import no.nav.helse.Subsumsjon.Companion.sporingIder
import no.nav.helse.Subsumsjon.Companion.subsumsjonerMedSøknadsIder
import java.time.LocalDate
import java.time.LocalDateTime

internal class Pseudovedtaksperiode(
    private val subsumsjoner: MutableList<Subsumsjon>,
    private val tilstandsmelding: MutableList<TilstandVedtaksperiode> = mutableListOf(),
    private var tilstand: Tilstand = Tilstand.UAVKLART
) {
    enum class Tilstand() {
        UAVKLART,
        VEDTAK_FATTET,
        TIL_INFOTRYGD
    }

    internal companion object {
        fun MutableList<Pseudovedtaksperiode>.finnEiere(subsumsjon: Subsumsjon): List<Pseudovedtaksperiode> {
            return filter {
                it.subsumsjoner.eier(subsumsjon)
            }.ifEmpty {
                this.add(Pseudovedtaksperiode(mutableListOf()))
                listOf(this[this.lastIndex])
            }
        }

        fun List<Pseudovedtaksperiode>.relevanteSubsumsjoner(eier: Pseudovedtaksperiode) =
            this.filterNot { it == eier }.map { it.subsumsjoner.relevante(eier.alleIder()) }.flatten()

        fun List<Pseudovedtaksperiode>.fjernSubsumsjoner(subsumsjoner: List<Subsumsjon>) {
            forEach {
                it.fjernSubsumsjoner(subsumsjoner)
            }
        }


        fun MutableList<Pseudovedtaksperiode>.håndter(subsumsjon: Subsumsjon) {

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
                pvpEiere.forEach { pvpEier ->
                    if (pvpEier.subsumsjoner.containsAll(it.subsumsjoner)) this.remove(it)
                }
            }
        }

        fun List<Pseudovedtaksperiode>.håndter(vedtakFattet: VedtakFattet) =
            none { it.håndter(vedtakFattet) }

        fun List<Pseudovedtaksperiode>.håndter(vedtakFattet: VedtaksperiodeForkastet) =
            none { it.håndter(vedtakFattet) }
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

    private fun skjæringstidspunkt(): Pair<LocalDate?, Boolean> {
        var result: LocalDate? = null
        tilstandsmelding.lastOrNull()?.accept(object : VedtakVisitor {
            override fun visitVedtakFattet(
                id: String,
                tidsstempel: LocalDateTime,
                hendelser: List<String>,
                fødselsnummer: String,
                vedtaksperiodeId: String,
                skjeringstidspunkt: LocalDate,
                fom: LocalDate,
                tom: LocalDate,
                organisasjonsnummer: String,
                utbetalingsId: String,
                eventName: String
            ) {
                result = skjeringstidspunkt
            }
        })
        if (result == null) {
            val usikkertSkjæringstidspunkt = subsumsjoner.finnSkjæringstidspunkt()
            return if (usikkertSkjæringstidspunkt != null)
                Pair(LocalDate.parse(usikkertSkjæringstidspunkt), true)
            else
                Pair(result, false)
        }
        return Pair(result, false)
    }


    fun antallSubsumsjoner(): Int {
        return subsumsjoner.size
    }

    fun accept(visitor: VedtaksperiodeVisitor) {
        val (skjæringstidspunkt, flagg) = skjæringstidspunkt()

        visitor.visitVedtaksperiode(
            tilstand.toString(),
            skjæringstidspunkt,
            subsumsjoner.finnOrgnummer(),
            subsumsjoner.finnVedtaksperiodeId(),
            flagg
        )
        visitor.preVisitSubsumsjoner()
        subsumsjoner.forEach { it.accept(visitor) }
        visitor.postVisitSubsumsjoner()
        visitor.preVisitVedtak()
        tilstandsmelding.forEach { it.accept(visitor) }
        visitor.postVisitVedtak()

    }

    fun håndter(vedtakFattet: VedtakFattet): Boolean {
        if (vedtakFattet.hørerTil(subsumsjoner.finnVedtaksperiodeId())) {
            tilstandsmelding += vedtakFattet
            tilstand = Tilstand.VEDTAK_FATTET
            return true
        }
        return false
    }

    fun håndter(vedtaksperiodeForkastet: VedtaksperiodeForkastet): Boolean {
        if (vedtaksperiodeForkastet.hørerTil(subsumsjoner.finnVedtaksperiodeId())) {
            tilstandsmelding += vedtaksperiodeForkastet
            tilstand = Tilstand.TIL_INFOTRYGD
            return true
        }
        return false
    }
}
