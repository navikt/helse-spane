import React from "react";
import {PersonDto, VedtaksperiodeDto} from "../../types";
import {Table} from "@navikt/ds-react";
import VedtaksperiodeExpandableRow from "./VedtaksperiodeExpandableRow";
import {filtrerPseudovedtaksperioder} from "../../utlis"

interface Props {
    valgte: string[]
    setOrgnumre: React.Dispatch<React.SetStateAction<string[]>>
    fraDato: string
    tilDato: string
    setPerson: React.Dispatch<React.SetStateAction<PersonDto | undefined>>
    person: PersonDto | undefined
}

function PseudovedtaksperiodeTable(props: Props) {
    const {valgte, setOrgnumre, fraDato, tilDato, person, setPerson} = props;

    return (
        <main className="table-container">
            <Table size="medium">
                { person && <Table.Body>
                    {filtrerPseudovedtaksperioder(person, valgte, fraDato, tilDato).map((vedtaksperiode: VedtaksperiodeDto, vedtaksperiodeIdx) =>
                        (
                            <VedtaksperiodeExpandableRow key={vedtaksperiodeIdx} vedtaksperiode={vedtaksperiode}/>
                        ))}
                </Table.Body>}
            </Table>
        </main>
    );
}

export default PseudovedtaksperiodeTable;
