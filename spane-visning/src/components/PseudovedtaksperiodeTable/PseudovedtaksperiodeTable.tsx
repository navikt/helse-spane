import React from "react";
import {PersonDto, VedtaksperiodeDto} from "../../types";
import {Table} from "@navikt/ds-react";
import VedtaksperiodeExpandableRow from "./VedtaksperiodeExpandableRow";
import {filtrerPseudovedtaksperioder} from "../../utlis"

interface Props {
    valgte: string[]
    fraDato: string
    tilDato: string
    person: PersonDto | undefined
    anonymisert: Boolean
}

function PseudovedtaksperiodeTable(props: Props) {
    const {valgte, fraDato, tilDato, person, anonymisert} = props;

    return (
        <main className="table-container">
            <Table size="medium">
                { person && <Table.Body>
                    {filtrerPseudovedtaksperioder(person, valgte, fraDato, tilDato).map((vedtaksperiode: VedtaksperiodeDto, vedtaksperiodeIdx) =>
                        (
                            <VedtaksperiodeExpandableRow key={vedtaksperiodeIdx} vedtaksperiode={vedtaksperiode} anonymisert={anonymisert}/>
                        ))}
                </Table.Body>}
            </Table>
        </main>
    );
}

export default PseudovedtaksperiodeTable;
