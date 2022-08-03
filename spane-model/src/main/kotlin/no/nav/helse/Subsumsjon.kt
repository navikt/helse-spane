package no.nav.helse

import java.time.ZonedDateTime

class Subsumsjon(
    private val id: String,
    private val versjon: String,
    private val eventName: String = "subsumsjon",
    private val kilde: String,
    private val versjonAvKode: String,
    private val fødselsnummer: String,
    private val sporing: Map<String, List<String>>,
    private val tidsstempel: ZonedDateTime,
    private val lovverk: String,
    private val lovverksversjon: String,
    private val paragraf: String,
    private val ledd: Int? = null,
    private val punktum: Int? = null,
    private val bokstav: String? = null,
    private val input: Map<String, Any>,
    private val output: Map<String, Any>,
    private val utfall: String,
) {
    internal companion object {
        internal fun List<Subsumsjon>.finnAlle(paragraf: String) = this.filter { it.paragraf == paragraf }

        internal fun MutableList<Subsumsjon>.eier(subsumsjon: Subsumsjon) = subsumsjon.eiesAv(this.sporingIder())

        internal fun List<Subsumsjon>.relevante(pvpIder: List<String>) = filter { it.erRelevant(pvpIder) }

        internal fun MutableList<Subsumsjon>.sporingIder() = this.flatMap { it.sporing.values.flatten() }

        internal fun MutableList<Subsumsjon>.subsumsjonerMedSøknadsIder() = this.filter { !it.sporing["soknad"].isNullOrEmpty() }

        internal fun MutableList<Subsumsjon>.finnSkjæringstidspunkt(): String {
            this.forEach {
                val skjæringstidspunkt = it.input["skjæringstidspunkt"] as String?
                if (skjæringstidspunkt != null) {
                    return skjæringstidspunkt
                }
            }
            return "ukjent"
        }
        internal fun MutableList<Subsumsjon>.finnOrgnummer(): String {
            this.forEach {
                val organisasjonsnummer = it.sporing["organisasjonsnummer"]?.get(0)
                if (organisasjonsnummer != null) return organisasjonsnummer
            }
            return "ukjent"
        }

        internal fun List<Subsumsjon>.finnVedtaksperiodeId() =
            mapNotNull { it.sporing["vedtaksperiode"] }
                .flatten()
                .distinct()
                .apply { require(this.size <= 1) }
                .toList().getOrNull(0)
    }


    private fun erRelevant(pvpIder: List<String>): Boolean {
        return if (!sporing["vedtaksperiode"].isNullOrEmpty()) {
            sporing["vedtaksperiode"]!!.first() in pvpIder
        } else if (!sporing["soknad"].isNullOrEmpty()) {
            pvpIder.containsAll(sporing["soknad"]!!) && pvpIder.containsAll(sporing["sykmelding"]!!)
        } else if (!sporing["sykmelding"].isNullOrEmpty()) {
            pvpIder.containsAll(sporing["sykmelding"]!!)
        } else {
            false
        }
    }

    fun sammeVedtaksperiode(pvpIder: List<String>): Boolean {
        return (this.sporing["vedtaksperiode"].takeUnless { it.isNullOrEmpty() }?.first() in pvpIder)
    }

    fun eiesAv(pvpIder: List<String>): Boolean {
        if (sammeVedtaksperiode(pvpIder)) return true
        return this.sporing.values.flatten().all { it in pvpIder }
    }

    fun accept(visitor: VedtaksperiodeVisitor) {
        visitor.visitSubsumsjon(
            id,
            versjon,
            eventName,
            kilde,
            versjonAvKode,
            fødselsnummer,
            sporing,
            tidsstempel,
            lovverk,
            lovverksversjon,
            paragraf,
            ledd,
            punktum,
            bokstav,
            input,
            output,
            utfall
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Subsumsjon

        if (id != other.id) return false
        if (versjon != other.versjon) return false
        if (eventName != other.eventName) return false
        if (kilde != other.kilde) return false
        if (versjonAvKode != other.versjonAvKode) return false
        if (fødselsnummer != other.fødselsnummer) return false
        if (sporing != other.sporing) return false
        if (tidsstempel != other.tidsstempel) return false
        if (lovverk != other.lovverk) return false
        if (lovverksversjon != other.lovverksversjon) return false
        if (paragraf != other.paragraf) return false
        if (ledd != other.ledd) return false
        if (punktum != other.punktum) return false
        if (bokstav != other.bokstav) return false
        if (input != other.input) return false
        if (output != other.output) return false
        if (utfall != other.utfall) return false

        return true
    }

    override fun hashCode(): Int {
        // TODO: Vil ikke denne bare overskrive resultatet for hver linje?
        // Holder det å bare bruke den siste linjen?
        var result = id.hashCode()
        result = 31 * result + versjon.hashCode()
        result = 31 * result + eventName.hashCode()
        result = 31 * result + kilde.hashCode()
        result = 31 * result + versjonAvKode.hashCode()
        result = 31 * result + fødselsnummer.hashCode()
        result = 31 * result + sporing.hashCode()
        result = 31 * result + tidsstempel.hashCode()
        result = 31 * result + lovverk.hashCode()
        result = 31 * result + lovverksversjon.hashCode()
        result = 31 * result + paragraf.hashCode()
        result = 31 * result + (ledd ?: 0)
        result = 31 * result + (punktum ?: 0)
        result = 31 * result + (bokstav?.hashCode() ?: 0)
        result = 31 * result + input.hashCode()
        result = 31 * result + output.hashCode()
        result = 31 * result + utfall.hashCode()
        return result
    }

    override fun toString(): String {
        return "Subsumsjon(sporing=$sporing, id=${id.subSequence(0, 5)}...)"
    }
}
