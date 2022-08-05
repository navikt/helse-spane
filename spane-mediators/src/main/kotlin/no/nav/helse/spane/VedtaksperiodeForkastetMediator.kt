package no.nav.helse.spane

import com.fasterxml.jackson.databind.JsonNode
import no.nav.helse.VedtaksperiodeForkastet
import no.nav.helse.sikkerlogger
import no.nav.helse.spane.db.PersonRepository
import java.time.LocalDateTime

class VedtaksperiodeForkastetMediator(private val database: PersonRepository) {

    fun håndtererForkastetVedtak(melding: JsonNode): Boolean {
        return (melding["eventName"] != null && melding["eventName"].asText() == "vedtaksperiodeForkastet")
    }


    fun håndterForkastetVedtaksperiode(melding: JsonNode) {
        val fnr = melding.get("fodselsnummer").asText()
        val person = database.hentPerson(fnr)?.deserialiser()
        if (person == null) {
            sikkerlogger.info("Motatt vedtaksperiodeForkastet for person = $fnr som ikke har mottatt subsumsjoner")
            return
        }

        val nyForkastet = lagForkastetVedtaksperiode(melding)
        val bleHåndtert = !person.håndter(nyForkastet)
        val visitor = DBVisitor()
        person.accept(visitor)
        val personJson = objectMapper.writeValueAsString(visitor.personMap)

        if (bleHåndtert) {
            sikkerlogger.info("Vedtaksperiode Forkastet melding ikke håndtert: {}", melding)
            sikkerlogger.info("lagrer person {}", personJson)
        }

        database.lagre(personJson, fnr)
    }
}

fun lagForkastetVedtaksperiode(melding: JsonNode): VedtaksperiodeForkastet {
    return VedtaksperiodeForkastet(
        melding.get("id").asText(),
        LocalDateTime.parse(melding.get("tidsstempel").asText()),
        melding.get("fodselsnummer").asText(),
        melding.get("vedtaksperiodeId").asText(),
        melding.get("organisasjonsnummer").asText(),
    )
}


