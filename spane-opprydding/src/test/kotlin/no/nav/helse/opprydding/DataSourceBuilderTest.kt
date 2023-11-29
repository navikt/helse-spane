package no.nav.helse.opprydding

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.BeforeEach
import org.testcontainers.containers.PostgreSQLContainer
import javax.sql.DataSource

internal abstract class DataSourceBuilderTest {
    protected lateinit var dataSource: DataSource
    companion object {
        private val psqlContainer = PostgreSQLContainer<Nothing>("postgres:14").apply {
            withCreateContainerCmdModifier { command -> command.withName("helse-spane") }
            withReuse(true)
            withLabel("app-navn", "spane-opprydding")
            start()
        }
    }

    @BeforeEach
    fun setupDB() {
        dataSource = runMigration(psqlContainer)
    }

    private fun runMigration(psql: PostgreSQLContainer<Nothing>): DataSource {
        val dataSource = HikariDataSource(createHikariConfig(psql))
        Flyway.configure()
            .cleanDisabled(false)
            .dataSource(dataSource)
            .locations("classpath:db/migration")
            .load()
            .also { it.clean() }
            .migrate()
        return dataSource
    }


    private fun createHikariConfig(psql: PostgreSQLContainer<Nothing>) =
        HikariConfig().apply {
            this.jdbcUrl = psql.jdbcUrl
            this.username = psql.username
            this.password = psql.password
            maximumPoolSize = 3
            minimumIdle = 1
            idleTimeout = 10001
            connectionTimeout = 1000
            initializationFailTimeout = 5000
            maxLifetime = 30001
        }
}
