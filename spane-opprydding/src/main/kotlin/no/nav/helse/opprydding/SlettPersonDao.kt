package no.nav.helse.opprydding

import javax.sql.DataSource
import kotliquery.TransactionalSession
import kotliquery.queryOf
import kotliquery.sessionOf

internal class SlettPersonDao(private val dataSource: DataSource) {
    internal fun slett(fødselsnummer: String) {
        sessionOf(dataSource).use { session ->
            session.transaction {
                it.slettPerson(fødselsnummer)
            }
        }
    }

    private fun TransactionalSession.slettPerson(fødselsnummer: String) {
        val query = "DELETE FROM person WHERE fnr = ?"
        run(queryOf(query, fødselsnummer).asExecute)
    }

}
