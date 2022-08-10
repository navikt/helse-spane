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


class SubsumsjonMediator(private val database: PersonRepository) {
    fun håndterer(melding: JsonNode): Boolean {
        if (melding["eventName"] == null || melding["eventName"].textValue() != "subsumsjon") {
            logger.info("melding id: {}, eventName: {} blir ikke håndtert", melding["id"], melding["eventName"])
            return false
        } else if (melding["fodselsnummer"] == null || melding["fodselsnummer"].isNull || melding["fodselsnummer"].textValue() == "") {
            sikkerlogger.info("Fant subsumsjon med manglende fødselsnummer {}", melding)
            return false
        }
        return true
    }

    fun håndterSubsumsjon(melding: JsonNode) {

        val fnr = melding["fodselsnummer"].textValue()

        val person = database.hentPerson(fnr)?.deserialiser() ?: Person(fnr)
        val nySubsumsjon = try {
            lagSubsumsjonFraJson(melding)
        } catch (e: Exception) {
            logger.error("Kan ikke håndtere melding")
            sikkerlogger.error("Kan ikke håndtere melding: $melding", e)
            return
        }

        person.håndter(nySubsumsjon)

        val DBVisitor = DBVisitor()
        person.accept(DBVisitor)
        val personJson = objectMapper.writeValueAsString(DBVisitor.personMap)
        database.lagre(personJson, fnr)
//        database.lagreParagrafkobling(
//            melding["paragraf"].textValue(),
//            melding["ledd"]?.asInt(),
//            melding["bokstav"]?.textValue(),
//            melding["punktum"]?.asInt(),
//            fnr
//        )
    }


}

private fun lesTidsstempel(melding: JsonNode): ZonedDateTime {
    val tidsstempel = melding["tidsstempel"]?.textValue() ?: ZonedDateTime.now().toString().also {
        logger.error("subsumsjon inneholder ikke tidsstempel meldingsid: {}", melding["id"])
        sikkerlogger.error("subsumsjon inneholder ikke tidsstempel melding: {}", melding)
    }
    return try {
        ZonedDateTime.parse(tidsstempel)
    } catch (e: DateTimeParseException) {
        ZonedDateTime.of(LocalDateTime.parse(tidsstempel), ZoneId.systemDefault())
    }
}


fun lagSubsumsjonFraJson(melding: JsonNode): Subsumsjon {

    val subsumsjon = Subsumsjon(
        melding["id"]?.textValue() ?: throw IllegalArgumentException("subsumsjon har ikke id felt"),
        melding["versjon"]?.textValue() ?: throw IllegalArgumentException("subsumsjon har ikke versjon felt"),
        melding["eventName"]?.textValue() ?: throw IllegalArgumentException("subsumsjon har ikke eventName felt"),
        melding["kilde"]?.textValue() ?: throw IllegalArgumentException("subsumsjon har ikke kilde felt"),
        melding["versjonAvKode"]?.textValue()
            ?: throw IllegalArgumentException("subsumsjon har ikke versjonAvKode felt"),
        melding["fodselsnummer"]?.textValue()
            ?: throw IllegalArgumentException("subsumsjon har ikke fodselsnummer felt"),
        objectMapper.convertValue(
            melding["sporing"] ?: throw IllegalArgumentException("subsumsjon har ikke sporing felt")
        ),
        lesTidsstempel(melding),
        melding["lovverk"]?.textValue() ?: throw IllegalArgumentException("subsumsjon har ikke lovverk felt"),
        melding["lovverksversjon"]?.textValue()
            ?: throw IllegalArgumentException("subsumsjon har ikke lovverksversjon felt"),
        melding["paragraf"]?.textValue() ?: throw IllegalArgumentException("subsumsjon har ikke paragraf felt"),
        melding["ledd"]?.intValue(),
        melding["punktum"]?.intValue(),
        melding["bokstav"]?.textValue(),
        objectMapper.convertValue(melding["input"]),
        objectMapper.convertValue(melding["output"]) ?: emptyMap(),
        melding["utfall"].textValue(),
    )
    return subsumsjon
}

