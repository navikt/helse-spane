package no.nav.helse

import org.junit.jupiter.api.fail
import java.time.ZonedDateTime
import java.util.*

abstract class AbstractPersonTest {
    private val person = Person("1234567890")
    lateinit var sykmeldingUUID: String
    lateinit var søknadUUID: String
    lateinit var vedtaksperiodeUUID: String

    internal fun assertMap(subsumsjon: Int, forventetMap: Map<String, Any>, key: String) {
        val input = (person.inspektør.vedtaksperioder[0].subsumsjoner[subsumsjon][key] as Map<*, *>)
        if (input.values != forventetMap.values) {
            fail("Input inneholder ikke forventede feilter. Expected: $forventetMap Actual: $input ")
        }
    }

    internal fun assertString(subsumsjon: Int, forventetString: String, key: String) {
        val string = person.inspektør.vedtaksperioder[0].subsumsjoner[subsumsjon][key]
        if (string != forventetString) {
            fail("Input inneholder ikke forventede feilter. Expected: $forventetString Actual: $string ")
        }
    }

    internal fun assertSporing(vararg uuids: String, vedtaksperiodeIndeks: Int = 0) {
        val sporing = (person.inspektør.vedtaksperioder[vedtaksperiodeIndeks].subsumsjoner[0]["sporing"] as Map<String, List<String>>)

        uuids.forEach {
            if (!sporing.values.flatten().contains(it)) {
                fail("Sporing inneholder ikke forventet uuid: expected: $it actual: $sporing")
            }
        }
    }

    internal fun assertAntallVedtaksPerioder(forventetAntall: Int) {
        if (forventetAntall != person.inspektør.vedtaksperioder.size) {
            fail("ikke forventet antall vedtaksperioder: expected: $forventetAntall actual: ${person.inspektør.vedtaksperioder.size}")
        }
    }

    internal fun assertPseudoVedtaksperiodeLengde(vedtaksperiodeIndeks: Int, antallSubsumsjoner: Int) {
        if (person.inspektør.vedtaksperioder[vedtaksperiodeIndeks].subsumsjoner.size != antallSubsumsjoner) {
            fail("ikke forventet antall subsumsjoner i vedtaksperiode[$vedtaksperiodeIndeks]: expected: $antallSubsumsjoner actual: ${person.inspektør.vedtaksperioder.size}")
        }
    }

    internal fun assertTidsstempel(subsumsjon: Int, forventetTidsstempel: ZonedDateTime) {
        val tidsstempel = person.inspektør.vedtaksperioder[0].subsumsjoner[subsumsjon]["tidsstempel"]
        if (tidsstempel != forventetTidsstempel) {
            fail("Input inneholder ikke forventede feilter. Expected: $forventetTidsstempel Actual: $tidsstempel ")
        }
    }

    internal fun sendSubsumsjon(id: String = UUID.randomUUID().toString()) {
        val søknadUUID = UUID.randomUUID().toString()
        val input = mapOf("skjæringtidspunkt" to "2018-01-01")
        val output = mapOf("antallOpptjeningsdager" to "28")
        val subsumsjon = TestHjelper.lagSubsumsjon(
            id = id,
            sporing = mapOf("sykmelding" to listOf(søknadUUID)),
            input = input,
            output = output
        )
        person.håndter(subsumsjon)
    }

    internal fun sendSykmeldingSubsumsjon(antall: Int = 1) {
        sykmeldingUUID = UUID.randomUUID().toString()
        for (i in 1..antall) {
            val sykmeldingSubsumsjon =
                TestHjelper.lagSubsumsjon(sporing = mapOf("sykmelding" to listOf(sykmeldingUUID)))
            person.håndter(sykmeldingSubsumsjon)
        }
    }

    internal fun sendSøknadSubsumsjon() {
        søknadUUID = UUID.randomUUID().toString()
        val subsumsjon = TestHjelper.lagSubsumsjon(
            sporing = mapOf(
                "sykmelding" to listOf(sykmeldingUUID),
                "soknad" to listOf(søknadUUID)
            )
        )
        person.håndter(subsumsjon)
    }

    internal fun sendVedtakSubsumsjon() {
        vedtaksperiodeUUID = UUID.randomUUID().toString()
        val subsumsjon = TestHjelper.lagSubsumsjon(
            sporing = mapOf(
                "sykmelding" to listOf(sykmeldingUUID),
                "soknad" to listOf(søknadUUID),
                "vedtaksperiode" to listOf(vedtaksperiodeUUID)
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

    private lateinit var fødselsnummer: String
    val vedtaksperioder: MutableList<TestVedtaksperiode> = mutableListOf()

    override fun preVisitPerson(fødselsnummer: String) {
        this.fødselsnummer = fødselsnummer
    }

    override fun preVisitSubsumsjoner(skjæringstidspunkt: String, orgnummer: String) {
        vedtaksperioder.add(TestVedtaksperiode(mutableListOf()))

    }


    override fun visitSubsumsjon(
        id: String,
        versjon: String,
        eventName: String,
        kilde: String,
        versjonAvKode: String,
        fødselsnummer: String,
        sporing: Map<String, List<String>>,
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