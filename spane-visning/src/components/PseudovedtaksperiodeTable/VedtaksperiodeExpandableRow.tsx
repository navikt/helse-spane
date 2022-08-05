import {HelpText, Table} from "@navikt/ds-react";
import React, {useState} from "react";
import UtvidetVedtaksperiodeTableInnhold from "./UtvidetVedtaksperiodeTableInnhold";
import {VedtaksperiodeDto} from "../../types";

interface Props {
    vedtaksperiode: VedtaksperiodeDto;
    anonymisert: Boolean
}

function VedtaksperiodeExpandableRow(props: Props) {
    const {vedtaksperiode, anonymisert} = props;
    const [expand, setExpand] = useState<boolean>(false);
    return (
        <Table.ExpandableRow
            content={<UtvidetVedtaksperiodeTableInnhold subsumsjoner={vedtaksperiode.subsumsjoner}
                                                        anonymisert={anonymisert}/>}
            togglePlacement="right"
            open={expand}
            onClick={() => setExpand(!expand)}
            style={{cursor: "pointer", backgroundColor: "#E6F0FF"}}
        >
            <Table.DataCell scope="row"> <b> Skjæringstidspunkt: </b> {vedtaksperiode.skjæringstidspunkt ?? 'ukjent'}
            </Table.DataCell>
            <Table.DataCell scope="row">
                <HelpText title="Hva betyr dette tallet?" placement="top-start">
                    Organisasjonsnummer til arbeidsgiver
                </HelpText>
                <b>
                    Arbeidsgiver:
                </b>
                {!anonymisert ? vedtaksperiode.orgnummer : '***********'}
            </Table.DataCell>
            <Table.DataCell scope="row"> <b> Tilstand: </b> {vedtaksperiode.tilstand} </Table.DataCell>

        </Table.ExpandableRow>
    );
}

export default VedtaksperiodeExpandableRow;
