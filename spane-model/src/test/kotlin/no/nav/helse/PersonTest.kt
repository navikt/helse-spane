package no.nav.helse

import no.nav.helse.TestHjelper.Companion.lagSubsumsjon
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.fail
import org.opentest4j.AssertionFailedError
import java.time.ZonedDateTime
import java.util.*
import java.util.random.RandomGeneratorFactory.all


internal class PersonTest {

    val person = Person("1234567890")
    lateinit var sykmeldingUUID: UUID
    lateinit var søknadUUID: UUID
    lateinit var vedtaksperiodeUUID: UUID

    @Test
    fun `håndter subsumsjon`() {
        sendSykmeldingSubsumsjon()
        sendSøknadSubsumsjon()
        sendVedtakSubsumsjon()
        assertSporing(0, sykmeldingUUID)
        assertSporing(1, sykmeldingUUID, søknadUUID)
        assertSporing(2, sykmeldingUUID, søknadUUID, vedtaksperiodeUUID)
    }

    private fun assertSporing(subsumsjon: Int, vararg uuids: UUID) {
        val sporing = (person.inspektør.vedtaksperioder[0].subsumsjoner[subsumsjon]["sporing"] as Map<*, *>)
        uuids.forEach {
            if(!sporing.values.contains(it)){
                fail("Sporing inneholder ikke forventet uuid: expected: $it actual: $sporing")
            }
        }
    }

    fun sendSykmeldingSubsumsjon() {
        sykmeldingUUID = UUID.randomUUID()
        val sykmeldingSubsumsjon = lagSubsumsjon(sporing = mapOf("sykmelding" to sykmeldingUUID))
        person.håndter(sykmeldingSubsumsjon)
    }

    fun sendSøknadSubsumsjon() {
        søknadUUID = UUID.randomUUID()
        val subsumsjon = lagSubsumsjon(
            sporing = mapOf(
                "sykmelding" to sykmeldingUUID,
                "soknad" to søknadUUID
            )
        )
        person.håndter(subsumsjon)
    }

    fun sendVedtakSubsumsjon() {
        vedtaksperiodeUUID = UUID.randomUUID()
        val subsumsjon = lagSubsumsjon(
            sporing = mapOf(
                "sykmelding" to sykmeldingUUID,
                "soknad" to søknadUUID,
                "vedtaksperiode" to vedtaksperiodeUUID
            )
        )
        person.håndter(subsumsjon)
    }

}

private val Person.inspektør: TestVisitor
    get() {
        val testVisitor = TestVisitor()
        this.accept(testVisitor)
        return testVisitor
    }

class TestVisitor : PersonVisitor {
    data class TestVedtaksperiode(val subsumsjoner: MutableList<Map<String, Any?>>)

    lateinit var fødselsnummer: String
    val vedtaksperioder: MutableList<TestVedtaksperiode> = mutableListOf()

    override fun preVisitPerson(fødselsnummer: String) {
        this.fødselsnummer = fødselsnummer
    }

    override fun preVisitSubsumsjoner() {
        vedtaksperioder.add(TestVedtaksperiode(mutableListOf()))

    }

    override fun visitSubsumsjon(
        id: String,
        versjon: String,
        eventName: String,
        kilde: String,
        versjonAvKode: String,
        fødselsnummer: String,
        sporing: Map<String, Any>,
        tidsstempel: ZonedDateTime,
        lovverk: String,
        lovverksversjon: String,
        paragraf: String,
        ledd: Int?,
        punktum: Int?,
        bokstav: String?,
        input: Map<String, Any>,
        output: Map<String, Any>,
        utfall: String
    ) {
        vedtaksperioder.last().subsumsjoner.add(
            mapOf(
                "id" to id,
                "versjon" to versjon,
                "eventName" to eventName,
                "kilde" to kilde,
                "versjonAvKode" to versjonAvKode,
                "fødselsnummer" to fødselsnummer,
                "sporing" to sporing,
                "tidsstempel" to tidsstempel,
                "lovverk" to lovverk,
                "lovverksversjon" to lovverksversjon,
                "paragraf" to paragraf,
                "ledd" to ledd,
                "punktum" to punktum,
                "bokstav" to bokstav,
                "input" to input,
                "output" to output,
                "utfall" to utfall,
            )
        )
    }


}
