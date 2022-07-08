package no.nav.helse

import com.google.gson.Gson
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
class jsonToModel {
    var gson = Gson()

    var melding = """{
    "id": "85d194ae-a387-40e6-b220-917548bdcb19",
    "versjon": "1.0.0",
    "eventName": "subsumsjon",
    "kilde": "spleis",
    "versjonAvKode": "ghcr.io/navikt/helse-spleis/spleis:3d29345",
    "fodselsnummer": "12312312222",
    "sporing": {
        "soknad": ["123456"]
    },
    "tidsstempel": "2018-11-13T20:20:39Z",
    "lovverk": "folketrygdloven",
    "lovverksversjon": "2020-01-01",
    "paragraf": "5-12",
    "ledd": 1,
    "punktum": 2,
    "bokstav": "a",
    "input": {
        "foo": 1
    },
    "output": {
        "foo": 2
    },
    "utfall": "VILKÅR_BEREGNET"
}"""

    @Test
    fun lagJsonFraObjekt(){
        val jsonString = gson.toJson(TestModel(1,"Test"))
        assertEquals(jsonString, """{"id":1,"description":"Test"}""")
    }
    @Test
    fun lesJsonTilObjekt(){
        val jsonString = """{"id":1,"description":"Test"}"""
        val testModel = gson.fromJson(jsonString, TestModel::class.java)
        assertEquals(testModel.id, 1)
        assertEquals(testModel.description, "Test")
    }
}