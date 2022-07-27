package no.nav.helse

import org.junit.jupiter.api.fail
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

internal class TestHjelper {


    companion object {
        fun Int.januar(year: Int): ZonedDateTime =
            ZonedDateTime.of(LocalDateTime.of(year, 1, this, 0, 0), ZoneId.systemDefault())

        fun Int.februar(year: Int): ZonedDateTime =
            ZonedDateTime.of(LocalDateTime.of(year, 2, this, 0, 0), ZoneId.systemDefault())

        fun Int.mars(year: Int): ZonedDateTime =
            ZonedDateTime.of(LocalDateTime.of(year, 3, this, 0, 0), ZoneId.systemDefault())

        fun Int.april(year: Int): ZonedDateTime =
            ZonedDateTime.of(LocalDateTime.of(year, 4, this, 0, 0), ZoneId.systemDefault())

        fun Int.mai(year: Int): ZonedDateTime =
            ZonedDateTime.of(LocalDateTime.of(year, 5, this, 0, 0), ZoneId.systemDefault())

        fun Int.juni(year: Int): ZonedDateTime =
            ZonedDateTime.of(LocalDateTime.of(year, 6, this, 0, 0), ZoneId.systemDefault())

        fun Int.juli(year: Int): ZonedDateTime =
            ZonedDateTime.of(LocalDateTime.of(year, 7, this, 0, 0), ZoneId.systemDefault())

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

        fun lagSubsumsjon(
            paragraf: String = "8-11",
            tidsstempel: ZonedDateTime = 1.januar(2022),
            sporing: Map<String, List<String>> = emptyMap(),
            input: Map<String, Any> = emptyMap(),
            output: Map<String, Any> = emptyMap(),

            ): Subsumsjon {
            return Subsumsjon(
                "id", "3", "sub", "kildee", "3",
                "1234567890", sporing, tidsstempel, "loven", "3",
                paragraf, null, null, null, input, output, "GODKJENT"
            )
        }

        fun lagVedtaksPeriode(
            antallSubsumSjoner: Int,
            paragraf: String = "8-11",
            tidsstempel: ZonedDateTime = 1.januar(2022),
            sporing: Map<String, List<String>> = emptyMap(),
            input: Map<String, Any> = emptyMap(),
            output: Map<String, Any> = emptyMap(),
        ): PseudoVedtaksperiode {
            val subsumsjoner = mutableListOf<Subsumsjon>()
            for (i in 1..antallSubsumSjoner) {
                subsumsjoner.add(
                    lagSubsumsjon(
                        paragraf = paragraf,
                        tidsstempel = tidsstempel,
                        sporing = sporing,
                        input = input,
                        output = output
                    )
                )
            }
            return PseudoVedtaksperiode(subsumsjoner)
        }

        fun assertVedtaksperioderAntallOgLengde(
            vedtaksperioder: MutableList<PseudoVedtaksperiode>,
            forventetAntallVedtaksperioder: Int,
            forventetAntallSubsumsjoner: Int
        ) {
            if (forventetAntallVedtaksperioder != vedtaksperioder.size) {
                fail("Liste av vedtaksperioder har ikke forventet antall vedtaksperioder: expected: $forventetAntallVedtaksperioder actual: ${vedtaksperioder.size}")
            }
            if (forventetAntallSubsumsjoner != vedtaksperioder[0].antallSubsumsjoner()) {
                fail("Liste av vedtaksperioder har ikke forventet antall vedtaksperioder: expected: $forventetAntallSubsumsjoner actual: ${vedtaksperioder[0].antallSubsumsjoner()}")
            }
        }


    }


}