package no.nav.helse.spane.kafka

import io.prometheus.client.Counter
import net.logstash.logback.argument.StructuredArguments.kv
import no.nav.helse.logger
import no.nav.helse.spane.SubsumsjonMediator
import no.nav.helse.spane.VedtakFattetMediator
import no.nav.helse.spane.VedtaksperiodeForkastetMediator
import no.nav.helse.spane.db.PersonRepository
import no.nav.helse.spane.objectMapper
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.errors.WakeupException
import java.time.Duration
import java.util.concurrent.atomic.AtomicBoolean

class Konsument(
    private val topic: String,
    private val konsument: KafkaConsumer<String, String>,
    private val personRepository: PersonRepository,
    private val subsumsjonMediator: SubsumsjonMediator = SubsumsjonMediator(personRepository),
    private val vedtakFattetMediator: VedtakFattetMediator = VedtakFattetMediator(personRepository),
    private val vedtaksperiodeForkastetMediator: VedtaksperiodeForkastetMediator = VedtaksperiodeForkastetMediator(
        personRepository
    ),
) {
    private val running = AtomicBoolean(false)

    companion object {
        private val meldingerLestCounter = Counter.build().labelNames("handled")
            .name("spane_meldinger_lest").help("Total messages handled").register()
    }

    private fun consumeMessages() {
        var lastException: Exception? = null
        try {
            konsument.subscribe(listOf(topic))
            while (running.get()) {
                konsument.poll(Duration.ofSeconds(5)).onEach {
                    val melding = objectMapper.readTree(it.value())
                    logger.info("Behandler melding, {} ", kv("id", melding.path("id").asText()))
                    if (subsumsjonMediator.håndterer(melding)) {
                        subsumsjonMediator.håndterSubsumsjon(melding)
                        meldingerLestCounter.labels("subsumsjon").inc()
                    } else if (vedtakFattetMediator.håndterer(melding)) {
                        vedtakFattetMediator.håndterVedtakFattet(melding)
                        meldingerLestCounter.labels("vedtak_fattet").inc()

                    } else if (vedtaksperiodeForkastetMediator.håndtererForkastetVedtak(melding)) {
                        vedtaksperiodeForkastetMediator.håndterForkastetVedtaksperiode(melding)
                        meldingerLestCounter.labels("vedtaksperiode_forkastet").inc()
                    } else
                        meldingerLestCounter.labels("melding_ikke_lest").inc()
                    logger.info("Melding {} behandlet", kv("id", melding.path("id").asText()))
                }
                konsument.commitSync()

            }
        } catch (err: WakeupException) {
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

    fun stop() {
        running.set(false)
        konsument.wakeup()
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
