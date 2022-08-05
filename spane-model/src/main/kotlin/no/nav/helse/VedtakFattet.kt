package no.nav.helse

import java.time.LocalDate
import java.time.LocalDateTime

class VedtakFattet(
    private val id: String,
    private val tidsstempel: LocalDateTime,
    private val hendelser: List<String>,
    private val fødselsnummer: String,
    private val vedtaksperiodeId: String,
    private val skjeringstidspunkt: LocalDate,
    private val fom: LocalDate,
    private val tom: LocalDate,
    private val organisasjonsnummer: String,
    private val utbetalingsId: String,
    private val eventName: String = "vedtakFattet"
) : TilstandVedtaksperiode {

    override fun hørerTil(vedtaksperiodeId: String?) = this.vedtaksperiodeId == vedtaksperiodeId

    override fun accept(visitor: VedtakVisitor) {
        visitor.visitVedtakFattet(
            id,
            tidsstempel,
            hendelser,
            fødselsnummer,
            vedtaksperiodeId,
            skjeringstidspunkt,
            fom,
            tom,
            organisasjonsnummer,
            utbetalingsId,
            eventName
        )
    }
}


/*
{
  "id": "aea7c208-e536-47c2-abeb-48eb09f41f22",
  "eventName": "vedtakFattet",
  "tidsstempel": "2022-08-03T11:36:27.932683096",
  "hendelser": [
    "6eb9e618-eed5-462e-9d2c-54f2a807e6e5",
    "be911189-01e6-463a-aa5c-a99fda13229a",
    "3e191166-74be-48b5-a083-fd2e9afed01e",
    "55f0551b-e7d2-47ff-a81e-6275ea94f8cd",
    "7b999f7d-a6e4-4519-8f00-9d7292c0758b",
    "1fa57d83-c1f6-487f-bc88-4a612e8d2ae7"
  ],
  "fodselsnummer": "22018219453",
  "vedtaksperiodeId": "a0c1d087-0746-4c79-8c87-5e3dcfc0fdcd",
  "skjeringstidspunkt": "2022-05-01",
  "fom": "2022-05-01",
  "tom": "2022-05-31",
  "organisasjonsnummer": "947064649",
  "utbetalingId": "1d79c48a-6ec0-4807-8daa-08ef09171078"
}
 */
