import { FormEvent, useState } from "react";
import "./App.css";
import { Environment } from "./environment";
import testPerson from "./resources/testPerson.json";
import Vedtaksperiode from "./components/Vedtaksperiode";
import "@navikt/ds-css";
import "@navikt/ds-css-internal";
import { Search, Table } from "@navikt/ds-react";

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
      <>
        <Table size="medium">
          <Table.Header>
            <Table.Row>
              <Table.HeaderCell scope="col">Vedtaksperiode</Table.HeaderCell>
              <Table.HeaderCell scope="col">Paragraf</Table.HeaderCell>
              <Table.HeaderCell scope="col">Fødseslnummer</Table.HeaderCell>
              <Table.HeaderCell scope="col">Utfall</Table.HeaderCell>
              <Table.HeaderCell scope="col">Tidsstempel</Table.HeaderCell>
            </Table.Row>
          </Table.Header>
          <Table.Body>
            {person &&
              person.vedtaksperioder.map(
                (vedtaksperiode: VedtaksperiodeDto, vedtaksperiodeIdx) => {
                  return vedtaksperiode.subsumsjoner.map(
                    (subsumsjon: SubsumsjonDto, subsumsjonIdx: number) => {
                      return (
                        <Table.ExpandableRow
                          key={subsumsjonIdx}
                          content={
                            <div className="table-row-expanded-content-container">
                              <div className="table-row-expanded-content-column">
                                div1
                              </div>
                              <div className="table-row-expanded-content-column">
                                div2
                              </div>
                            </div>
                          }
                          togglePlacement="right"
                        >
                          <Table.HeaderCell scope="row">
                            {vedtaksperiodeIdx + 1}
                          </Table.HeaderCell>
                          <Table.HeaderCell scope="row">
                            {subsumsjon.paragraf}
                          </Table.HeaderCell>
                          <Table.DataCell>
                            {subsumsjon.fødselsnummer}
                          </Table.DataCell>
                          <Table.DataCell>{subsumsjon.utfall}</Table.DataCell>
                          <Table.DataCell>
                            {subsumsjon.tidsstempel}
                          </Table.DataCell>
                        </Table.ExpandableRow>
                      );
                    }
                  );
                }
              )}
          </Table.Body>
        </Table>
      </>
    </div>
  );
}

export default App;
