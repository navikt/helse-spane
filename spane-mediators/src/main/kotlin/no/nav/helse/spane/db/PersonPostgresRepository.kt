package no.nav.helse.spane.db

import kotliquery.Query
import kotliquery.queryOf
import kotliquery.sessionOf
import javax.sql.DataSource

internal class PersonPostgresRepository(private val dataSource: DataSource) : PersonRepository {

    override fun hentPerson(fnr: String): SerialisertPerson? {
        val statement = "SELECT data FROM person WHERE fnr = ?"
        return hentPerson(queryOf(statement, fnr))
    }

    override fun lagre(json: String, fnr: String) {
        val statement = "INSERT INTO person (fnr, data) VALUES (?, ?::JSON)" +
                " ON CONFLICT (fnr)" +
                " DO" +
                " UPDATE SET data = EXCLUDED.data"
        sessionOf(dataSource).use { session ->
            session.run(queryOf(statement, fnr, json).asUpdate)
        }
    }

    private fun hentPerson(query: Query) :SerialisertPerson? = sessionOf(dataSource).use { session ->
        session.run(query.map {
            SerialisertPerson(it.string("data"))
        }.asSingle)
    }

}