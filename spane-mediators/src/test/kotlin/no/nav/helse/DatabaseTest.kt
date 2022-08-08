package no.nav.helse

import no.nav.helse.spane.DBVisitor
import no.nav.helse.spane.objectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class DatabaseTest : AbstractDatabaseTest() {
    @Test
    fun `person blir lagret i database`() {
        val person = Person(FØDSELSNUMMER)
        person.håndter(lagSubsumsjon())
        person.håndter(vedtakFattet())

        val visitor = DBVisitor()

        person.accept(visitor)

        val pvp = objectMapper.writeValueAsString(visitor.personMap)

        lagrePerson(pvp)

        val hentetPerson = hentPersonJson()

        assertEquals(pvp, hentetPerson)
        assertEquals(1, objectMapper.readTree(hentetPerson)["vedtaksperioder"][0]["vedtakStatus"].size())

    }

}