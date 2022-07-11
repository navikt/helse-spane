package no.nav.helse

import no.nav.helse.Subsumsjon.Companion.finnAlle
import no.nav.helse.Subsumsjon.Companion.sorterPåTid
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.*

internal class SubsumsjonTest {


    @Test
    fun `filtrer på paragraf`() {
        val subsumsjoner = listOf(lagSubsumsjon("8-11"), lagSubsumsjon("8-13"), lagSubsumsjon("8-13"))
        val resultat = subsumsjoner.finnAlle("8-11")
        assertEquals(1, resultat.size)
    }

    @Test
    fun `sorter på tid`() {
        val subsumsjoner = listOf(
            lagSubsumsjon(tidsstempel = 31.januar(2022)),
            lagSubsumsjon(tidsstempel = 3.januar(2022)),
            lagSubsumsjon(tidsstempel = 4.februar(2022))
        )

        val sortert = listOf(
            lagSubsumsjon(tidsstempel = 3.januar(2022)),
            lagSubsumsjon(tidsstempel = 31.januar(2022)),
            lagSubsumsjon(tidsstempel = 4.februar(2022))
        )
        assertEquals(sortert, subsumsjoner.sorterPåTid())

    }


    fun lagSubsumsjon(paragraf: String = "8-11", tidsstempel: ZonedDateTime = 1.januar(2022)): Subsumsjon {
        return Subsumsjon(
            "id", "3", "sub", "kildee", "3",
            "1234567890", emptyMap(), tidsstempel, "loven", "3",
            paragraf, null, null, null, emptyMap(), emptyMap(), "GODKJENT"
        )
    }


}

fun Int.januar(year: Int): ZonedDateTime =
    ZonedDateTime.of(LocalDateTime.of(year, 1, this, 0, 0), ZoneId.systemDefault())

fun Int.februar(year: Int): ZonedDateTime =
    ZonedDateTime.of(LocalDateTime.of(year, 2, this, 0, 0), ZoneId.systemDefault())

fun Int.mars(year: Int): ZonedDateTime = ZonedDateTime.of(LocalDateTime.of(year, 3, this, 0, 0), ZoneId.systemDefault())
fun Int.april(year: Int): ZonedDateTime =
    ZonedDateTime.of(LocalDateTime.of(year, 4, this, 0, 0), ZoneId.systemDefault())

fun Int.mai(year: Int): ZonedDateTime = ZonedDateTime.of(LocalDateTime.of(year, 5, this, 0, 0), ZoneId.systemDefault())
fun Int.juni(year: Int): ZonedDateTime = ZonedDateTime.of(LocalDateTime.of(year, 6, this, 0, 0), ZoneId.systemDefault())
fun Int.juli(year: Int): ZonedDateTime = ZonedDateTime.of(LocalDateTime.of(year, 7, this, 0, 0), ZoneId.systemDefault())
fun Int.august(year: Int): ZonedDateTime =
    ZonedDateTime.of(LocalDateTime.of(year, 8, this, 0, 0), ZoneId.systemDefault())

fun Int.september(year: Int): ZonedDateTime =
    ZonedDateTime.of(LocalDateTime.of(year, 9, this, 0, 0), ZoneId.systemDefault())

fun Int.oktober(year: Int): ZonedDateTime =
    ZonedDateTime.of(LocalDateTime.of(year, 10, this, 0, 0), ZoneId.systemDefault())

fun Int.november(year: Int): ZonedDateTime =
    ZonedDateTime.of(LocalDateTime.of(year, 11, this, 0, 0), ZoneId.systemDefault())

fun Int.desember(year: Int): ZonedDateTime =
    ZonedDateTime.of(LocalDateTime.of(year, 12, this, 0, 0), ZoneId.systemDefault())