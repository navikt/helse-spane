import {Checkbox, CheckboxGroup} from "@navikt/ds-react";
import React from "react";
import DatePicker from "./DatePicker";
import "./sidepanel.css";

interface Props {
    orgnumre: string[];
    setValgte: React.Dispatch<React.SetStateAction<string[]>>
}

function SidePanel(props: Props) {
    const {orgnumre, setValgte} = props;

    return (
        <div className="sidepanel-container">
            <h3>Personnummer: </h3>
            12345678901
            <div className="date-picker-container">
                <DatePicker label="Fra"/>
                <DatePicker label="Til"/>
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

export default SidePanel;
