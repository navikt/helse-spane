package no.nav.helse

import java.time.ZonedDateTime

interface PersonVisitor : VedtaksperiodeVisitor {

    fun preVisitPerson(fødselsnummer: String){}

    fun postVisitPerson(){}

    fun preVisitVedtaksperioder(){}

    fun postVisitVedtaksperioder(){}


}


interface VedtaksperiodeVisitor : SubsumsjonVisitor {

    fun preVisitSubsumsjoner(skjæringstidspunkt: String){}

    fun postVisitSubsumsjoner(){}


}

interface SubsumsjonVisitor {

    fun visitSubsumsjon(
        id : String,
        versjon : String,
        eventName : String,
        kilde : String,
        versjonAvKode : String,
        fødselsnummer : String,
        sporing : Map<String, List<String>>,
        tidsstempel : ZonedDateTime,
        lovverk : String,
        lovverksversjon : String,
        paragraf : String,
        ledd : Int?,
        punktum : Int?,
        bokstav : String?,
        input : Map<String, Any>,
        output : Map<String, Any>,
        utfall : String
    ){}

}


