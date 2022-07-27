import { Search, Table } from "@navikt/ds-react";
import React, { FormEvent, useState } from "react";
import VedtaksperiodeTableBody from "./VedtaksperiodeTableBody";
import "@navikt/ds-css-internal";
import { Backend, restBackend, testBackend } from "../service";
import { Environment } from "../environment";
import { PersonDto } from "../types";
import "./personSøk.css";

interface Props {}

function PersonSøkSide(props: Props) {
  const {} = props;
  const backend: Backend = Environment.isDevelopment
    ? testBackend()
    : restBackend();

  const [person, setPerson] = useState<PersonDto>();
  const [fødselsnummer, setFødselsnummer] = useState<string>("");

  const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    backend.person(fødselsnummer).then((r) => setPerson(r));
  };

  return (
    <main className="main-container">
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
      <Table size="medium">
        {person && <VedtaksperiodeTableBody person={person} />}
      </Table>
    </main>
  );
}

export default PersonSøkSide;
