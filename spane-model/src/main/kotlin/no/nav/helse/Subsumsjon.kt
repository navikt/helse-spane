package no.nav.helse

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.ZonedDateTime

var logger: Logger = LoggerFactory.getLogger("Spane")

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
    internal class Subsumsjoner(subsumsjoner: Collection<Subsumsjon>) {
        private val subsumsjoner = subsumsjoner.toMutableSet()
        val antall: Int
            get() = subsumsjoner.size

        fun finnAlle(paragraf: String) = subsumsjoner.filter { it.paragraf == paragraf }

        fun eier(subsumsjon: Subsumsjon) = subsumsjon.eiesAv(sporingIder())

        private val relevanteSubsumsjoner = mutableMapOf<Set<String>, Set<Subsumsjon>>()

        fun relevante(pvpIder: Set<String>): Collection<Subsumsjon> {
            return relevanteSubsumsjoner.computeIfAbsent(pvpIder) { spvpIder ->
                subsumsjoner.filter { it.erRelevant(spvpIder) }.toSet()
            }
        }

        fun sporingIder() = subsumsjoner.flatMapTo(HashSet()) { it.sporing.values.flatten() }

        fun subsumsjonerMedSøknadsIder() =
            subsumsjoner.filterTo(HashSet()) { !it.sporing["soknad"].isNullOrEmpty() }

        fun finnOrgnummer(): String {
            subsumsjoner.forEach {
                val organisasjonsnummer = it.sporing["organisasjonsnummer"]?.first()
                if (organisasjonsnummer != null) return organisasjonsnummer
            }
            return "ukjent"
        }

        fun finnVedtaksperiodeId() =
            subsumsjoner.mapNotNull { it.sporing["vedtaksperiode"] }
                .flatten()
                .distinct()
                .filterNot { it == "null" }
                .apply { require(this.size <= 1) }
                .toList().getOrNull(0)

        fun finnSkjæringstidspunkt(): String? {
            subsumsjoner.forEach {
                val skjæringstidspunkt = it.input["skjæringstidspunkt"] as String?
                if (skjæringstidspunkt != null) {
                    return skjæringstidspunkt
                }
            }
            return null
        }

        fun harAlle(subsumsjoner: Subsumsjoner): Boolean {
            return this.subsumsjoner.containsAll(subsumsjoner.subsumsjoner)
        }

        fun fjernAlle(subsumsjoner: Set<Subsumsjon>) {
            if (subsumsjoner.isEmpty()) return
            if (this.subsumsjoner.intersect(subsumsjoner).isEmpty()) {
                return
            }
            logger.info("Skal fjerne subsumsjoner")
            val antallSubsumsjonerFør = this.subsumsjoner.size
            this.subsumsjoner.removeAll(subsumsjoner)
            val antallSubsumsjonerEtterpå = this.subsumsjoner.size
            val antallFjernet = antallSubsumsjonerFør - antallSubsumsjonerEtterpå
            logger.info("Fjernet $antallFjernet subsumsjoner")
            if (antallFjernet != 0) relevanteSubsumsjoner.clear()
        }

        fun leggTil(vararg subsumsjoner: Subsumsjon) {
            subsumsjoner.forEach {
                if (it !in this.subsumsjoner) this.subsumsjoner.add(it).also {
                    relevanteSubsumsjoner.clear()
                }
            }
        }

        fun visit(visitor: VedtaksperiodeVisitor) {
            subsumsjoner.forEach { it.accept(visitor) }
        }
    }

    private fun erRelevant(pvpIder: Set<String>): Boolean {
        return if (!sporing["vedtaksperiode"].isNullOrEmpty()) {
            sporing["vedtaksperiode"]!!.first() in pvpIder
        } else if (!sporing["soknad"].isNullOrEmpty()) {
            (pvpIder.containsAll(sporing["soknad"]!!) && pvpIder.containsAll(sporing["sykmelding"]!!))
                    || (pvpIder.containsAll(sporing["soknad"]!!) && pvpIder.containsAll(sporing["sykmeldingsid"]!!))
        } else if (!sporing["sykmelding"].isNullOrEmpty()) {
            pvpIder.containsAll(sporing["sykmelding"]!!)
        } else if (!sporing["sykmeldingsid"].isNullOrEmpty()) {
            pvpIder.containsAll(sporing["sykmeldingsid"]!!)
        }
        else {
            false
        }
    }

    private fun sammeVedtaksperiode(pvpIder: Set<String>): Boolean {
        return (this.sporing["vedtaksperiode"].takeUnless { it.isNullOrEmpty() }?.first() in pvpIder)
    }

    fun eiesAv(pvpIder: Set<String>): Boolean {
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
