import React from 'react';
import {Table} from "@navikt/ds-react";
import {PersonDto, SubsumsjonDto, VedtaksperiodeDto} from "../App";

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
                                        <div className="table-row-expanded-content-container">
                                            <div className="table-row-expanded-content-column">
                                                <div>
                                                    <b>Output</b>
                                                </div>
                                                <div>
                                                    <b>Input</b>
                                                </div>
                                                <div>
                                                    <b>Lovverk: </b>
                                                    {subsumsjon.lovverk}
                                                </div>
                                                <div>
                                                    <b>Kilde: </b>
                                                    {subsumsjon.kilde}
                                                </div>
                                            </div>
                                            <div className="table-row-expanded-content-column">
                                                <div>
                                                    <b>Versjon av kode:</b>{" "}
                                                    {subsumsjon.versjonAvKode}
                                                </div>
                                                <div>
                                                    <b>Meldingsid:</b> {subsumsjon.id}
                                                </div>
                                            </div>
                                        </div>
                                    }
                                    togglePlacement="right"
                                >
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

