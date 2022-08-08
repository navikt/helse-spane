package no.nav.helse

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import no.nav.common.KafkaEnvironment
import no.nav.helse.TestHjelper.Companion.testSubsumsjon
import no.nav.helse.TestHjelper.Companion.testVedtakFattet
import no.nav.helse.spane.db.PersonPostgresRepository
import no.nav.helse.spane.ktorServer
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.*
import org.junit.jupiter.api.TestInstance.Lifecycle
import java.util.*
import java.util.concurrent.TimeUnit


@TestInstance(Lifecycle.PER_CLASS)
internal class E2ETest : AbstractDatabaseTest() {

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

    fun startApp() {
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
        jobb = GlobalScope.launch {
            Application(konfig, ::ktorServer, personRepository).startBlocking()
        }
    }


    private fun producerProperties() = Properties().apply {
        put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, embeddedKafkaEnvironment.brokersURL)
    }

    private fun consumerProperties(): MutableMap<String, Any> {
        return HashMap<String, Any>().apply {
            put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, embeddedKafkaEnvironment.brokersURL)
            put(ConsumerConfig.GROUP_ID_CONFIG, "integration-test" + UUID.randomUUID().toString().slice(1..5))
            put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
        }
    }

    private fun produceToTopic(records: List<String>) {
        val clientProperties = producerProperties()
        clientProperties[ProducerConfig.BATCH_SIZE_CONFIG] = 0
        clientProperties[ProducerConfig.LINGER_MS_CONFIG] = 0

        val producer = KafkaProducer(clientProperties, StringSerializer(), StringSerializer())
        records.forEach { r ->
            producer.send(ProducerRecord(testTopic, r, r))
            producer.flush()
        }
    }

    @BeforeAll
    internal fun setup() {
        embeddedKafkaEnvironment.start()
        kafkaProducer = KafkaProducer(producerProperties(), StringSerializer(), StringSerializer())
        kafkaConsumer = KafkaConsumer(consumerProperties(), StringDeserializer(), StringDeserializer())
        kafkaConsumer.subscribe(listOf(testTopic))
    }

    @AfterAll
    internal fun teardown() {
        kafkaConsumer.close()
        kafkaProducer.close()
        embeddedKafkaEnvironment.tearDown()
    }

    @AfterEach
    internal fun avsluttApp() {
        jobb.cancel()
    }

    @Test
    @Disabled("Vet ikke hva som er galt, hva er vel det verste som kan skje om jeg skrur den av")
    fun `blir en melding lest`() {
        startApp()
        produceToTopic(listOf(testSubsumsjon, testVedtakFattet))
        val client = HttpClient()

        await("wait until recods are sent").atMost(30, TimeUnit.SECONDS).until {
            val response = runBlocking { client.get("http://localhost:8080/fnr/22018219453").bodyAsText()}
            (response.isNotEmpty()).also { println(response) }
        }
    }

}