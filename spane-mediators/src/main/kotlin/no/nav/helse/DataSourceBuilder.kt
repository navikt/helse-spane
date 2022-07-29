package no.nav.helse

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

internal class DataSourceBuilder(konfig: Konfig) {

    private val hikariConfig = HikariConfig().apply {
        jdbcUrl = konfig.jdbcUrl
        username = konfig.dbUsername
        password = konfig.dbPassword
        maximumPoolSize = konfig.dbMaximumPoolSize
        connectionTimeout = konfig.dbConnectionTimeout
        maxLifetime = konfig.dbMaxLifetime
        initializationFailTimeout = konfig.dbInitializationFailTimeout
    }

    internal fun getDataSource() = HikariDataSource(hikariConfig)
}
