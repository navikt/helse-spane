package no.nav.helse

import no.nav.helse.spane.lagSubsumsjonFraJson
import no.nav.helse.spane.objectMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class DeserialiseringTest {


    @Test
    fun `deseraliser av subsjumsjon`(){
        val melding = """
            {
              "id": "be487a53-baee-42b2-b20b-d50f84ea6ade",
              "eventName": "subsumsjon",
              "versjon": "1.0.0",
              "kilde": "syfosmregler",
              "tidsstempel": "2022-08-04T15:15:52.507933635+02",
              "versjonAvKode": "docker.pkg.github.com",
              "fodselsnummer": "29108522745",
              "sporing": {
                "sykmeldingsid": [
                  "21385661-4b23-45fb-908f-616f90f4946c"
                ]
              },
              "lovverk": "folketrygdloven",
              "lovverksversjon": "2022-01-01",
              "paragraf": "8-7",
              "ledd": 2,
              "punktum": null,
              "bokstav": null,
              "input": {
                "data": "data"
              },
              "output": null,
              "utfall": "VILKAR_OPPFYLT"
            }
        """.trimIndent()
        assertDoesNotThrow { lagSubsumsjonFraJson(objectMapper.readTree(melding)) }
    }
}