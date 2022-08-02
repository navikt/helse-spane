import { SubsumsjonDto } from "../types";
import { FileContent } from "@navikt/ds-icons";
import React, { useEffect, useState } from "react";
import byggStringRekursivt from "./utlis";

interface Props {
  subsumsjon: SubsumsjonDto;
}

export default function UtvidetSubsumsjonTableInnhold(props: Props) {
  const { subsumsjon } = props;
  const [outputString, setOutputString] = useState<string>("");
  const [inputString, setInputString] = useState<string>("");
  const [sporingString, setSporingString] = useState<string>("");
  useEffect(() => {
    setOutputString(byggStringRekursivt(subsumsjon.output));
    setInputString(byggStringRekursivt(subsumsjon.input));
    setSporingString(byggStringRekursivt(subsumsjon.sporing));
  }, []);

  return (
    <div className="table-row-expanded-content-container">
      <div className="table-row-expanded-content-column">
        <div>
          <b>Output: </b>
          {outputString.split("\n").map((s, key) => (
            <div key={key}>{s}</div>
          ))}
        </div>
        <div>
          <b>Input: </b>
          {inputString.split("\n").map((s, key) => (
            <div style={{ overflowWrap: "break-word" }} key={key}>
              {s}
            </div>
          ))}
        </div>
        <div>
          <b>Kilde: </b>
          {subsumsjon.kilde}
        </div>
      </div>
      <div className="table-row-expanded-content-column">
        <div>
          <b>Lovverk: </b> {subsumsjon.lovverk}
        </div>
        <div>
          <b>Versjon av lovverk: </b> {subsumsjon.lovverksversjon}
        </div>
        <div>
          <p></p>
          {sporingString.split("\n").filter((s) => (s === "sykmelding:" || s === "soknad:")).map((s, key) => (
            <div key={key} >{s}<FileContent/></div>
          ))}
        </div>
      </div>
    </div>
  );
}
