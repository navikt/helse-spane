package no.nav.helse

import no.nav.helse.Subsumsjon.Companion.erRelevant

internal class Vedtaksperiode(
    private val sykmeldingsID: String,
    private val subsumsjoner: MutableList<Subsumsjon>

) {


    companion object {
        fun MutableList<Vedtaksperiode>.finnAlle(subsumsjon: Subsumsjon) {

            this.forEach {
                it.subsumsjoner.erRelevant(subsumsjon)

                /*
                if (subsumsjon.sjekkID(it.sykmeldingsID)) {
                    it.subsumsjoner.add(subsumsjon)
                    return
                }

                 */

            }


        }

    }
}
