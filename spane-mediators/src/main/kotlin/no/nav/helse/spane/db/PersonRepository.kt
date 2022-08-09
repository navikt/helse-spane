package no.nav.helse.spane.db


interface PersonRepository {
    fun hentPerson(fnr: String): SerialisertPerson?

    fun lagre(json: String, fnr: String)

    fun lagreParagrafkobling(paragraf: String, ledd: Int?, bokstav: String?, punktum: Int?, fnr: String)


}
