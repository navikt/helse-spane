package no.nav.helse.spane.db

import kotliquery.Query
import kotliquery.queryOf
import kotliquery.sessionOf
import javax.sql.DataSource

internal class PersonPostgresRepository(private val dataSource: DataSource) : PersonRepository {

    override fun hentPerson(fnr: String): SerialisertPerson? {
        //TODO Hent noe data fra db

        val statement = "SELECT data FROM person WHERE fnr = ? ORDER BY id DESC LIMIT 1"

        return hentPerson(queryOf(statement))
    }

    private fun hentPerson(query: Query) :SerialisertPerson? = sessionOf(dataSource).use { session ->
        session.run(query.map {
            SerialisertPerson(it.string("data"))
        }.asSingle)
    }




//    fun lagrePerson(person: SerialisertPerson) {
//        @Language("PostgreSQL")
//        val statement = "INSERT INTO person (data) VALUES (oerson) ON CONFLICT DO NOTHING"
//        lagrePerson(queryOf(statement), personJson = person.json)
//    }
//    private fun lagrePerson(query: Query, personJson : String) {
//        sessionOf(dataSource).use { session ->
//            session.run(query.map {
//                    //TODO Legg til data i db
//
//            }.asSingle)
//        }
//    }



}