import {useState} from "react";
import "./App.css";
import "@navikt/ds-css";
import "@navikt/ds-css-internal";
import PersonSøkSide from "./components/PersonSøkSide";
import Sidepanel from "./components/Sidepanel";
import {Header} from "@navikt/ds-react-internal";


function App() {
    const [orgnumre, setOrgnumre] = useState<string[]>([]);

    return (
        <div>
            <Header>
                <Header.Title as="h1">NAV | Spane</Header.Title>
            </Header>
            <div className="ytre-container">
                <Sidepanel orgnumre={orgnumre}/>
                <PersonSøkSide setOrgnumre={setOrgnumre}/>
            </div>
        </div>
    );
}


export default App;
