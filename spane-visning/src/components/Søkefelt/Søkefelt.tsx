import { Checkbox, CheckboxGroup, Search } from "@navikt/ds-react";
import React, { FormEvent, useState } from "react";
import { BackendParagraf, BackendPerson } from "../../service";
import { PersonDto } from "../../types";
import "./søkefelt.css";

interface Props {
  fødselsnummer: string;
  backendPerson: BackendPerson;
  backendParagraf: BackendParagraf;
  søketekst: string;
  setSøketekst: React.Dispatch<React.SetStateAction<string>>;
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
    søketekst,
    setSøketekst,
    setHarSøkt,
    setPerson,
    setPersoner,
    setOrgnumre,
    setAnonymisert,
    anonymisert,
    fane,
  } = props;

  const [feilmelding, setFeilmelding] = useState<string>("");

  const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    if (fane === "Person") {
      if (!/^\d+$/.test(søketekst)) {
        setFeilmelding("Fødselsnummer kan kun være tall");
        return;
      }
      if (søketekst.length < 11) {
        setFeilmelding("Fødselsnummer må være 11 siffer lang");
        return;
      }
    } else {
      if (/0-9/.test(søketekst)) {
        setFeilmelding("Paragraf på feil form");
        return;
      }
    }

    setHarSøkt(true);
    setFeilmelding("");

    fane === "Person"
      ? backendPerson
          .person(søketekst)
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
      : backendParagraf.personer(søketekst).then((r) => {
          setPersoner(r);
        });
  };

  return (
    <div className="søkefelt-container">
      <h2>{fane === "Person" ? "Søk på person" : "Søk på paragraf"}</h2>
      <div className="søkefelt-actions">
        <form role="search" onSubmit={handleSubmit}>
          <Search
            label="Søk etter fødselsnummer"
            size="small"
            variant="secondary"
            onChange={setSøketekst}
            maxLength={11}
            type={"numeric"}
            error={feilmelding}
          />
        </form>

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
