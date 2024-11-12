package no.nav.helse.opprydding

import com.github.navikt.tbd_libs.rapids_and_rivers.JsonMessage
import com.github.navikt.tbd_libs.rapids_and_rivers.test_support.TestRapid
import kotliquery.queryOf
import kotliquery.sessionOf
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class AppTest: DataSourceBuilderTest() {
    private lateinit var testRapid: TestRapid
    private lateinit var personRepository: SlettPersonDao

    @BeforeEach
    fun beforeEach() {
        testRapid = TestRapid()
        personRepository = SlettPersonDao(dataSource)
        SlettPersonRiver(testRapid, personRepository)
    }

    @Test
    fun `slettemelding medfører at person slettes fra databasen`() {
        opprettData("123")
        assertAntallRader(fnr = "123", 1)
        testRapid.sendTestMessage(slettemelding("123"))
        assertAntallRader(fnr = "123", 0)
    }

    @Test
    fun `sletter kun aktuelt fnr`() {
        opprettData("123")
        opprettData("1234")
        testRapid.sendTestMessage(slettemelding("123"))
        assertAntallRader(fnr = "123", 0)
        assertAntallRader(fnr = "1234", 1)
    }

    private fun assertAntallRader(fnr: String, antall: Int) {
        val antallRader =  sessionOf(dataSource).use { session ->
            @Language("postgresql")
            val query = "select count(1) from person where fnr = :fnr"
            session.run(queryOf(query, mapOf("fnr" to fnr)).map { it.int(1) }.asSingle)
        } ?: 0
        assertEquals(antall, antallRader)
    }

    private fun slettemelding(fødselsnummer: String) = JsonMessage.newMessage(
        "slett_person", mapOf("fødselsnummer" to fødselsnummer)
    ).toJson()

    private fun opprettData(fødselsnummer: String) {
        val data = getData(fødselsnummer)
        @Language("postgresql")
        val query = "insert into person (fnr, data) values (:fnr, :data::json)"
        val params = mapOf<String, Any>("fnr" to fødselsnummer, "data" to data)
        sessionOf(dataSource).use { it.run(queryOf(query, params).asUpdate)}
    }
}

@Language("json")
fun getData(fnr: String) = """
    {
        "vedtaksperioder": [
            {
                "subsumsjoner": [
                    {
                        "id": "ad1f06b9-3355-4e3b-891d-13d3229a8726",
                        "versjon": "3",
                        "eventName": "sub",
                        "kilde": "kildee",
                        "versjonAvKode": "3",
                        "fodselsnummer": "1234567890",
                        "sporing": {
                            "vedtaksperiode": [
                                "6b0d05c4-8206-427f-861a-222f33831fc8"
                            ]
                        },
                        "tidsstempel": "2023-11-29T19:58:30.953579+01:00",
                        "lovverk": "loven",
                        "lovverksversjon": "3",
                        "paragraf": "8-11",
                        "ledd": null,
                        "punktum": null,
                        "bokstav": null,
                        "input": {},
                        "output": {},
                        "utfall": "GODKJENT"
                    }
                ],
                "vedtakStatus": [
                    {
                        "id": "198ca2a2-aea3-46f6-aa34-3d72d2fe2222",
                        "tidsstempel": "2023-11-29T19:58:30.956277",
                        "hendelser": [],
                        "fodselsnummer": "1234567890",
                        "vedtaksperiodeId": "6b0d05c4-8206-427f-861a-222f33831fc8",
                        "skjeringstidspunkt": "2023-11-29",
                        "fom": "2023-11-29",
                        "tom": "2023-12-29",
                        "organisasjonsnummer": "123456789",
                        "utbetalingId": "12345",
                        "eventName": "vedtakFattet"
                    }
                ],
                "orgnummer": "ukjent",
                "skjæringstidspunkt": "2023-11-29",
                "vedtaksperiodeId": "6b0d05c4-8206-427f-861a-222f33831fc8",
                "tilstand": "VEDTAK_FATTET",
                "ikkeSikkertSkjæringstidspunkt": false,
                "fom": "2023-11-29",
                "tom": "2023-12-29"
            }
        ],
        "fnr": "$fnr"
    }
"""
