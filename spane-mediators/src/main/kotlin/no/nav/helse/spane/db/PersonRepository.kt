package no.nav.helse.spane.db


internal interface PersonRepository {
    fun hentPerson(fødselsnummer: String): SerialisertPerson?
}
