import { Checkbox, CheckboxGroup, Search } from "@navikt/ds-react";
import React, { useState } from "react";
import { Backend } from "../../service";
import { PersonDto } from "../../types";
import "./søkefelt.css";

interface Props {
  fødselsnummer: string;
  backend: Backend;
  setFødselsnummer: React.Dispatch<React.SetStateAction<string>>;
  setPerson: React.Dispatch<React.SetStateAction<PersonDto | undefined>>;
  setPersoner: React.Dispatch<React.SetStateAction<PersonDto[] | undefined>>;
  setOrgnumre: React.Dispatch<React.SetStateAction<string[]>>;
  setAnonymisert: React.Dispatch<React.SetStateAction<Boolean>>;
  anonymisert: Boolean;
  fane: string;
}

function Søkefelt(props: Props) {
  const {
    fødselsnummer,
    backend,
    setFødselsnummer,
    setPerson,
    setPersoner,
    setOrgnumre,
    setAnonymisert,
    anonymisert,
    fane,
  } = props;

  const [feilmelding, setFeilmelding] = useState<string>("");

  const [fnrInput, setFnrInput] = useState<string>("0");

  const handleChangeFnr = (fnr: string) => {
    setFnrInput(fnr);

    if (!/^\d+$/.test(fnrInput)) {
      setFeilmelding("Fødselsnummer kan kun være tall");
      return;
    }
    if (fnrInput.length < 11) {
      setFeilmelding("Fødselsnummer må være 11 siffer lang");
      return;
    }

    setFeilmelding("");
    setFødselsnummer(fnrInput);
  };

  const handleSubmit = () => {
    backend
      .person(fnrInput)
      .then((r) => {
        setPerson(r);
        setPersoner([r, r, r]);
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
      });
  };

  return (
    <div className="personsøk-container">
      <h2>{fane === "Person" ? "Søk på person" : "Søk på paragraf"}</h2>
      <div className="personsøk-actions-container">
        <Search
          label="Søk etter fødselsnummer"
          size="small"
          variant="secondary"
          onChange={(e) => handleChangeFnr(e)}
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

export default Søkefelt;
