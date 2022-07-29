package no.nav.helse

import no.nav.helse.SpaneDataSource.migratedDb
import no.nav.helse.spane.db.PersonPostgresRepository
import org.junit.jupiter.api.BeforeAll
import javax.sql.DataSource

internal abstract class AbstraktDatabaseTest {
    private lateinit var dataSource: DataSource
    private lateinit var personRepository: PersonPostgresRepository

    @BeforeAll
    internal fun setupAll() {
        dataSource = migratedDb
        personRepository = PersonPostgresRepository(dataSource)

    }

    protected fun hentPerson(){
        val person = personRepository.hentPerson("10877799145")
        if (person != null) {
            println(person.json)
        }
    }



}