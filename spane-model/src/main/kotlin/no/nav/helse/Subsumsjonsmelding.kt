package no.nav.helse

data class Subsumsjonsmelding(
    private val id : String,
    private val versjon : String,
    private val eventName : String,
    private val kilde : String,
    private val versjonAvKode : String,
    private val fodselsnummer : String, //TODO Fjern denne (dobbellagring)
    private val sporing : Sporing,
    private val tidsstempel : String,
    private val lovverk : String,
    private val lovverksversjon : String,
    private val paragraf : String,
    private val ledd : Int?,
    private val punktum : Int?,
    private val bokstav : String?,
    private val input : Faktum,
    private val output : UtPutt,
    private val utfall : String,
)
