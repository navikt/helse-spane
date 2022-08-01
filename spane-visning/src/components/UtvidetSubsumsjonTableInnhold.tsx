import { SubsumsjonDto } from "../types";
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
          <b>Versjon av kode:</b>
          <div style={{ overflowWrap: "break-word" }}>
            {subsumsjon.versjonAvKode}
          </div>
        </div>
        <div>
          <b>Meldingsid: </b> {subsumsjon.id}
        </div>
        <div>
          <b>Sporing: </b>{" "}
          {sporingString.split("\n").map((s, key) => (
            <div key={key}>{s}</div>
          ))}
        </div>
      </div>
    </div>
  );
}