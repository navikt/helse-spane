package no.nav.helse

import com.github.navikt.tbd_libs.test_support.KafkaContainers
import com.github.navikt.tbd_libs.test_support.kafkaTest
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import no.nav.helse.TestHjelper.Companion.testSubsumsjon
import no.nav.helse.TestHjelper.Companion.testVedtakFattet
import no.nav.helse.spane.db.PersonPostgresRepository
import no.nav.helse.spane.kafka.Konsument
import no.nav.helse.spane.ktorServer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.Test
import java.util.*
import java.util.concurrent.TimeUnit

private val kafkaContainer = KafkaContainers.container("spane-kafka")

internal class E2ETest {

    @Test
    fun `blir en melding lest`() = spaneE2E {
        val client = HttpClient()

        await("wait until recods are sent").atMost(30, TimeUnit.SECONDS).until {
            val response = runBlocking { client.get("http://localhost:8080/fnr/22018219453").bodyAsText()}
            (response.isNotEmpty()).also { println(response) }
        }
    }

    private fun spaneE2E(testblokk: () -> Unit) {
        kafkaTest(kafkaContainer) {
            val testDataSource = databaseContainer.nyTilkobling()
            try {
                val personRepository = PersonPostgresRepository(testDataSource.ds)

                send(testSubsumsjon)
                send(testVedtakFattet).get(20, TimeUnit.SECONDS)

                val consumer = KafkaConsumer(Properties().apply {
                    putAll(kafkaContainer.connectionProperties)
                    this[ConsumerConfig.GROUP_ID_CONFIG] = "spane"
                    this[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = false
                    this[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
                }, StringDeserializer(), StringDeserializer())

                val konsument = Konsument(topicnavn, consumer, personRepository)
                val ktor = ktorServer(personRepository)
                val application = Application(konsument, ktor)

                application.start()
                testblokk()
                application.stopBlocking()
            } finally {
                databaseContainer.droppTilkobling(testDataSource)
            }

        }
    }
}