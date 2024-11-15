package no.nav.helse

import no.nav.helse.spane.DBVisitor
import no.nav.helse.spane.db.PersonPostgresRepository
import no.nav.helse.spane.objectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*

internal class DatabaseTest {
    private val FØDSELSNUMMER = "1234567890"
    private val VEDTAKSPERIODE_ID = UUID.randomUUID().toString()

    @Test
    fun `person blir lagret i database`() = databaseTest {
        val person = Person(FØDSELSNUMMER)
        person.håndter(lagSubsumsjon())
        person.håndter(vedtakFattet())

        val visitor = DBVisitor()

        person.accept(visitor)

        val pvp = objectMapper.writeValueAsString(visitor.personMap)

        lagrePerson(pvp)

        val hentetPerson = hentPersonJson()

        assertEquals(objectMapper.readTree(pvp), objectMapper.readTree(hentetPerson))
        assertEquals(1, objectMapper.readTree(hentetPerson)["vedtaksperioder"][0]["vedtakStatus"].size())

    }

    private fun databaseTest(testblokk: PersonPostgresRepository.() -> Unit) {
        val testDataSource = databaseContainer.nyTilkobling()
        try {
            testblokk(PersonPostgresRepository(testDataSource.ds))
        } finally {
            databaseContainer.droppTilkobling(testDataSource)
        }
    }

    private fun PersonPostgresRepository.hentPersonJson() = hentPerson(FØDSELSNUMMER)?.json
    private fun PersonPostgresRepository.lagrePerson(json: String) = lagre(json, FØDSELSNUMMER)

    fun lagSubsumsjon(
        paragraf: String = "8-11",
        tidsstempel: ZonedDateTime = ZonedDateTime.now(),
        sporing: Map<String, List<String>> = mapOf("vedtaksperiode" to listOf(VEDTAKSPERIODE_ID)),
        input: Map<String, Any> = emptyMap(),
        output: Map<String, Any> = emptyMap(),
        id: String = UUID.randomUUID().toString()

    ): Subsumsjon {
        return Subsumsjon(
            id, "3", "sub", "kildee", "3",
            FØDSELSNUMMER, sporing, tidsstempel, "loven", "3",
            paragraf, null, null, null, input, output, "GODKJENT"
        )
    }

    fun vedtakFattet(): VedtakFattet {
        val skjæringstidspunkt = LocalDate.now()
        return VedtakFattet(
            UUID.randomUUID().toString(),
            LocalDateTime.now(),
            emptyList(),
            FØDSELSNUMMER,
            VEDTAKSPERIODE_ID,
            skjæringstidspunkt,
            skjæringstidspunkt,
            skjæringstidspunkt.plusDays(30),
            "123456789",
            "12345"
        )
    }
}