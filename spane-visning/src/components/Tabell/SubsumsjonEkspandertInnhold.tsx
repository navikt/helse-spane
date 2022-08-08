import { SubsumsjonDto } from "../../types";
import React, { useEffect, useState } from "react";
import { byggStringRekursivt } from "../../utlis";
import { ReadMore } from "@navikt/ds-react";
import "./subsumsjonekspandertinnhold.css";

interface Props {
  subsumsjon: SubsumsjonDto;
  anonymisert: Boolean;
}

export default function SubsumsjonEkspandertInnhold(props: Props) {
  const { subsumsjon, anonymisert } = props;
  const [outputString, setOutputString] = useState<string>("");
  const [inputString, setInputString] = useState<string>("");
  useEffect(() => {
    setOutputString(byggStringRekursivt(subsumsjon.output));
    setInputString(byggStringRekursivt(subsumsjon.input));
  }, []);

  function tekniskInfo() {
    let resultatString = "";
    resultatString += "Subsumsjons ID:\n " + subsumsjon.id + "\n";
    resultatString += "Versjon av kode:\n " + subsumsjon.versjonAvKode + "\n";
    resultatString += "Versjon:\n " + subsumsjon.versjon + "\n";
    resultatString +=
      "Sporing:\n " + byggStringRekursivt(subsumsjon.sporing) + "\n";
    return resultatString.split("\n").map((s, key) => (
      <div style={{ overflowWrap: "break-word" }} key={key}>
        {s}
      </div>
    ));
  }

  return (
    <div className="subsumsjon-ekspanderbar-rad-container">
      <div className="subsumsjon-ekspandert-innhold-kolonne">
        <div>
          <b>Faktum: </b>
          {!anonymisert
            ? inputString.split("\n").map((s, key) => (
                <div style={{ overflowWrap: "break-word" }} key={key}>
                  {s}
                </div>
              ))
            : "********"}
        </div>
        <div>
          <b>Resultat: </b>
          {!anonymisert
            ? outputString
                .split("\n")
                .map((s, key) => <div key={key}>&nbsp;{s}</div>)
            : "********"}
        </div>
      </div>
      <div className="subsumsjon-ekspandert-innhold-kolonne">
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
        <p></p>
        <ReadMore size="small" header="Teknisk info">
          {tekniskInfo()}
        </ReadMore>
      </div>
    </div>
  );
}
