package no.nav.helse.spane.db

import no.nav.helse.Person
import no.nav.helse.lagSubsumsjonFraJson
import no.nav.helse.objectMapper

class SerialisertPerson(val json: String) {
    internal fun deserialiser(): Person {
        val personJson = objectMapper.readTree(json)
        val person = Person(personJson["fnr"].asText())

        personJson["vedtaksperioder"].flatMap {
            it["subsumsjoner"]
        }.forEach {
            val subsumsjon = lagSubsumsjonFraJson(it)
            person.håndter(subsumsjon)
        }
        return person
    }


}