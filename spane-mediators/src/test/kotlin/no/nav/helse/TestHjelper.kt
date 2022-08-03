package no.nav.helse

class TestHjelper {
    companion object {
        val melding = """{
  "id": "c1d728cc-8320-44c9-a9f0-fe7e68d14b3f",
  "eventName": "subsumsjon",
  "tidsstempel": "2022-08-03T15:29:57.156659595+02:00",
  "versjon": "1.0.0",
  "kilde": "spleis",
  "versjonAvKode": "ghcr.io/navikt/helse-spleis/spleis:eca0650",
  "fodselsnummer": "22018219453",
  "sporing": {
    "vedtaksperiode": [
      "a0c1d087-0746-4c79-8c87-5e3dcfc0fdcd"
    ],
    "soknad": [
      "413e3bd6-41e0-4d9e-826f-79c5197ed39b"
    ],
    "inntektsmelding": [
      "532a76bb-ecef-4f31-81bc-ad85657a7ac8",
      "87a0b387-b591-43a9-b28c-635e81761d55"
    ],
    "overstyrtidslinje": [
      "55f0551b-e7d2-47ff-a81e-6275ea94f8cd",
      "c43d0c87-1de8-4490-8893-97cc7dc27c4e"
    ],
    "overstyrinntekt": [
      "7b999f7d-a6e4-4519-8f00-9d7292c0758b",
      "1fa57d83-c1f6-487f-bc88-4a612e8d2ae7"
    ],
    "organisasjonsnummer": [
      "947064649"
    ],
    "sykmelding": [
      "b15fe27f-432b-4d56-9cdb-275124435112"
    ]
  },
  "lovverk": "folketrygdloven",
  "lovverksversjon": "2021-05-21",
  "paragraf": "8-12",
  "input": {
    "fom": "2022-05-01",
    "tom": "2022-05-31",
    "utfallFom": "2022-05-17",
    "utfallTom": "2022-05-31",
    "tidslinjegrunnlag": [
      [
        {
          "fom": "2022-05-01",
          "tom": "2022-05-16",
          "dagtype": "AGPDAG",
          "grad": 0
        },
        {
          "fom": "2022-05-17",
          "tom": "2022-05-17",
          "dagtype": "NAVDAG",
          "grad": 80
        },
        {
          "fom": "2022-05-18",
          "tom": "2022-05-31",
          "dagtype": "NAVDAG",
          "grad": 100
        }
      ]
    ],
    "beregnetTidslinje": [
      {
        "fom": "2022-05-01",
        "tom": "2022-05-16",
        "dagtype": "AGPDAG",
        "grad": 0
      },
      {
        "fom": "2022-05-17",
        "tom": "2022-05-17",
        "dagtype": "NAVDAG",
        "grad": 80
      },
      {
        "fom": "2022-05-18",
        "tom": "2022-05-31",
        "dagtype": "NAVDAG",
        "grad": 100
      }
    ]
  },
  "output": {
    "gjenståendeSykedager": 237,
    "forbrukteSykedager": 11,
    "maksdato": "2023-04-27"
  },
  "utfall": "VILKAR_OPPFYLT",
  "ledd": 1,
  "punktum": 1,
  "bokstav": null
}""".trimIndent()

    }

}