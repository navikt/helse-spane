package no.nav.helse.spane

import com.fasterxml.jackson.databind.JsonNode
import no.nav.helse.VedtakFattet
import no.nav.helse.spane.db.PersonRepository
import java.time.LocalDate
import java.time.LocalDateTime

class VedtakFattetMediator(private val database: PersonRepository) {

    fun håndterer(melding: JsonNode): Boolean {
        return (melding["eventName"] != null  && melding["eventName"].asText() == "vedtakFattet")
    }

    fun håndterVedtakFattet(melding: JsonNode) {
        val fnr = melding.get("fodselsnummer").asText()

        val person = database.hentPerson(fnr)?.deserialiser() ?: throw IllegalArgumentException("Motatt vedtakFattet for person = $fnr som ikke har mottatt subsumsjoner")
        val nyVedtakFattet = lagVedtakFattet(melding)
        person.håndter(nyVedtakFattet)

        val visitor = DBVisitor()
        person.accept(visitor)
        val personJson = objectMapper.writeValueAsString(visitor.personMap)
        database.lagre(personJson, fnr)
    }


}

fun lagVedtakFattet(melding: JsonNode): VedtakFattet {
    return VedtakFattet(
        melding.get("id").asText(),
        LocalDateTime.parse(melding.get("tidsstempel").asText()),
        melding.get("hendelser").map { it.asText() },
        melding.get("fodselsnummer").asText(),
        melding.get("vedtaksperiodeId").asText(),
        LocalDate.parse(melding.get("skjeringstidspunkt").asText()),
        LocalDate.parse(melding.get("fom").asText()),
        LocalDate.parse(melding.get("tom").asText()),
        melding.get("organisasjonsnummer").asText(),
        melding.get("utbetalingId").asText(),
    )
}

/*
{
  "id": "aea7c208-e536-47c2-abeb-48eb09f41f22",
  "eventName": "vedtakFattet",
  "tidsstempel": "2022-08-03T11:36:27.932683096",
  "hendelser": [
    "6eb9e618-eed5-462e-9d2c-54f2a807e6e5",
    "be911189-01e6-463a-aa5c-a99fda13229a",
    "3e191166-74be-48b5-a083-fd2e9afed01e",
    "55f0551b-e7d2-47ff-a81e-6275ea94f8cd",
    "7b999f7d-a6e4-4519-8f00-9d7292c0758b",
    "1fa57d83-c1f6-487f-bc88-4a612e8d2ae7"
  ],
  "fodselsnummer": "22018219453",
  "vedtaksperiodeId": "a0c1d087-0746-4c79-8c87-5e3dcfc0fdcd",
  "skjeringstidspunkt": "2022-05-01",
  "fom": "2022-05-01",
  "tom": "2022-05-31",
  "organisasjonsnummer": "947064649",
  "utbetalingId": "1d79c48a-6ec0-4807-8daa-08ef09171078"
}
 */
