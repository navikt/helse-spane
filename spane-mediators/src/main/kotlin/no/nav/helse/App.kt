package no.nav.helse

import no.nav.helse.spane.db.PersonPostgresRepository
import no.nav.helse.spane.kafka.Konsument
import no.nav.helse.spane.ktorServer
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.UUID

var logger: Logger = LoggerFactory.getLogger("Spane")
var sikkerlogger: Logger = LoggerFactory.getLogger("tjenestekall")

fun main() {
    val config = Konfig.fromEnv()

    val dataSourceBuilder = DataSourceBuilder(config)
    val personRepository = PersonPostgresRepository(dataSourceBuilder.getDataSource())

    val clientId: String = UUID.randomUUID().toString().slice(1..5)
    val consumer = KafkaConsumer(
        config.kafkaKonsumentKonfig.konsumentKonfig(clientId, config.kafkaKonsumentKonfig.consumerGroup),
        StringDeserializer(),
        StringDeserializer()
    )
    val konsument = Konsument(config.kafkaKonsumentKonfig.topic, consumer, personRepository)
    val ktor = ktorServer(personRepository)

    Application(konsument, ktor).startBlocking()
}

