package no.nav.helse

import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.consumer.ConsumerConfig
import java.time.Duration
import java.util.*

data class KonsumentKonfig(
    val appNavn: String,
    val kafkaBrokers: List<String>,
    val topic: String,
    val consumerGroup: String,
    private val trustStorePath: String?,
    private val kafkaKeyStorePath: String?,
    private val credStorePassword: String?
) {
    internal fun konsumentKonfig(clientId: String, consumerGroup: String) = Properties().apply {
        put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers)
        put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup)
        put(ConsumerConfig.CLIENT_ID_CONFIG, "consumer-$appNavn-$clientId")
        put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
        put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false)
        put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 600000)
        put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1)

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
class Konfig(
    val kafkaKonsumentKonfig: KonsumentKonfig,
    val jdbcUrl: String,
    val dbUsername: String,
    val dbPassword: String,
    val dbMaximumPoolSize: Int,
    val dbConnectionTimeout: Long,
    val dbMaxLifetime: Long,
    val dbInitializationFailTimeout: Long
) {

    companion object {
        fun fromEnv(): Konfig {
            val appNavn = System.getenv("NAIS_APP_NAME")
            return Konfig(
                kafkaKonsumentKonfig = KonsumentKonfig(
                    appNavn = appNavn,
                    kafkaBrokers = System.getenv("KAFKA_BROKERS").split(";"),
                    topic = System.getenv("SUBSUMSJON_TOPIC"),
                    consumerGroup = System.getenv("SUBSUMSJON_CONSUMER_GROUP") ?: "consumer-$appNavn-v5",
                    trustStorePath = System.getenv("KAFKA_TRUSTSTORE_PATH"),
                    kafkaKeyStorePath = System.getenv("KAFKA_KEYSTORE_PATH"),
                    credStorePassword = System.getenv("KAFKA_CREDSTORE_PASSWORD")
                ),

                jdbcUrl = String.format(
                    "jdbc:postgresql://%s:%s/%s",
                    System.getenv("DATABASE_HOST"),
                    System.getenv("DATABASE_PORT"),
                    System.getenv("DATABASE_DATABASE")
                ),
                dbUsername = System.getenv("DATABASE_USERNAME") ?: "",
                dbPassword = System.getenv("DATABASE_PASSWORD") ?: "",
                dbMaximumPoolSize = 1,
                dbConnectionTimeout = Duration.ofSeconds(30).toMillis(),
                dbMaxLifetime = Duration.ofMinutes(30).toMillis(),
                dbInitializationFailTimeout = Duration.ofMinutes(1).toMillis()
            )
        }
    }
}
