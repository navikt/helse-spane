import {HelpText, Table} from "@navikt/ds-react";
import React, {useState} from "react";
import UtvidetVedtaksperiodeTableInnhold from "./UtvidetVedtaksperiodeTableInnhold";
import {VedtaksperiodeDto} from "../../types";
import "./vedtaksperiodeExpandableRow.css"

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
            <Table.DataCell scope="row">
                <b> {/*vedtaksperiode.ikkeSikkertSkjæringstidspunkt ? "Skjæringstidspunkt*: " : "Skjæringstidspunkt: "*/} </b> {/*vedtaksperiode.skjæringstidspunkt ?? 'ukjent'*/}
            </Table.DataCell>
            <Table.DataCell scope="row">
                <div className="arbeidsgiver-container" >
                        <HelpText title="Hva betyr dette tallet?" id="arbeidsgiver-helptext">
                            Organisasjonsnummer til arbeidsgiver
                        </HelpText>
                    <b>Arbeidsgiver: </b>
                    {!anonymisert ? vedtaksperiode.orgnummer : '***********'}
                </div>
            </Table.DataCell>
            <Table.DataCell scope="row"> <b> Tilstand: </b> {vedtaksperiode.tilstand} </Table.DataCell>

        </Table.ExpandableRow>
    );
}

export default VedtaksperiodeExpandableRow;
