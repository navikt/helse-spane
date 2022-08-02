import React from "react";
import {Table} from "@navikt/ds-react";
import {PersonDto, VedtaksperiodeDto} from "../types";
import VedtaksperiodeExpandableRow from "./VedtaksperiodeExpandableRow";
import vedtaksperiodeExpandableRow from "./VedtaksperiodeExpandableRow";

interface Props {
    person: PersonDto;
    valgte: string[];
    fraDato: string;
    tilDato: string;
}

export default function VedtaksperiodeTableBody(props: Props) {
    const {person, valgte, fraDato, tilDato} = props;
    console.log(fraDato)
    function filtrerVedtaksperioder() {
        let vedtaksperioder = [...person.vedtaksperioder]
        if (valgte.length > 0) {
            vedtaksperioder = vedtaksperioder.filter(vedtaksperiode =>
                valgte.includes(vedtaksperiode.orgnummer))
        }
        if (fraDato !== "" || tilDato !== "") vedtaksperioder = vedtaksperioder.filter(vedtaksperiode => vedtaksperiode.skjæringstidspunkt !== "ukjent")
        if (fraDato !== "") {
            let dato = new Date(fraDato)
            vedtaksperioder = vedtaksperioder.filter(vedtaksperiode => new Date(vedtaksperiode.skjæringstidspunkt) >= dato)
        }
        if (tilDato !== "") {
            let dato = new Date(tilDato)
            vedtaksperioder = vedtaksperioder.filter(vedtaksperiode => new Date(vedtaksperiode.skjæringstidspunkt) <= dato)
        }
        return vedtaksperioder
    }

    return (
        <Table.Body>
            {filtrerVedtaksperioder().map((vedtaksperiode: VedtaksperiodeDto, vedtaksperiodeIdx) =>
                (
                    <VedtaksperiodeExpandableRow key={vedtaksperiodeIdx} vedtaksperiode={vedtaksperiode}/>
                ))}
        </Table.Body>
    );
}
