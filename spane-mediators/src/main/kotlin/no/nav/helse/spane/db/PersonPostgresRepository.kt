package no.nav.helse.spane.db

import kotliquery.Query
import kotliquery.queryOf
import kotliquery.sessionOf
import no.nav.helse.sikkerlogger
import org.intellij.lang.annotations.Language
import javax.sql.DataSource

internal class PersonPostgresRepository(private val dataSource: DataSource) : PersonRepository {

    override fun hentPerson(fnr: String): SerialisertPerson? {
        @Language("PostgreSQL")
        val statement = "SELECT data FROM person WHERE fnr = ?"
        return hentPerson(queryOf(statement, fnr))
    }

    override fun lagre(json: String, fnr: String) {
        @Language("PostgreSQL")
        val statement = "INSERT INTO person (fnr, data) VALUES (?, ?::JSON)" +
                " ON CONFLICT (fnr)" +
                " DO" +
                " UPDATE SET data = EXCLUDED.data"
        sessionOf(dataSource).use { session ->
            session.run(queryOf(statement, fnr, json).asUpdate)
        }
    }

    override fun lagreParagrafkobling(paragraf: String, ledd: Int?, bokstav: String?, punktum: Int?, fnr: String) {
        sikkerlogger.info("tester i personpostgres {}", bokstav)
        @Language("PostgreSQL")
        val statement = "INSERT INTO paragrafsøk (paragraf, ledd, bokstav, punktum) VALUES (?, ?, ?, ?) " +
                " ON CONFLICT" +
                " DO" +
                " NOTHING " +
                " returning id"

        @Language("PostgreSQL")
        val statement2 = "INSERT INTO person_paragrafsøk (fnr, id) VALUES (?, ?)" +
                " ON CONFLICT" +
                " DO" +
                " NOTHING "

        sessionOf(dataSource).use { session ->
            val id = session.run(queryOf(statement, paragraf, ledd, bokstav, punktum).asUpdate)
            session.run(queryOf(statement2, fnr, id).asUpdate)
        }
    }

    private fun hentPerson(query: Query): SerialisertPerson? = sessionOf(dataSource).use { session ->
        session.run(query.map {
            SerialisertPerson(it.string("data"))
        }.asSingle)
    }

}