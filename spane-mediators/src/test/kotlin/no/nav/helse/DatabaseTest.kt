package no.nav.helse

import no.nav.helse.spane.APIVisitor
import no.nav.helse.spane.objectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class DatabaseTest : AbstraktDatabaseTest() {
    @Test
    fun `person blir lagret i database`() {

        val person = Person(FØDSELSNUMMER)
        person.håndter(lagSubsumsjon())

        val besøkende = APIVisitor()

        person.accept(besøkende)

        val utputt = objectMapper.writeValueAsString(besøkende.personMap)

        lagrePerson(utputt)

        assertEquals(utputt, hentPersonJson())
    }


}