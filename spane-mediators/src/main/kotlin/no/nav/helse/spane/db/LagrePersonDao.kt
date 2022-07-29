package no.nav.helse.spane.db

import java.util.UUID
import javax.sql.DataSource
import kotliquery.TransactionalSession
import kotliquery.queryOf
import kotliquery.sessionOf
import no.nav.helse.Person
import org.intellij.lang.annotations.Language

internal class LagrePersonDao(private val dataSource: DataSource) {
//
//
//    fun lagrePerson(person: Person) {
//        lagrePerson(
//            personJson = person.json()
//        )
//    }
//
//    fun personAvstemt(hendelse: Avstemming) {
//        @Language("PostgreSQL")
//        val statement = "UPDATE unike_person SET sist_avstemt = now() WHERE fnr = :fnr"
//        sessionOf(dataSource).use { session ->
//            session.run(queryOf(statement, mapOf(
//                "fnr" to hendelse.fødselsnummer().toLong()
//            )).asExecute)
//        }
//    }
//
//    private fun lagrePerson(personJson: String) {
//        sessionOf(dataSource).use { session ->
//            session.transaction {
//                opprettNyPerson(it, personJson, vedtak)
//            }
//        }.also {
//            PostgresProbe.personSkrevetTilDb()
//        }
//    }
//
//    private fun opprettNyPerson(session: TransactionalSession, fødselsnummer: Fødselsnummer, aktørId: String, skjemaVersjon: Int, meldingId: UUID, personJson: String, vedtak: Boolean) {
//        @Language("PostgreSQL")
//        val statement = "INSERT INTO unike_person (fnr, aktor_id) VALUES (:fnr, :aktor) ON CONFLICT DO NOTHING"
//        session.run(queryOf(statement, mapOf(
//            "fnr" to fødselsnummer.toLong(),
//            "aktor" to aktørId.toLong()
//        )).asExecute)
//        slettEldrePersonversjon(session, fødselsnummer)
//        opprettNyPersonversjon(session, fødselsnummer, aktørId, skjemaVersjon, meldingId, personJson, vedtak)
//    }
//
//    private fun slettEldrePersonversjon(session: Session, fødselsnummer: Fødselsnummer) {
//        @Language("PostgreSQL")
//        val statement = """
//            DELETE FROM person
//            WHERE vedtak = false AND fnr = ?
//        """
//        session.run(queryOf(statement, fødselsnummer.toLong()).asExecute)
//    }
//
//    private fun opprettNyPersonversjon(session: Session, fødselsnummer: Fødselsnummer, aktørId: String, skjemaVersjon: Int, meldingId: UUID, personJson: String, vedtak: Boolean) {
//        @Language("PostgreSQL")
//        val statement = """
//            INSERT INTO person (aktor_id, fnr, skjema_versjon, melding_id, data, vedtak)
//            VALUES (?, ?, ?, ?, ?, ?)
//        """
//        session.run(queryOf(statement, aktørId.toLong(), fødselsnummer.toLong(), skjemaVersjon, meldingId, personJson, vedtak).asExecute)
//    }
}
