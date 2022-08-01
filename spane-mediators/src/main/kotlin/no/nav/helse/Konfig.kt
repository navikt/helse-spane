package no.nav.helse

import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.consumer.ConsumerConfig
import java.time.Duration
import java.util.*

class Konfig(
    val appNavn: String,
    val kafkaBrokers: List<String>,
    val topic: String,
    val consumerGroup: String,
    val jdbcUrl: String,
    val dbUsername: String,
    val dbPassword: String,
    val dbMaximumPoolSize: Int,
    val dbConnectionTimeout: Long,
    val dbMaxLifetime: Long,
    val dbInitializationFailTimeout: Long,
    private val trustStorePath: String?,
    private val kafkaKeyStorePath: String?,
    private val credStorePassword: String?
) {

    companion object {
        fun fromEnv(): Konfig {
            val appNavn = System.getenv("NAIS_APP_NAME")
            return Konfig(
                appNavn,
                System.getenv("KAFKA_BROKERS").split(";"),
                System.getenv("SUBSUMSJON_TOPIC"),
                System.getenv("SUBSUMSJON_CONSUMER_GROUP") ?: "consumer-$appNavn-v2",

                System.getenv("DATABASE_URL") ?: String.format(
                    "jdbc:postgresql://%s:%s/%s",
                    System.getenv("DATABASE_HOST"),
                    System.getenv("DATABASE_PORT"),
                    System.getenv("DATABASE_DATABASE")
                ),
                System.getenv("DATABASE_USERNAME") ?: "",
                System.getenv("DATABASE_PASSWORD") ?: "",
                1,
                Duration.ofSeconds(30).toMillis(),
                Duration.ofMinutes(30).toMillis(),
                Duration.ofMinutes(1).toMillis(),
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