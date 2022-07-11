package no.nav.helse

internal class Subsumsjon(
    private val id : String,
    private val versjon : String,
    private val eventName : String,
    private val kilde : String,
    private val versjonAvKode : String,
    private val fodselsnummer : String, //TODO Fjern denne (dobbellagring)
    private val sporing : Map<String, Any>,
    private val tidsstempel : String,
    private val lovverk : String,
    private val lovverksversjon : String,
    private val paragraf : String,
    private val ledd : Int?,
    private val punktum : Int?,
    private val bokstav : String?,
    private val input : Map<String, Any>,
    private val output : Map<String, Any>,
    private val utfall : String,
){
    companion object{
        fun List<Subsumsjon>.finnAlle(paragraf: String) = this.filter { it.paragraf == paragraf }
    }
}
