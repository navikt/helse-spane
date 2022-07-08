package no.nav.helse

import java.util.Date

data class Sykmeldingsperiode(
    private val skjæringstidspunkt : Date,
    private val subsumsjonsmeldinger: List<Subsumsjonsmelding>
)
