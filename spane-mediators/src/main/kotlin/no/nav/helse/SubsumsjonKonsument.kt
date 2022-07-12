package no.nav.helse

import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.errors.WakeupException
import org.apache.kafka.common.serialization.StringDeserializer
import java.time.Duration
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class SubsumsjonKonsument (
        private val konfig: Konfig,
        clientId: String = UUID.randomUUID().toString().slice(1..5),
        private val håndterSubsumsjon: (input: String) -> Any?
    ) {

    private val konsument = KafkaConsumer(konfig.konsumentKonfig(clientId, konfig.consumerGroup), StringDeserializer(), StringDeserializer())
    private val running = AtomicBoolean(false)

    private fun consumeMessages() {
        var lastException: Exception? = null
        try {
            konsument.subscribe(listOf(konfig.topic))
            while (running.get()) {
                konsument.poll(Duration.ofSeconds(1)).onEach {
                    håndterSubsumsjon(it.value())
                }
            }
        } catch (err: WakeupException) {
            // throw exception if we have not been told to stop
            if (running.get()) throw err
        } catch (err: Exception) {
            lastException = err
            throw err
        } finally {
            closeResources(lastException)
        }
    }



    fun start() {
        logger.info("Starter Spane")
        if (running.getAndSet(true)) return logger.info("Spane kjører allerede")

        consumeMessages()
    }

    private fun closeResources(lastException: Exception?) {
        if (running.getAndSet(false)) {
            logger.warn("Slutter å konsumere meldinger pga en feil: ", lastException)
        } else {
            logger.info("Mottok stopp signal, slutter å konsumere meldinger")
        }
        tryAndLog(konsument::unsubscribe)
        tryAndLog(konsument::close)
    }

    private fun tryAndLog(block: () -> Unit) {
        try {
            block()
        } catch (err: Exception) {
            logger.error(err.message, err)
        }
    }
}