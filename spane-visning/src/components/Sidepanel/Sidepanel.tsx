import React from "react";
import "./sidepanel.css";
import { LawFilled, PeopleFilled } from "@navikt/ds-icons";

import { PersonDto } from "../../types";
import TidsromVelger from "./TidsromVelger";
import ArbeidsgiverCheckbox from "./ArbeidsgiverCheckbox";

interface Props {
  orgnumre: string[];
  setValgte: React.Dispatch<React.SetStateAction<string[]>>;
  setFraDato: React.Dispatch<React.SetStateAction<string>>;
  setTilDato: React.Dispatch<React.SetStateAction<string>>;
  søk: string;
  harSøkt: Boolean;
  person: PersonDto | undefined;
  anonymisert: Boolean;
  fane: string;
}

export default function Sidepanel(props: Props) {
  const {
    orgnumre,
    setValgte,
    setFraDato,
    setTilDato,
    søk,
    harSøkt,
    person,
    anonymisert,
    fane,
  } = props;

  return (
    <div className="sidepanel-container">
      <div className="fødselsnummer-container">
        <div className="fødselsnummer-overskrift">
          {fane === "Person" ? (
              <PeopleFilled height={"2em"} width={"2em"} />
          ) : (
              <LawFilled height={"2em"} width={"2em"} />
          )}
          <h3 style={{ display: "inline" }}>
            {fane === "Person" ? "Fødselsnummer:" : "Paragraf:"}
          </h3>
        </div>
        <div>
          {harSøkt ? (!anonymisert ? søk : "***********") : null}
        </div>
      </div>
      {person && (
        <div>
          {fane === "Person" ? (
            <>
              <TidsromVelger setFraDato={setFraDato} setTilDato={setTilDato} />
              <ArbeidsgiverCheckbox
                anonymisert={anonymisert}
                setValgte={setValgte}
                orgnumre={orgnumre}
              />
            </>
          ) : null}
        </div>
      )}
    </div>
  );
}
