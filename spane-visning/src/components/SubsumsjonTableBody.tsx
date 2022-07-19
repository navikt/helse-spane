import React from 'react';
import {Table} from "@navikt/ds-react";
import {PersonDto, SubsumsjonDto, VedtaksperiodeDto} from "../types";

interface Props {
    person: PersonDto
}


export default function SubsumsjonTableBody(props: Props) {
    const {person} = props

    function byggStringRekursivt(innhold: any, resultat: string = "") {
        if (innhold === null) {
            resultat += "null\n"
        } else if (typeof innhold === "string") {
            resultat += innhold + "\n"
        } else if (typeof innhold === "boolean") {
            resultat += innhold + "\n"
        } else if (typeof innhold === "number") {
            resultat += innhold.toString() + "\n"
        } else if (Array.isArray(innhold)) {
            for (const elem of innhold) {
                resultat += byggStringRekursivt(elem) + "\n"
            }
        } else if (typeof innhold === "object") {
            for (const [key, value] of Object.entries(innhold)) {
                if (typeof value === "object") {
                    resultat += key + ":\n" + byggStringRekursivt(value) + "\n"
                    //TODO Finn ut hvorfor det blir en ekstra \n hvis value er en liste
                    // eller hvorfor null blir nylinje
                } else {
                    resultat += key + ": " + byggStringRekursivt(value) + "\n"
                }
            }
        } else {
            resultat += "****** Ikke gjenkjent type, noe har skjedd feil ******"
        }
        return resultat
    }


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
                                                    <b>Output: </b>
                                                    {subsumsjon.output && byggStringRekursivt(subsumsjon.output).split("\n").map((s) =>
                                                        <p>{s}</p>)}
                                                </div>
                                                <div>
                                                    <b>Input: </b>
                                                    {subsumsjon.input && byggStringRekursivt(subsumsjon.input).split("\n").map((s) =>
                                                        <p>{s}</p>)}
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
                                                    <b>Meldingsid: </b> {subsumsjon.id}
                                                </div>
                                                <div>
                                                    <b>Sporing: </b> {
                                                    subsumsjon.sporing && byggStringRekursivt(subsumsjon.sporing).split("\n").map((s) =>
                                                        <p>{s}</p>)
                                                }
                                                </div>
                                            </div>
                                        </div>
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

