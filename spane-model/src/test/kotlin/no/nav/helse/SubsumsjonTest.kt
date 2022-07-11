package no.nav.helse

import no.nav.helse.Subsumsjon.Companion.finnAlle
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class SubsumsjonTest {

    @Test
    fun `filtrer på paragraf`() {
        val paragrafer = listOf(Subsumsjon("id", "3", "sub", "kildee", "3",
            "1234567890", emptyMap(), "12:30", "loven", "3",
            "8-11", null, null, null, emptyMap(), emptyMap(), "GODKJENT"
        ))
        val resultat = paragrafer.finnAlle("8-11")
        assertEquals(1, resultat.size)
    }


}