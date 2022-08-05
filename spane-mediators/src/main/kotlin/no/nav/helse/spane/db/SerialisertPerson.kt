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

        personJson["vedtaksperioder"].flatMap { it["subsumsjoner"] }.forEach {
            val subsumsjon = lagSubsumsjonFraJson(it)
            person.håndter(subsumsjon)
        }

        personJson["vedtaksperioder"].flatMap { it["vedtakStatus"] //TODO where it["eventName"] == vedtakFattet
             }.forEach {
            val vedtakFattet = lagVedtakFattet(it)
            person.håndter(vedtakFattet)
        }
        val vedtaksStatus =personJson["vedtaksperioder"]["vedtakStatus"]
        if (vedtaksStatus != null && !vedtaksStatus.isNull && vedtaksStatus.asText() != "") {
            personJson["vedtaksperioder"].flatMap { it["vedtakStatus"]  //TODO where it["eventName"] == vedtakForkastetEllerNoe
            }.forEach {
                val forkastet = lagForkastetVedtaksperiode(it)
                person.håndter(forkastet)
            }
        }
        return person
    }


}