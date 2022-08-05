package no.nav.helse

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime

interface PersonVisitor : VedtaksperiodeVisitor {

    fun preVisitPerson(fødselsnummer: String) {}

    fun postVisitPerson() {}

    fun preVisitVedtaksperioder() {}

    fun postVisitVedtaksperioder() {}


}


interface VedtaksperiodeVisitor : SubsumsjonVisitor, VedtakVisitor {

    fun visitVedtaksperiode(
        tilstand: String,
        skjæringstidspunkt: LocalDate?,
        orgnummer: String,
        vedtaksperiodeId: String?
    ) {
    }

    fun preVisitSubsumsjoner() {}

    fun postVisitSubsumsjoner() {}

    fun preVisitVedtakFattet() {}

    fun postVisitVedtakFattet() {}
}

interface SubsumsjonVisitor {

    fun visitSubsumsjon(
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
    }
}

interface VedtakVisitor {
    fun visitVedtakFattet(
        id: String,
        tidsstempel: LocalDateTime,
        hendelser: List<String>,
        fødselsnummer: String,
        vedtaksperiodeId: String,
        skjeringstidspunkt: LocalDate,
        fom: LocalDate,
        tom: LocalDate,
        organisasjonsnummer: String,
        utbetalingsId: String
    ) {}

    fun visitVedtaksperiodeForkastet(
        id: String,
        tidsstempel: LocalDateTime,
        fødselsnummer: String,
        vedtaksperiodeId: String,
        organisasjonsnummer: String,
    ) {}
}



