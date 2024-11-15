package no.nav.helse

import com.github.navikt.tbd_libs.test_support.TestDataSource
import no.nav.helse.spane.db.PersonPostgresRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal abstract class AbstractDatabaseTest {
    private lateinit var personRepository: PersonPostgresRepository

    protected val FØDSELSNUMMER = "1234567890"
    protected val VEDTAKSPERIODE_ID = UUID.randomUUID().toString()

    private lateinit var testDataSource: TestDataSource
    protected val dataSource get() = testDataSource.ds

    @BeforeEach
    fun before() {
        testDataSource = databaseContainer.nyTilkobling()
        personRepository = PersonPostgresRepository(dataSource)
    }

    @AfterEach
    fun after() {
        databaseContainer.droppTilkobling(testDataSource)
    }

    protected fun hentPersonJson() = personRepository.hentPerson(FØDSELSNUMMER)?.json

    fun lagrePerson(json: String) = personRepository.lagre(json, FØDSELSNUMMER)

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