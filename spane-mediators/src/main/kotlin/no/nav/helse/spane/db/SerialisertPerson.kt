package no.nav.helse.spane.db

import no.nav.helse.Person
import no.nav.helse.spane.lagSubsumsjonFraJson
import no.nav.helse.spane.lagVedtakFattet
import no.nav.helse.spane.objectMapper

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

        if(!personJson["vedtaksperioder"]["vedtakFattet"].isNull) {
            personJson["vedtaksperioder"].flatMap {
                it["vedtakFattet"]
            }.forEach {
                val vedtakFattet = lagVedtakFattet(it)
                person.håndter(vedtakFattet)
            }
        }
        return person
    }


}