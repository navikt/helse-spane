package no.nav.helse

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

private const val FØDSELSNUMMER = "1234567890"

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


        fun lagSporing(
            sykmeldingId: List<String>,
            søknadId: List<String> = emptyList(),
            vedtaksperiodeId: List<String> = emptyList(),
        ): Map<String, List<String>>{

            return mapOf("sykmelding" to sykmeldingId,
                "soknad" to søknadId,
                "vedtaksperiode" to vedtaksperiodeId)
        }
        fun lagSubsumsjon(
            paragraf: String = "8-11",
            tidsstempel: ZonedDateTime = 1.januar(2022),
            sporing: Map<String, List<String>> = emptyMap(),
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

        fun lagVedtaksPeriode(
            antallSubsumSjoner: Int,
            paragraf: String = "8-11",
            tidsstempel: ZonedDateTime = 1.januar(2022),
            sporing: Map<String, List<String>> = mapOf("vedtaksperiode" to listOf(UUID.randomUUID().toString())),
            input: Map<String, Any> = emptyMap(),
            output: Map<String, Any> = emptyMap()
        ): PseudoVedtaksperiode {
            val subsumsjoner = mutableListOf<Subsumsjon>()
            for (i in 1..antallSubsumSjoner) {
                subsumsjoner.add(
                    lagSubsumsjon(
                        paragraf = paragraf,
                        tidsstempel = tidsstempel,
                        sporing = sporing,
                        input = input,
                        output = output,
                        id = UUID.randomUUID().toString()
                    )
                )
            }
            return PseudoVedtaksperiode(subsumsjoner)
        }
        fun PseudoVedtaksperiode.inspektør(): TestVisitor.TestPseudoVedtaksperiode {
            val visitor = TestVisitor()
            this.accept(visitor)
            return visitor.vedtaksperioder[0]
        }

        fun vedtakFattet(pvp: PseudoVedtaksperiode): VedtakFattet {
            val skjæringstidspunkt = LocalDate.now()
            return VedtakFattet(
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                emptyList(),
                FØDSELSNUMMER,
                pvp.inspektør().vedtaksperiodeId ?: throw IllegalArgumentException("Kan ikke fatte vedtak på pvp uten en subsumsjon med vedtaksperiodeId"),
                skjæringstidspunkt,
                skjæringstidspunkt,
                skjæringstidspunkt.plusDays(30),
                "123456789",
                "12345"
            )
        }
    }

}