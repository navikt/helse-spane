package no.nav.helse

import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.consumer.ConsumerConfig
import java.util.*

class Konfig(
    val appNavn: String,
    val kafkaBrokers: List<String>,
    val topic: String,
    val consumerGroup: String,
    private val trustStorePath: String?,
    private val kafkaKeyStorePath: String?,
    private val credStorePassword: String?,
) {

    companion object{
         fun fromEnv(): Konfig {
             val appNavn = System.getenv("NAIS_APP_NAME")
             return Konfig(
                 appNavn,
                 System.getenv("KAFKA_BROKERS").split(";"),
                 System.getenv("SUBSUMSJON_TOPIC"),
                 System.getenv("SUBSUMSJON_CONSUMER_GROUP") ?: "consumer-$appNavn-v2",
                 System.getenv("KAFKA_TRUSTSTORE_PATH"),
                 System.getenv("KAFKA_KEYSTORE_PATH"),
                 System.getenv("KAFKA_CREDSTORE_PASSWORD")
             )
         }
    }


    internal fun konsumentKonfig(clientId: String, consumerGroup: String) = Properties().apply {
        put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers)
        put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup)
        put(ConsumerConfig.CLIENT_ID_CONFIG, "consumer-$appNavn-$clientId")
        put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
        put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false)

        if (kafkaKeyStorePath != null) {
            this += sslConfig()
        }
    }

    private fun sslConfig() = Properties().apply {
        logger.info("SSL config enabled")
        put("security.protocol", "SSL")
        put("ssl.truststore.location", trustStorePath!!)
        put("ssl.truststore.password", credStorePassword!!)
        put("ssl.keystore.type", "PKCS12")
        put("ssl.keystore.location", kafkaKeyStorePath!!)
        put("ssl.keystore.password", credStorePassword)
        put("ssl.key.password", credStorePassword)
    }
}