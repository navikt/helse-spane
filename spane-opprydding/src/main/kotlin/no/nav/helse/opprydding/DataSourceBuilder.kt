package no.nav.helse.opprydding

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.time.Duration

internal class DataSourceBuilder(env: Map<String, String>) {

    private val gcpProjectId: String = env.required("GCP_TEAM_PROJECT_ID")
    private val databaseRegion: String = env.required("DATABASE_REGION")
    private val databaseInstance: String = env.required("DATABASE_INSTANCE")
    private val databaseUsername: String = env.required("DATABASE_SPANE_OPPRYDDING_USERNAME")
    private val databasePassword: String = env.required("DATABASE_SPANE_OPPRYDDING_PASSWORD")
    private val databaseName: String = env.required("DATABASE_SPANE_OPPRYDDING_DATABASE")

    private val hikariConfig = HikariConfig().apply {
        jdbcUrl = String.format(
            "jdbc:postgresql:///%s?%s&%s",
            databaseName,
            "cloudSqlInstance=$gcpProjectId:$databaseRegion:$databaseInstance",
            "socketFactory=com.google.cloud.sql.postgres.SocketFactory"
        )

        username = databaseUsername
        password = databasePassword

        maximumPoolSize = 3
        minimumIdle = 1
        initializationFailTimeout = Duration.ofSeconds(10).toMillis()
        connectionTimeout = Duration.ofSeconds(1).toMillis()
        maxLifetime = Duration.ofMinutes(30).toMillis()
        idleTimeout = Duration.ofMinutes(10).toMillis()
    }

    internal val dataSource by lazy { HikariDataSource(hikariConfig) }
}

internal fun Map<String, String>.required(name: String) = requireNotNull(get(name)) { "$name must be set" }
