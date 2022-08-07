import React from "react";
import "./sidepanel.css";
import {PeopleFilled} from "@navikt/ds-icons";
import {PersonDto} from "../../types";
import VelgTidsrom from "./VelgTidsrom";
import ArbeidsgiverCheckbox from "./ArbeidsgiverCheckbox";


interface Props {
    orgnumre: string[];
    setValgte: React.Dispatch<React.SetStateAction<string[]>>
    setFraDato: React.Dispatch<React.SetStateAction<string>>
    setTilDato: React.Dispatch<React.SetStateAction<string>>
    fødselsnummer: string
    person: PersonDto | undefined
    anonymisert: Boolean
}

function Sidepanel(props: Props) {
    const {orgnumre, setValgte, setFraDato, setTilDato, fødselsnummer, person, anonymisert} = props;

    return (
        <div className="sidepanel-container">
            <div className="personnummer-container">
                <PeopleFilled height={"2em"} width={"2em"}/>
                <h3 style={{display: "inline"}}>Personnummer: </h3>
            </div>

            {!anonymisert ? fødselsnummer : '***********'}
            {person &&
                <div>
                    <h4 className="velg-tid-overskrift-container">Velg tidsrom: </h4>
                    <VelgTidsrom setFraDato={setFraDato} setTilDato={setTilDato}/>
                    <ArbeidsgiverCheckbox anonymisert={anonymisert} setValgte={setValgte} orgnumre={orgnumre}/>
                </div>
            }
        </div>
    );
}

export default Sidepanel;
