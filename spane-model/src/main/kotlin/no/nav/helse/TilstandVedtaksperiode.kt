package no.nav.helse

import org.apache.kafka.common.protocol.types.Schema.Visitor


interface TilstandVedtaksperiode{

    fun hørerTil(vedtaksperiodeId: String?): Boolean

    fun accept(visitor: VedtakFattetVisitor) {}

    fun accept(visitor: VedtaksperiodeForkastetVisitor) {}

}