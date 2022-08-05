package no.nav.helse.spane

import com.fasterxml.jackson.databind.JsonNode
import no.nav.helse.VedtaksperiodeForkastet
import no.nav.helse.sikkerlogger
import no.nav.helse.spane.db.PersonRepository
import java.time.LocalDateTime

class VedtaksperiodeForkastetMediator(private val database: PersonRepository) {

    fun håndtererForkastetVedtak(melding: JsonNode): Boolean {
        return (melding["eventName"] != null  && melding["eventName"].asText() == "vedtaksperiodeForkastet")
    }

    fun håndterForkastetVedtaksperiode(melding: JsonNode) {
        val fnr = melding.get("fodselsnummer").asText()
        sikkerlogger.info("leser melding event {}: {}", melding.get("eventName"), melding)
        val person = database.hentPerson(fnr)?.deserialiser() ?: throw IllegalArgumentException("Motatt vedtakForkastet for person = $fnr som ikke har mottatt subsumsjoner")

        val nyForkastet = lagForkastetVedtaksperiode(melding)
        person.håndter(nyForkastet)

        val visitor = DBVisitor()
        person.accept(visitor)
        val personJson = objectMapper.writeValueAsString(visitor.personMap)
        sikkerlogger.info("lagrer person {}", personJson)

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


