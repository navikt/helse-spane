import { useState } from "react";
import "./MainContainer.css";
import "@navikt/ds-css";
import "@navikt/ds-css-internal";
import Sidepanel from "./Sidepanel/Sidepanel";
import Søkefelt from "./Søkefelt/Søkefelt";

import { Header } from "@navikt/ds-react-internal";
import { PersonDto } from "../types";
import { Backend, restBackend, testBackend } from "../service";
import { Environment } from "../environment";
import PseudovedtaksperiodeTabell from "./Tabell/PseudovedtaksperiodeTabell";
import { ToggleGroup } from "@navikt/ds-react";
import { Law, People } from "@navikt/ds-icons";
import PersonTabell from "./Tabell/PersonTabell";

export default function MainContainer() {
  const [orgnumre, setOrgnumre] = useState<string[]>([]);
  const [valgte, setValgte] = useState<string[]>([]);
  const [fraDato, setFraDato] = useState<string>("");
  const [tilDato, setTilDato] = useState<string>("");
  const [person, setPerson] = useState<PersonDto>();
  const [personer, setPersoner] = useState<PersonDto[]>();
  const [anonymisert, setAnonymisert] = useState<Boolean>(false);
  const [fødselsnummer, setFødselsnummer] = useState<string>("");
  const [fane, setFane] = useState<string>("Person");

  const backend: Backend = Environment.isDevelopment
    ? testBackend()
    : restBackend();

  return (
    <div>
      <Header>
        <Header.Title as="h1">Paragraf i kode</Header.Title>
      </Header>

      <div>
        <ToggleGroup onChange={(x) => setFane(x)} value={fane} size="medium">
          <ToggleGroup.Item value="Person">
            <People aria-hidden />
            Søk på personnummer
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
            fødselsnummer={fødselsnummer}
            backend={backend}
            setOrgnumre={setOrgnumre}
            setFødselsnummer={setFødselsnummer}
            setPerson={setPerson}
            setPersoner={setPersoner}
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
              fødselsnummer={fødselsnummer}
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
                <PersonTabell personer={personer} anonymisert={anonymisert} />
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
