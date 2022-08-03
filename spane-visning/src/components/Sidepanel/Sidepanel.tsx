import {Checkbox, CheckboxGroup, Search} from "@navikt/ds-react";
import React, {FormEvent} from "react";
import DatePicker from "./DatePicker";
import "./sidepanel.css";
import { PeopleFilled } from "@navikt/ds-icons";



interface Props {
    orgnumre: string[];
    setValgte: React.Dispatch<React.SetStateAction<string[]>>
    setFraDato: React.Dispatch<React.SetStateAction<string>>
    setTilDato: React.Dispatch<React.SetStateAction<string>>
    fødselsnummer: string
}

function Sidepanel(props: Props) {
    const {orgnumre, setValgte, setFraDato, setTilDato, fødselsnummer} = props;
    return (
        <div className="sidepanel-container">   
            <div className="personnummer-container">
                <PeopleFilled height={"2em"} width={"2em"}/>
                <h3 style={{display: "inline"}} >Personnummer: </h3>
            </div>
            {fødselsnummer}
            <div className="date-picker-container">
                <DatePicker label="Fra" setDato={setFraDato}/>
                <DatePicker label="Til" setDato={setTilDato}/>
            </div>
            <div className="checkbox-group-container">
                <CheckboxGroup
                    legend="Arbeidsgiver"
                    onChange={(v) => {
                        setValgte(v)
                    }}
                >
                    {orgnumre.map((orgnummer, key) => {
                        return <Checkbox key={key} value={orgnummer}>{orgnummer}</Checkbox>
                    })}
                </CheckboxGroup>
            </div>
        </div>
    );
}

export default Sidepanel;
