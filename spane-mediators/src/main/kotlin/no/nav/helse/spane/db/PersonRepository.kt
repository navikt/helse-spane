package no.nav.helse.spane.db


interface PersonRepository {
    fun hentPerson(fødselsnummer: String): SerialisertPerson?

    fun lagre(json: String, fnr: String)
}
