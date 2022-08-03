import {useState} from "react";
import "./MainContainer.css";
import "@navikt/ds-css";
import "@navikt/ds-css-internal";
import Sidepanel from "./Sidepanel/Sidepanel";
import Søkefelt from "./Søkefelt/Søkefelt";

import {Header} from "@navikt/ds-react-internal";
import {PersonDto} from "../types";
import {Backend, restBackend, testBackend} from "../service";
import {Environment} from "../environment";
import PseudovedtaksperiodeTable from "./PseudovedtaksperiodeTable/PseudovedtaksperiodeTable";


function MainContainer() {
    const [orgnumre, setOrgnumre] = useState<string[]>([]);
    const [valgte, setValgte] = useState<string[]>([]);
    const [fraDato, setFraDato] = useState<string>("");
    const [tilDato, setTilDato] = useState<string>("");
    const [person, setPerson] = useState<PersonDto>()
    const [fødselsnummer, setFødselsnummer] = useState<string>("");

    const backend: Backend = Environment.isDevelopment
        ? testBackend()
        : restBackend();

    return (
        <div>
            <Header>
                <Header.Title as="h1">Paragraf i kode</Header.Title>
            </Header>
            <div className="ytre-main-container">
                <Søkefelt fødselsnummer={fødselsnummer} backend={backend} setOrgnumre={setOrgnumre} 
                               setFødselsnummer={setFødselsnummer} setPerson={setPerson} />
                <div className="indre-main-container">
                    <Sidepanel orgnumre={orgnumre} setValgte={setValgte} setFraDato={setFraDato} setTilDato={setTilDato}
                               fødselsnummer={fødselsnummer}/>
                    <PseudovedtaksperiodeTable valgte={valgte} setOrgnumre={setOrgnumre} fraDato={fraDato} tilDato={tilDato} setPerson={setPerson} person={person}  />
                </div>
            </div>
        </div>
    );
}

export default MainContainer