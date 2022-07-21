package no.nav.helse

import no.nav.helse.SporingNoe.*
import java.time.ZonedDateTime

class Subsumsjon(
    private val id: String,
    private val versjon: String,
    private val eventName: String = "subsumsjon",
    private val kilde: String,
    private val versjonAvKode: String,
    private val fødselsnummer: String,
    private val sporing: Map<String, Any>,
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
        fun List<Subsumsjon>.finnAlle(paragraf: String) = this.filter { it.paragraf == paragraf }

        fun List<Subsumsjon>.sorterPåTid() = this.sortedBy { it.tidsstempel }

        fun MutableList<Subsumsjon>.erRelevant(subsumsjon: Subsumsjon, søk : SporingNoe): Boolean {
            this.forEach {
                // for hver subsumsjon, sjekk vedtaksid, så søknadsid, så sykemeldingid


                if(it.sporing[søk.navn] == subsumsjon.sporing[søk.navn]) {
                    this += subsumsjon
                    return true
                }

                // sjekk i de resterende
            }
            return false
        }
    }

    fun finnSøkeParameter(): SporingNoe? {
        if (sporing["vedtaksperiode"] != null)  {
            return VEDTAKSPERIODE
        }
        if (sporing["soknad"] != null) {
            return SØKNAD
        }
        if (sporing["sykmelding"] != null) {
            return SYKMELDING
        }
        return null
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

        // TODO: Er det noe poeng i å refaktorere denne koden?
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

}
