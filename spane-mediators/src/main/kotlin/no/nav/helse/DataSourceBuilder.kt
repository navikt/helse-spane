package no.nav.helse

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway

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

    internal fun getDataSource() = HikariDataSource(hikariConfig).also { migrate(it) }

    private fun migrate(dataSource: HikariDataSource, initSql: String = "") =
        Flyway.configure()
            .dataSource(dataSource)
            .baselineOnMigrate(true)
            .initSql(initSql)
            .load()
            .migrate()


}
