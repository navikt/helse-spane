import { FormEvent, useState } from "react";
import "./App.css";
import { Environment } from "./environment";
import "@navikt/ds-css";
import "@navikt/ds-css-internal";
import { Search, Table } from "@navikt/ds-react";
import {Backend, restBackend, testBackend} from "./service";
import SubsumsjonTableBody from "./components/SubsumsjonTableBody";
import {PersonDto} from "./types";



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
            {person && <SubsumsjonTableBody person={person}/>}
        </Table>
      </>
    </div>
  );
}

export default App;
