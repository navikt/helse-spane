package no.nav.helse

import org.junit.jupiter.api.fail
import java.time.ZonedDateTime
import java.util.*

abstract class AbstractPersonTest {
    val person = Person("1234567890")
    lateinit var sykmeldingUUID: UUID
    lateinit var søknadUUID: UUID
    lateinit var vedtaksperiodeUUID: UUID

    internal fun assertMap(subsumsjon: Int, forventetMap: Map<String, Any>, key: String){
        val input = (person.inspektør.vedtaksperioder[0].subsumsjoner[subsumsjon][key] as Map<*, *>)
        if (input.values != forventetMap.values){
            fail("Input inneholder ikke forventede feilter. Expected: $forventetMap Actual: $input ")
        }
    }

    internal fun assertString(subsumsjon: Int, forventetString: String, key: String) {
        val input = (person.inspektør.vedtaksperioder[0].subsumsjoner[subsumsjon][key])
        println(input)
        if (input != forventetString){
            fail("Input inneholder ikke forventede feilter. Expected: $forventetString Actual: $input ")
        }
    }

    internal fun assertSporing(subsumsjon: Int, vararg uuids: UUID) {
        val sporing = (person.inspektør.vedtaksperioder[0].subsumsjoner[subsumsjon]["sporing"] as Map<*, *>)
        uuids.forEach {
            if (!sporing.values.contains(it)) {
                fail("Sporing inneholder ikke forventet uuid: expected: $it actual: $sporing")
            }
        }
    }

    internal fun sendSubsumsjon() {
        sykmeldingUUID = UUID.randomUUID()
        val input = mapOf("skjæringtidspunkt" to "2018-01-01")
        val output = mapOf("antallOpptjeningsdager" to "28")
        val subsumsjon = TestHjelper.lagSubsumsjon(sporing = mapOf("sykmelding" to sykmeldingUUID), input = input, output = output)
        person.håndter(subsumsjon)
    }
    internal fun sendSykmeldingSubsumsjon() {
        sykmeldingUUID = UUID.randomUUID()
        val sykmeldingSubsumsjon = TestHjelper.lagSubsumsjon(sporing = mapOf("sykmelding" to sykmeldingUUID))
        person.håndter(sykmeldingSubsumsjon)
    }

    internal fun sendSøknadSubsumsjon() {
        søknadUUID = UUID.randomUUID()
        val subsumsjon = TestHjelper.lagSubsumsjon(
            sporing = mapOf(
                "sykmelding" to sykmeldingUUID,
                "soknad" to søknadUUID
            )
        )
        person.håndter(subsumsjon)
    }

    internal fun sendVedtakSubsumsjon() {
        vedtaksperiodeUUID = UUID.randomUUID()
        val subsumsjon = TestHjelper.lagSubsumsjon(
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

private val Subsumsjon.inspektør: TestVisitor
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