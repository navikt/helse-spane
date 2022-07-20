import {SubsumsjonDto} from "../types";
import React from "react";
import byggStringRekursivt from "./utlis";

interface Props {
    subsumsjon: SubsumsjonDto
}


export default function UtvidetTableInnhold(props: Props) {
    const {subsumsjon} = props

    return (
        <div className="table-row-expanded-content-container">
            <div className="table-row-expanded-content-column">
                <div>
                    <b>Output: </b>
                    {subsumsjon.output && byggStringRekursivt(subsumsjon.output).split("\n").map((s, key) =>
                        <p key={key}>{s}</p>)}
                </div>
                <div>
                    <b>Input: </b>
                    {subsumsjon.input && byggStringRekursivt(subsumsjon.input).split("\n").map((s, key) =>
                        <p key={key}>{s}</p>)}
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
                    subsumsjon.sporing && byggStringRekursivt(subsumsjon.sporing).split("\n").map((s, key) =>
                        <p key={key}>{s}</p>)
                }
                </div>
            </div>
        </div>
    )
}