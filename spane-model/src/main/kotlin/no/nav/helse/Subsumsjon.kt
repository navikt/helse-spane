package no.nav.helse

internal class Subsumsjon(
    private val id : String,
    private val versjon : String,
    private val eventName : String = "subsumsjon",
    private val kilde : String,
    private val versjonAvKode : String,
    private val fødselsnummer : String,
    private val sporing : Map<String, Any>,
    private val tidsstempel : String,
    private val lovverk : String,
    private val lovverksversjon : String,
    private val paragraf : String,
    private val ledd : Int? = null,
    private val punktum : Int? = null,
    private val bokstav : String? = null,
    private val input : Map<String, Any>,
    private val output : Map<String, Any>,
    private val utfall : String,
){
    companion object{
        fun List<Subsumsjon>.finnAlle(paragraf: String) = this.filter { it.paragraf == paragraf }
    }
}
