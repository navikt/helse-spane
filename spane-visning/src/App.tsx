import { FormEvent, useState } from "react";
import "./App.css";
import { Environment } from "./environment";
import testPerson from "./resources/testPerson.json";
import Vedtaksperiode from "./components/Vedtaksperiode";
import "@navikt/ds-css";
import "@navikt/ds-css-internal";
import { Search } from "@navikt/ds-react";

export const restBackend = (): Backend => {
  return {
    person(fnr: string): Promise<PersonDto> {
      return fetch(`/fnr/` + fnr, {
        method: "get",
        headers: {
          Accept: "application/json",
        },
      }).then((response) => response.json());
    },
  };
};

export const testBackend = (): Backend => {
  return {
    person(fnr: string): Promise<PersonDto> {
      return Promise.resolve(testPerson as unknown as PersonDto);
    },
  };
};

export type Backend = {
  person: (fnr: string) => Promise<PersonDto>;
};

export type PersonDto = {
  vedtaksperioder: VedtaksperiodeDto[];
  fnr: string;
};

export type VedtaksperiodeDto = {
  subsumsjoner: SubsumsjonDto[];
};

export type SubsumsjonDto = {
  id: string;
  versjon: string;
  eventName: string;
  kilde: string;
  versjonAvKode: string;
  fødselsnummer: string;
  sporing: Map<string, string[]>;
  tidsstempel: string;
  lovverk: string;
  lovverksversjon: string;
  paragraf: string;
  ledd: number | null;
  punktum: number | null;
  bokstav: string | null;
  input: Map<string, any>;
  output: Map<string, any>;
  utfall: string;
};

function App(this: any) {
  const backend: Backend = Environment.isDevelopment
    ? testBackend()
    : restBackend();

  const [person, setPerson] = useState<PersonDto>();
  const [fødselsnummer, setFødselsnummer] = useState<string>("");

  const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    console.log(fødselsnummer);
    backend.person(fødselsnummer).then((r) => setPerson(r));
  };

  return (
    <div className="main-container">
      <h1>no Spane no gain</h1>
      <div>People call me the jarlinator, but you can call me tonight</div>
      <div className="search-container">
        <form onSubmit={handleSubmit}>
          <Search
            label="Søk etter fødselsnummer"
            size="medium"
            variant="secondary"
            onChange={(e) => setFødselsnummer(e)}
          />
        </form>
      </div>

      <div>
        {person &&
          person.vedtaksperioder.map(
            (vedtaksperiode: VedtaksperiodeDto, key) => {
              return (
                <Vedtaksperiode
                  key={key}
                  subsumsjoner={vedtaksperiode.subsumsjoner}
                />
              );
            }
          )}
      </div>
    </div>
  );
}

export default App;
