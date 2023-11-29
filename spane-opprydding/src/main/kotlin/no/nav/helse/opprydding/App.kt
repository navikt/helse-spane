package no.nav.helse.opprydding

object App {
    @JvmStatic
    fun main(args: Array<String>) {
        ApplicationBuilder(System.getenv()).start()
    }
}
