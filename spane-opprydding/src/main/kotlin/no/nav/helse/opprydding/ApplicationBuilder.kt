package no.nav.helse.opprydding

import no.nav.helse.rapids_rivers.RapidApplication

internal class ApplicationBuilder(env: Map<String, String>) {

    // Sikrer at appen kun kan startes i dev-gcp
    private val dataSourceBuilder = env["NAIS_CLUSTER_NAME"].let { clusterName ->
        if (clusterName != "dev-gcp") throw IllegalArgumentException("env variable NAIS_CLUSTER_NAME has an unsupported value. Supported value: dev-gcp.")
        DataSourceBuilder(env)
    }

    private val rapidsConnection = RapidApplication.create(env)

    init {
        SlettPersonRiver(rapidsConnection, SlettPersonDao(dataSourceBuilder.dataSource))
    }

    internal fun start() = rapidsConnection.start()
}
