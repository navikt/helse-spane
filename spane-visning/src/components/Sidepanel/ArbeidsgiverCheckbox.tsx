import {Checkbox, CheckboxGroup} from "@navikt/ds-react";
import React from "react";

interface Props {
    orgnumre: string[];
    setValgte: React.Dispatch<React.SetStateAction<string[]>>
    anonymisert: Boolean
}

function ArbeidsgiverCheckbox(props: Props) {
    const {orgnumre, setValgte, anonymisert} = props;


    return <div className="checkbox-group-container">
        <CheckboxGroup
            legend="Arbeidsgiver"
            onChange={(v) => {
                setValgte(v)
            }}
        >
            {orgnumre.map((orgnummer, key) => {
                return <Checkbox key={key}
                                 value={orgnummer}> {!anonymisert ? orgnummer : '***********'}</Checkbox>
            })}
        </CheckboxGroup>
    </div>
}

export default ArbeidsgiverCheckbox;