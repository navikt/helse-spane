package no.nav.helse

import no.nav.common.KafkaEnvironment
import no.nav.helse.spane.db.PersonPostgresRepository
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal abstract class AbstractDatabaseTest {
    private lateinit var personRepository: PersonPostgresRepository

    private val testTopic = "testTopic"
    protected val FØDSELSNUMMER = "1234567890"
    protected val VEDTAKSPERIODE_ID = UUID.randomUUID().toString()

    private val embeddedKafkaEnvironment = KafkaEnvironment(
        autoStart = false,
        noOfBrokers = 1,
        topicInfos = listOf(testTopic).map { KafkaEnvironment.TopicInfo(it, partitions = 1) },
        withSchemaRegistry = false,
        withSecurity = false
    )

    @BeforeAll
    internal fun setupAll() {
        val konfig = Konfig(
            "Spane",
            listOf(embeddedKafkaEnvironment.brokersURL),
            testTopic,
            "kaSomHelst",
            PostgresContainer.instance.jdbcUrl,
            PostgresContainer.instance.username,
            PostgresContainer.instance.password,
            1,
            250,
            100,
            100,
            null,
            null,
            null
        )

        val dataSourceBuilder = DataSourceBuilder(konfig)
        personRepository = PersonPostgresRepository(dataSourceBuilder.getDataSource())

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