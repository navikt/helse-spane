package no.nav.helse.spane

import com.fasterxml.jackson.databind.JsonNode
import no.nav.helse.VedtakFattet
import no.nav.helse.VedtaksperiodeForkastet
import no.nav.helse.spane.db.PersonRepository
import java.time.LocalDate
import java.time.LocalDateTime

class ForkastetMediator(private val database: PersonRepository) {

    fun håndtererForkastetVedtak(melding: JsonNode): Boolean {
        return (melding["eventName"] != null  && melding["eventName"].asText() == "vedtaksperiodeForkastet")
    }

    fun håndterForkastet(melding: JsonNode) {
        val fnr = melding.get("fodselsnummer").asText()

        val person = database.hentPerson(fnr)?.deserialiser() ?: throw IllegalArgumentException("Motatt vedtakForkastet for person = $fnr som ikke har mottatt subsumsjoner")
        val nyForkastet = lagForkastet(melding)
        person.håndter(nyForkastet)

        val visitor = DBVisitor()
        person.accept(visitor)
        val personJson = objectMapper.writeValueAsString(visitor.personMap)
        database.lagre(personJson, fnr)
    }


}


//TODO: Dette er feil
fun lagForkastet(melding: JsonNode): VedtaksperiodeForkastet {
    return VedtaksperiodeForkastet(
        melding.get("id").asText(),
        LocalDateTime.parse(melding.get("tidsstempel").asText()),
        melding.get("fodselsnummer").asText(),
        melding.get("vedtaksperiodeId").asText(),
        melding.get("organisasjonsnummer").asText(),
    )
}


