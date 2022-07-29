package no.nav.helse

import org.junit.jupiter.api.Test

internal class DatabaseTest: AbstraktDatabaseTest() {

    @Test
    fun vilkårsgrunnlag() {
        hentPerson()
    }

}