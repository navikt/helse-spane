import React from 'react';
import {Table} from "@navikt/ds-react";
import {PersonDto, SubsumsjonDto, VedtaksperiodeDto} from "../types";
import UtvidetTableInnhold from "./UtvidetTableInnhold";

interface Props {
    person: PersonDto
}


export default function SubsumsjonTableBody(props: Props) {
    const {person} = props

    return (
        <Table.Body>
            {person.vedtaksperioder.map(
                (vedtaksperiode: VedtaksperiodeDto, vedtaksperiodeIdx) => {
                    return vedtaksperiode.subsumsjoner.map(
                        (subsumsjon: SubsumsjonDto, subsumsjonIdx: number) => {
                            return (
                                <Table.ExpandableRow
                                    key={subsumsjonIdx}
                                    content={
                                        <UtvidetTableInnhold subsumsjon={subsumsjon}/>
                                    }
                                    togglePlacement="right">
                                    <Table.HeaderCell scope="row">
                                        {vedtaksperiodeIdx + 1}
                                    </Table.HeaderCell>
                                    <Table.HeaderCell scope="row">
                                        {subsumsjon.paragraf}
                                    </Table.HeaderCell>
                                    <Table.DataCell>
                                        {subsumsjon.fødselsnummer}
                                    </Table.DataCell>
                                    <Table.DataCell>{subsumsjon.utfall}</Table.DataCell>
                                    <Table.DataCell>
                                        {subsumsjon.tidsstempel}
                                    </Table.DataCell>
                                </Table.ExpandableRow>
                            );
                        }
                    );
                }
            )}
        </Table.Body>
    )
}

