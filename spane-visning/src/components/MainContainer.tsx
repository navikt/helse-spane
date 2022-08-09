import React, { useState } from "react";
import "./MainContainer.css";
import "@navikt/ds-css";
import "@navikt/ds-css-internal";
import Sidepanel from "./Sidepanel/Sidepanel";
import Søkefelt from "./Søkefelt/Søkefelt";

import { Header } from "@navikt/ds-react-internal";
import { PersonDto } from "../types";
import { Environment } from "../environment";
import PseudovedtaksperiodeTabell from "./Tabell/PseudovedtaksperiodeTabell";
import { Loader, Tabs } from "@navikt/ds-react";
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
  const [personer, setPersoner] = useState<PersonDto[]>();
  const [anonymisert, setAnonymisert] = useState<Boolean>(false);
  const [søketekst, setSøketekst] = useState<string>("");
  const [harSøkt, setHarSøkt] = useState<Boolean>(false);

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
        <Tabs defaultValue="Person" size="medium">
          <Tabs.List>
            <Tabs.Tab
              value="Person"
              iconPosition="left"
              label="Person"
              icon={<People title="Søk på person" />}
            />
            <Tabs.Tab
              value="Paragraf"
              iconPosition="left"
              label="Paragraf"
              icon={<Law title="Søk på paragraf" />}
            />
          </Tabs.List>
          <Tabs.Panel value="Person" className="h-24 w-full bg-gray-50 p-8">
            <div className="ytre-main-container">
              <Søkefelt
                fødselsnummer={søketekst}
                backendPerson={backendPerson}
                backendParagraf={backendParagraf}
                setOrgnumre={setOrgnumre}
                søketekst={søketekst}
                setSøketekst={setSøketekst}
                setHarSøkt={setHarSøkt}
                setPerson={setPerson}
                setPersoner={setPersoner}
                setAnonymisert={setAnonymisert}
                anonymisert={anonymisert}
                fane={"Person"}
              />
              <div className="indre-main-container">
                <Sidepanel
                  orgnumre={orgnumre}
                  setValgte={setValgte}
                  setFraDato={setFraDato}
                  setTilDato={setTilDato}
                  søk={søketekst}
                  harSøkt={harSøkt}
                  person={person}
                  anonymisert={anonymisert}
                  fane={"Person"}
                />
                <div className={"tabell-container"}>
                  {!person && harSøkt ? (
                    <Loader
                      variant="interaction"
                      size="3xlarge"
                      title="venter..."
                    />
                  ) : (
                    <PseudovedtaksperiodeTabell
                      valgte={valgte}
                      fraDato={fraDato}
                      tilDato={tilDato}
                      person={person}
                      anonymisert={anonymisert}
                    />
                  )}
                </div>
              </div>
            </div>
          </Tabs.Panel>
          <Tabs.Panel value="Paragraf" className="h-24 w-full bg-gray-50 p-8">
            <div className="ytre-main-container">
              <Søkefelt
                fødselsnummer={søketekst}
                backendPerson={backendPerson}
                backendParagraf={backendParagraf}
                setOrgnumre={setOrgnumre}
                søketekst={søketekst}
                setSøketekst={setSøketekst}
                setHarSøkt={setHarSøkt}
                setPerson={setPerson}
                setPersoner={setPersoner}
                setAnonymisert={setAnonymisert}
                anonymisert={anonymisert}
                fane={"Paragraf"}
              />
              <div className="indre-main-container">
                <Sidepanel
                  orgnumre={orgnumre}
                  setValgte={setValgte}
                  setFraDato={setFraDato}
                  setTilDato={setTilDato}
                  søk={søketekst}
                  harSøkt={harSøkt}
                  person={person}
                  anonymisert={anonymisert}
                  fane={"Paragraf"}
                />
                <div className={"tabell-container"}>
                  {!personer && harSøkt ? (
                    <Loader
                      variant="interaction"
                      size="3xlarge"
                      title="venter..."
                    />
                  ) : (
                    <PersonTabell
                      personer={personer}
                      anonymisert={anonymisert}
                    />
                  )}
                </div>
              </div>
            </div>
          </Tabs.Panel>
        </Tabs>
      </div>
    </div>
  );
}
