package no.nav.helse

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class DatabaseTest: AbstraktDatabaseTest() {

    @Test
    //@Disabled
    fun vilkårsgrunnlag() {
        hentPerson()
    }

}