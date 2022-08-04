import {SubsumsjonDto} from "../../types";
import {FileContent} from "@navikt/ds-icons";
import React, {useEffect, useState} from "react";
import {byggStringRekursivt} from "../../utlis";
import {ReadMore} from "@navikt/ds-react";

interface Props {
    subsumsjon: SubsumsjonDto;
    anonymisert: Boolean
}

export default function UtvidetSubsumsjonTableInnhold(props: Props) {
    const {subsumsjon, anonymisert} = props;
    const [outputString, setOutputString] = useState<string>("");
    const [inputString, setInputString] = useState<string>("");
    const [sporingString, setSporingString] = useState<string>("");
    useEffect(() => {
        setOutputString(byggStringRekursivt(subsumsjon.output));
        setInputString(byggStringRekursivt(subsumsjon.input));
        setSporingString(byggStringRekursivt(subsumsjon.sporing));
    }, []);

    function tekniskInfo() {
        let resultatString = ""
        resultatString += "Subsumsjons ID:\n " + subsumsjon.id + "\n"
        resultatString += "Versjon av kode:\n " + subsumsjon.versjonAvKode + "\n"
        resultatString += "Versjon:\n " + subsumsjon.versjon + "\n"
        return resultatString.split("\n").map((s, key) => (
            <div style={{overflowWrap: "break-word"}} key={key}>
                &nbsp;{s}
            </div>
        ))
    }

    return (
        <div className="table-row-expanded-content-container">
            <div className="table-row-expanded-content-column">
                <div>
                    <b>Faktum: </b>
                    {!anonymisert ? inputString.split("\n").map((s, key) => (
                        <div style={{overflowWrap: "break-word"}} key={key}>
                            &nbsp;{s}
                        </div>
                    )) : '********'}
                </div>
                <div>
                    <b>Resultat: </b>
                    {!anonymisert ? outputString.split("\n").map((s, key) => (
                        <div key={key}>&nbsp;{s}</div>
                    )) : '********'}
                </div>
            </div>
            <div className="table-row-expanded-content-column">
                <div>
                    <b>Kilde: </b>
                    {subsumsjon.kilde}
                </div>
                <div>
                    <b>Lovverk: </b> {subsumsjon.lovverk}
                </div>
                <div>
                    <b>Versjon av lovverk: </b> {subsumsjon.lovverksversjon}
                </div>
                <div>
                    <p></p>
                    {sporingString.split("\n").filter((s) => (s === "sykmelding:" || s === "soknad:")).map((s, key) => (
                        <div key={key}> &nbsp;{s === "soknad:" ? "søknad:" : s}<FileContent/></div>
                    ))}
                </div>
                <ReadMore
                    size="small"
                    header="Teknisk info"
                >
                    {tekniskInfo()}
                </ReadMore>
            </div>
        </div>
    );
}
