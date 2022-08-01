import React from "react";
import {Table} from "@navikt/ds-react";
import {PersonDto, VedtaksperiodeDto} from "../types";
import VedtaksperiodeExpandableRow from "./VedtaksperiodeExpandableRow";

interface Props {
    person: PersonDto;
    valgte: string[]
}

export default function VedtaksperiodeTableBody(props: Props) {
    const {person, valgte} = props;

    return (
        <Table.Body>
            {valgte.length > 0

                ? person.vedtaksperioder.filter(vedtaksperiode =>
                    valgte.includes(vedtaksperiode.orgnummer)
                ).map(
                    (vedtaksperiode: VedtaksperiodeDto, vedtaksperiodeIdx) =>
                        (
                            <VedtaksperiodeExpandableRow key={vedtaksperiodeIdx} vedtaksperiode={vedtaksperiode}/>
                        )
                ) : person.vedtaksperioder.map(
                    (vedtaksperiode: VedtaksperiodeDto, vedtaksperiodeIdx) =>
                        (
                            <VedtaksperiodeExpandableRow key={vedtaksperiodeIdx} vedtaksperiode={vedtaksperiode}/>
                        )
                )
            }
        </Table.Body>
    );
}
