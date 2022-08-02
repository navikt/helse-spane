package no.nav.helse.spane

import no.nav.helse.PersonVisitor
import java.time.ZonedDateTime

typealias APIVedtaksperiode = Map<String, Any>

class APIVisitor : PersonVisitor {
    val personMap = mutableMapOf<String, Any>("vedtaksperioder" to mutableListOf<APIVedtaksperiode>())
    override fun preVisitPerson(fødselsnummer: String) {
        personMap["fnr"] = fødselsnummer
    }

    override fun preVisitSubsumsjoner(skjæringstidspunkt: String, orgnummer: String) {
        (personMap["vedtaksperioder"] as MutableList<APIVedtaksperiode>)
            .add(mutableMapOf("subsumsjoner" to mutableListOf<Any>(), "orgnummer" to orgnummer, "skjæringstidspunkt" to skjæringstidspunkt))

    }

    override fun visitSubsumsjon(
        id: String,
        versjon: String,
        eventName: String,
        kilde: String,
        versjonAvKode: String,
        fødselsnummer: String,
        sporing: Map<String, List<String>>,
        tidsstempel: ZonedDateTime,
        lovverk: String,
        lovverksversjon: String,
        paragraf: String,
        ledd: Int?,
        punktum: Int?,
        bokstav: String?,
        input: Map<String, Any>,
        output: Map<String, Any>,
        utfall: String
    ) {
        ((personMap["vedtaksperioder"] as MutableList<APIVedtaksperiode>).last()["subsumsjoner"] as MutableList<Any>).add(
            mapOf(
                "id" to id,
                "versjon" to versjon,
                "eventName" to eventName,
                "kilde" to kilde,
                "versjonAvKode" to versjonAvKode,
                "fødselsnummer" to fødselsnummer,
                "sporing" to sporing,
                "tidsstempel" to tidsstempel,
                "lovverk" to lovverk,
                "lovverksversjon" to lovverksversjon,
                "paragraf" to paragraf,
                "ledd" to ledd,
                "punktum" to punktum,
                "bokstav" to bokstav,
                "input" to input,
                "output" to output,
                "utfall" to utfall
            )
        )
    }

}