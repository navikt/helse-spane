package no.nav.helse

import kotlinx.coroutines.Job
import no.nav.common.KafkaEnvironment
import no.nav.helse.spane.db.PersonPostgresRepository
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.producer.Producer
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File

internal class DatabaseTest: AbstraktDatabaseTest() {
    private val testTopic = "testTopic"
    private lateinit var kafkaProducer: Producer<String, String>
    private lateinit var kafkaConsumer: Consumer<String, String>
    private lateinit var jobb: Job
    private val embeddedKafkaEnvironment = KafkaEnvironment(
        autoStart = false,
        noOfBrokers = 1,
        topicInfos = listOf(testTopic).map { KafkaEnvironment.TopicInfo(it, partitions = 1) },
        withSchemaRegistry = false,
        withSecurity = false
    )
    @Test
    @Disabled("ikke ferdig skrevet")
    fun `lagre person i database`() {
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
        val dataSource = dataSourceBuilder.getDataSource()
        val personRepository = PersonPostgresRepository(dataSource)
        //val json = object {}.javaClass.getResourceAsStream("testPerson.json")?.bufferedReader()?.readLines()
        //val json = File("/test/kotlin/resources/testPerson.json").inputStream().bufferedReader().use { it.readText() }
        //println(json)
        //personRepository.lagre()
    }

}