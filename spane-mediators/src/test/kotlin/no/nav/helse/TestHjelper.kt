package no.nav.helse

class TestHjelper {
    companion object {
        val melding = """{
          "id": "f8d81915-f30d-48d9-a709-c9d7b897f376",
          "tidsstempel": "2022-02-22T10:18:54.204322399+01:00",
          "eventName": "subsumsjon",
          "versjon": "1.0.0",
          "kilde": "syfosmregler",
          "versjonAvKode": "docker.pkg.github.com/navikt/syfosmregler/syfosmregler:2b455f3007b5ec4338525dc313c9ff28b8dbbc9b",
          "fodselsnummer": "10877799145",
          "sporing": {
            "sykmelding": [
              "3c01a382-a80d-462f-8125-591b07f30574"
            ]
          },
          "lovverk": "folketrygdloven",
          "lovverksversjon": "2022-01-01",
          "paragraf": "8-3",
          "ledd": 1,
          "punktum": 2,
          "bokstav": null,
          "input": {
            "forsteFomDato": "2022-01-01",
            "pasientFodselsdato": "1987-06-24"
          },
          "output": null,
          "utfall": "VILKAR_OPPFYLT"
        }""".trimIndent()

    }

}