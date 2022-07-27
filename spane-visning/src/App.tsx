import { FormEvent, useState } from "react";
import "./App.css";
import "@navikt/ds-css";
import "@navikt/ds-css-internal";
import PersonSøkSide from "./components/PersonSøkSide";
import Sidepanel from "./components/Sidepanel";
import { Header } from "@navikt/ds-react-internal";

function App() {
  return (
    <div>
      <Header>
        <Header.Title as="h1">NAV | Spane</Header.Title>
      </Header>
      <div className="ytre-container">
        <Sidepanel />
        <PersonSøkSide />
      </div>
    </div>
  );
}


export default App;
