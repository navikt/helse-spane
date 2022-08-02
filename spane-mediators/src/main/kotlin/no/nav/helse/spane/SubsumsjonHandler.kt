package no.nav.helse.spane

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.helse.Person
import no.nav.helse.Subsumsjon
import no.nav.helse.sikkerlogger
import no.nav.helse.spane.db.PersonRepository
import java.time.ZonedDateTime

val objectMapper = jacksonObjectMapper()
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    .registerModule(JavaTimeModule())

fun håndterSubsumsjon(value: String, database: PersonRepository) {
    val melding = objectMapper.readTree(value)
    val fnr = melding["fodselsnummer"].asText()

    val person = database.hentPerson(fnr)?.deserialiser() ?: Person(fnr)
    val nySubsumsjon = lagSubsumsjonFraJson(melding)
    person.håndter(nySubsumsjon)
    sikkerlogger.info("Mottok melding som hadde forventet fødselsnummer {}", person.toString())

    val apiVisitor = APIVisitor()
    person.accept(apiVisitor)
    val personJson = objectMapper.writeValueAsString(apiVisitor.personMap)
    database.lagre(personJson, fnr)
}


fun lagSubsumsjonFraJson(melding: JsonNode): Subsumsjon {
    val subsumsjon = Subsumsjon(
        melding.get("id").asText(),
        melding.get("versjon").asText(),
        melding.get("eventName").asText(),
        melding.get("kilde").asText(),
        melding.get("versjonAvKode").asText(),
        melding.get("fodselsnummer").asText(),
        objectMapper.convertValue(melding.get("sporing")),
        ZonedDateTime.parse(melding.get("tidsstempel").asText()),
        melding.get("lovverk").asText(),
        melding.get("lovverksversjon").asText(),
        melding.get("paragraf").asText(),
        melding.get("ledd")?.asInt(),
        melding.get("punktum")?.asInt(),
        melding.get("bokstav")?.asText(),
        objectMapper.convertValue(melding.get("input")),
        objectMapper.convertValue(melding.get("output")) ?: emptyMap(),
        melding.get("utfall").asText(),
    )
    return subsumsjon
}





