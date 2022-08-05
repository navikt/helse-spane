package no.nav.helse.spane.db

import no.nav.helse.Person
import no.nav.helse.spane.lagForkastetVedtaksperiode
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

        personJson["vedtaksperioder"].flatMap {
            it["vedtakStatus"]
        }.forEach {
            val vedtakFattet = lagVedtakFattet(it)
            person.håndter(vedtakFattet)
        }
        if(personJson["vedtaksperioder"]["vedtakStatus"] != null && !personJson["vedtaksperioder"]["vedtakStatus"].isNull &&
            personJson["vedtaksperioder"]["vedtakStatus"].asText() != "" ) {
            personJson["vedtaksperioder"].flatMap {
                it["forkastet"]
            }.forEach {
                val forkastet = lagForkastetVedtaksperiode(it)
                person.håndter(forkastet)
            }
        }
        return person
    }


}