package no.nav.helse

import no.nav.helse.Subsumsjon.Subsumsjoner
import java.time.LocalDate
import java.time.LocalDateTime

internal class Pseudovedtaksperiode(
    private val subsumsjoner: Subsumsjoner,
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
                this.add(Pseudovedtaksperiode(Subsumsjoner(mutableSetOf())))
                listOf(this[this.lastIndex])
            }
        }

        private fun List<Pseudovedtaksperiode>.relevanteSubsumsjoner(eier: Pseudovedtaksperiode): Set<Subsumsjon> =
            this.filterNot { it == eier }.map { it.subsumsjoner.relevante(eier.alleIder()) }.flatten().toSet()

        private fun List<Pseudovedtaksperiode>.fjernSubsumsjoner(subsumsjoner: List<Subsumsjon>) {
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
                    if (pvpEier.subsumsjoner.harAlle(it.subsumsjoner)) this.remove(it)
                }
            }
        }

        fun List<Pseudovedtaksperiode>.håndter(vedtakFattet: VedtakFattet) =
            none { it.håndter(vedtakFattet) }

        fun List<Pseudovedtaksperiode>.håndter(vedtakFattet: VedtaksperiodeForkastet) =
            none { it.håndter(vedtakFattet) }
    }

    private fun fjernSubsumsjoner(subsumsjoner: List<Subsumsjon>) {
        this.subsumsjoner.fjernAlle(subsumsjoner)
    }

    private fun leggTil(vararg subsumsjoner: Subsumsjon) {
        this.subsumsjoner.leggTil(*subsumsjoner)
    }

    private fun alleIder() = subsumsjoner.sporingIder()

    private fun subsumsjonerMedSøknadsider() = subsumsjoner.subsumsjonerMedSøknadsIder()

    private fun skjæringstidspunkt(): Pair<List<LocalDate?>, Boolean> {
        var result: LocalDate? = null
        var vFom: LocalDate? = null
        var vTom: LocalDate? = null
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
                vFom = fom
                vTom = tom
            }
        })

        if (result == null) {
            val usikkertSkjæringstidspunkt = subsumsjoner.finnSkjæringstidspunkt()
            return if (usikkertSkjæringstidspunkt != null)
                Pair(listOf(LocalDate.parse(usikkertSkjæringstidspunkt), vFom, vTom), true)
            else
                Pair(listOf(result, vFom, vTom), false)
        }
        return Pair(listOf(result, vFom, vTom), false)

    }

    fun antallSubsumsjoner(): Int {
        return subsumsjoner.antall
    }

    fun accept(visitor: VedtaksperiodeVisitor) {
        val (skjæringstidspunkter, flagg) = skjæringstidspunkt()
        val (skjæringstidspunkt, fom, tom) = skjæringstidspunkter

        visitor.visitVedtaksperiode(
            tilstand.toString(),
            skjæringstidspunkt,
            subsumsjoner.finnOrgnummer(),
            subsumsjoner.finnVedtaksperiodeId(),
            flagg,
            fom,
            tom
        )
        subsumsjoner.visit(visitor)
        tilstandsmelding.forEach { it.accept(visitor) }
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

