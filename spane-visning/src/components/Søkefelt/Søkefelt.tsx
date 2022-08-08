import { Checkbox, CheckboxGroup, Search } from "@navikt/ds-react";
import React, { useState } from "react";
import { BackendParagraf, BackendPerson } from "../../service";
import { PersonDto } from "../../types";
import "./søkefelt.css";

interface Props {
  fødselsnummer: string;
  backendPerson: BackendPerson;
  backendParagraf: BackendParagraf;
  søk: string;
  setSøk: React.Dispatch<React.SetStateAction<string>>;
  setHarSøkt: React.Dispatch<React.SetStateAction<Boolean>>;
  setPerson: React.Dispatch<React.SetStateAction<PersonDto | undefined>>;
  setPersoner: React.Dispatch<React.SetStateAction<PersonDto[] | undefined>>;
  setOrgnumre: React.Dispatch<React.SetStateAction<string[]>>;
  setAnonymisert: React.Dispatch<React.SetStateAction<Boolean>>;
  anonymisert: Boolean;
  fane: string;
}

export default function Søkefelt(props: Props) {
  const {
    fødselsnummer,
    backendPerson,
    backendParagraf,
    søk,
    setSøk,
    setHarSøkt,
    setPerson,
    setPersoner,
    setOrgnumre,
    setAnonymisert,
    anonymisert,
    fane,
  } = props;

  const [feilmelding, setFeilmelding] = useState<string>("");

  const handleSubmit = () => {
    setHarSøkt(true);
    if (fane === "Person" && !/^\d+$/.test(søk)) {
      setFeilmelding("Fødselsnummer kan kun være tall");
      console.log(søk);
      return;
    }
    if (fane === "Person" && søk.length < 11) {
      setFeilmelding("Fødselsnummer må være 11 siffer lang");
      console.log(søk);
      return;
    }
    fane === "Person"
      ? backendPerson
          .person(søk)
          .then((r) => {
            setPerson(r);
            return r;
          })
          .then((r) => {
            let orgnumre: string[] = [];
            r!.vedtaksperioder.forEach((vedtaksperiode) => {
              if (!orgnumre.includes(vedtaksperiode.orgnummer)) {
                orgnumre.push(vedtaksperiode.orgnummer);
              }
            });
            setOrgnumre(orgnumre);
          })
      : backendParagraf.personer(søk).then((r) => {
          setPersoner(r);
        });
  };

  return (
    <div className="søkefelt-container">
      <h2>{fane === "Person" ? "Søk på person" : "Søk på paragraf"}</h2>
      <div className="søkefelt-actions">
        <Search
          label="Søk etter fødselsnummer"
          size="small"
          variant="secondary"
          onChange={(e) => setSøk(e)}
          maxLength={11}
          type={"numeric"}
          error={feilmelding}
        >
          <Search.Button onClick={handleSubmit}></Search.Button>
        </Search>
        <CheckboxGroup
          className="anonymiser-checbox-group"
          legend="Anonymiser data gruppe"
          onChange={() =>
            fødselsnummer ? setAnonymisert(!anonymisert) : anonymisert
          }
          hideLegend
        >
          <Checkbox value="Anonymiser data">Anonymiser data</Checkbox>
        </CheckboxGroup>
      </div>
    </div>
  );
}
