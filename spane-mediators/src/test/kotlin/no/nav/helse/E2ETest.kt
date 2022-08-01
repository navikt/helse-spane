package no.nav.helse

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import no.nav.common.KafkaEnvironment
import no.nav.helse.TestHjelper.Companion.melding
import no.nav.helse.spane.db.PersonPostgresRepository
import no.nav.helse.spane.db.PersonRepository
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
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap


@TestInstance(Lifecycle.PER_CLASS)
internal class E2ETest : AbstraktDatabaseTest() {

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

    private var teller = 0


    fun testHåndterSubsumsjon(input: String, postgresRepository: PersonRepository) {
        logger.info("fikk melding")
        if (input == melding) {
            teller++
        }
    }

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
            Application(konfig, ::ktorServer, ::testHåndterSubsumsjon, personRepository).startBlocking()
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
    //@Disabled("endret konfig, vet ikke hvorfor det feiler")
    fun `blir en melding lest`() {
        startApp()
        produceToTopic(listOf(melding))

        await("wait until recods are sent").atMost(20, TimeUnit.SECONDS).until {
            teller >= 1
        }
    }

}