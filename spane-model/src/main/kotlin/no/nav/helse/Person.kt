package no.nav.helse

class Person {
    private val subsumsjoner = mutableListOf<Subsumsjon>()

    val antallVedtaksperioder: Int
        get() = subsumsjoner.size

    internal fun håndter(subsumsjon: Subsumsjon){
        subsumsjoner.add(subsumsjon)
    }
}
