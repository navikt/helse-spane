package no.nav.helse.spane

import no.nav.helse.PersonVisitor
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime

typealias APIVedtaksperiode = Map<String, Any?>

class APIVisitor : PersonVisitor {
    val personMap = mutableMapOf<String, Any>("vedtaksperioder" to mutableListOf<APIVedtaksperiode>())
    override fun preVisitPerson(fødselsnummer: String) {
        personMap["fnr"] = fødselsnummer
    }


    override fun visitVedtaksperiode(
        tilstand: String,
        skjæringstidspunkt: LocalDate?,
        orgnummer: String,
        vedtaksperiodeId: String?,
        ikkeSikkertSkjæringstidspunkt: Boolean,
        fom: LocalDate?,
        tom: LocalDate?
    ) {
        (personMap["vedtaksperioder"] as MutableList<APIVedtaksperiode>)
            .add(
                mutableMapOf(
                    "subsumsjoner" to mutableListOf<Any>(),
                    "vedtakStatus" to mutableListOf<Any>(),
                    "orgnummer" to orgnummer,
                    "skjæringstidspunkt" to skjæringstidspunkt,
                    "vedtaksperiodeId" to vedtaksperiodeId,
                    "tilstand" to tilstand,
                    "ikkeSikkertSkjæringstidspunkt" to ikkeSikkertSkjæringstidspunkt,
                    "fom" to fom,
                    "tom" to tom
                )
            )
    }

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
        ((personMap["vedtaksperioder"] as MutableList<APIVedtaksperiode>).last()["vedtakStatus"] as MutableList<Any>).add(
            mapOf(
                "id" to id,
                "tidsstempel" to tidsstempel,
                "hendelser" to hendelser,
                "fodselsnummer" to fødselsnummer,
                "vedtaksperiodeId" to vedtaksperiodeId,
                "skjeringstidspunkt" to skjeringstidspunkt,
                "fom" to fom,
                "tom" to tom,
                "organisasjonsnummer" to organisasjonsnummer,
                "utbetalingId" to utbetalingsId,
                "eventName" to eventName
            )
        )
    }
    override fun visitVedtaksperiodeForkastet(
        id: String,
        tidsstempel: LocalDateTime,
        fødselsnummer: String,
        vedtaksperiodeId: String,
        organisasjonsnummer: String,
        eventName: String
    ) {
        ((personMap["vedtaksperioder"] as MutableList<APIVedtaksperiode>).last()["vedtakStatus"] as MutableList<Any>).add(
            mapOf(
                "id" to id,
                "tidsstempel" to tidsstempel,
                "fodselsnummer" to fødselsnummer,
                "vedtaksperiodeId" to vedtaksperiodeId,
                "organisasjonsnummer" to organisasjonsnummer,
                "eventName" to eventName
            )
        )
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
                "fodselsnummer" to fødselsnummer,
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