package no.nav.helse.spane

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.helse.Person
import no.nav.helse.Subsumsjon
import no.nav.helse.logger
import no.nav.helse.sikkerlogger
import no.nav.helse.spane.db.PersonRepository
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeParseException

val objectMapper = jacksonObjectMapper()
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    .registerModule(JavaTimeModule())

fun håndterSubsumsjon(value: String, database: PersonRepository) {
    val melding = objectMapper.readTree(value)

    if(melding["eventName"] == null || melding["eventName"].asText() != "subsumsjon") {
        logger.info("melding id: {}, eventName: {} blir ikke håndtert", melding["id"], melding["eventName"])
        return
    }

    if(melding["fodselsnummer"] == null || melding["fodselsnummer"].isNull ||  melding["fodselsnummer"].asText() == "" ) {
        sikkerlogger.info("Fant subsumsjon med null i fnr {}", melding)
        return
    }

    val fnr = melding.get("fodselsnummer").asText()

    val person = database.hentPerson(fnr)?.deserialiser() ?: Person(fnr)
    val nySubsumsjon = lagSubsumsjonFraJson(melding)
    person.håndter(nySubsumsjon)

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
        lesTidsstempel(melding),
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


fun lesTidsstempel(melding: JsonNode): ZonedDateTime{
    return try {
        ZonedDateTime.parse(melding.get("tidsstempel").asText())
    }catch (e: DateTimeParseException){
        ZonedDateTime.of(LocalDateTime.parse(melding.get("tidsstempel").asText()), ZoneId.systemDefault())
    }
}





