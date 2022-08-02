package no.nav.helse

import no.nav.common.KafkaEnvironment
import no.nav.helse.spane.db.PersonPostgresRepository
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal abstract class AbstraktDatabaseTest {
    private lateinit var personRepository: PersonPostgresRepository

    private val testTopic = "testTopic"

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

    protected fun hentPerson(){
        val person = personRepository.hentPerson("10877799145")
        if (person != null) {
            println(person.json)
        }
    }

    fun assertPersonLagret(json: String){
        personRepository.lagre(json, "180992 gammel")
    }



}