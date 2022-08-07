import { useState } from "react";
import "./MainContainer.css";
import "@navikt/ds-css";
import "@navikt/ds-css-internal";
import Sidepanel from "./Sidepanel/Sidepanel";
import Søkefelt from "./Søkefelt/Søkefelt";

import { Header } from "@navikt/ds-react-internal";
import { ParagrafsøkDto, PersonDto } from "../types";
import { Environment } from "../environment";
import PseudovedtaksperiodeTabell from "./Tabell/PseudovedtaksperiodeTabell";
import { ToggleGroup } from "@navikt/ds-react";
import { Law, People } from "@navikt/ds-icons";
import PersonTabell from "./Tabell/PersonTabell";

import {
  BackendParagraf,
  BackendPerson,
  restBackendParagraf,
  restBackendPerson,
  testBackendParagraf,
  testBackendPerson,
} from "../service";

export default function MainContainer() {
  const [orgnumre, setOrgnumre] = useState<string[]>([]);
  const [valgte, setValgte] = useState<string[]>([]);
  const [fraDato, setFraDato] = useState<string>("");
  const [tilDato, setTilDato] = useState<string>("");
  const [person, setPerson] = useState<PersonDto>();
  const [paragrafSøk, setParagrafSøk] = useState<ParagrafsøkDto>();
  const [anonymisert, setAnonymisert] = useState<Boolean>(false);
  const [søk, setSøk] = useState<string>("");
  const [fane, setFane] = useState<string>("Person");

  const backendPerson: BackendPerson = Environment.isDevelopment
    ? testBackendPerson()
    : restBackendPerson();
  const backendParagraf: BackendParagraf = Environment.isDevelopment
    ? testBackendParagraf()
    : restBackendParagraf();

  return (
    <div>
      <Header>
        <Header.Title as="h1">Paragraf i kode</Header.Title>
      </Header>

      <div>
        <ToggleGroup onChange={(x) => setFane(x)} value={fane} size="medium">
          <ToggleGroup.Item value="Person">
            <People aria-hidden />
            Søk på person
          </ToggleGroup.Item>
          <ToggleGroup.Item value="Paragraf">
            <Law aria-hidden />
            Søk på paragraf
          </ToggleGroup.Item>
        </ToggleGroup>
      </div>

      <div>
        <div className="ytre-main-container">
          <Søkefelt
            fødselsnummer={søk}
            backendPerson={backendPerson}
            backendParagraf={backendParagraf}
            setOrgnumre={setOrgnumre}
            setSøk={setSøk}
            setPerson={setPerson}
            setPersoner={setParagrafSøk}
            setAnonymisert={setAnonymisert}
            anonymisert={anonymisert}
            fane={fane}
          />
          <div className="indre-main-container">
            <Sidepanel
              orgnumre={orgnumre}
              setValgte={setValgte}
              setFraDato={setFraDato}
              setTilDato={setTilDato}
              søk={søk}
              person={person}
              anonymisert={anonymisert}
              fane={fane}
            />
            <div className={"tabell-container"}>
              {fane === "Person" ? (
                <PseudovedtaksperiodeTabell
                  valgte={valgte}
                  fraDato={fraDato}
                  tilDato={tilDato}
                  person={person}
                  anonymisert={anonymisert}
                />
              ) : (
                <PersonTabell
                  paragrafSøk={paragrafSøk}
                  anonymisert={anonymisert}
                />
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
