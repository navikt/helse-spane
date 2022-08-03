import {Checkbox, CheckboxGroup, Search} from "@navikt/ds-react";
import React, {FormEvent} from "react";
import DatePicker from "./DatePicker";
import "./sidepanel.css";
import {Backend} from "../../service";
import {PersonDto} from "../../types";

interface Props {
    orgnumre: string[];
    setOrgnumre: React.Dispatch<React.SetStateAction<string[]>>
    setValgte: React.Dispatch<React.SetStateAction<string[]>>
    setFraDato: React.Dispatch<React.SetStateAction<string>>
    setTilDato: React.Dispatch<React.SetStateAction<string>>
    backend: Backend
    fødselsnummer: string
    setFødselsnummer: React.Dispatch<React.SetStateAction<string>>
    setPerson: React.Dispatch<React.SetStateAction<PersonDto | undefined>>

}

function SidePanel(props: Props) {
    const {orgnumre, setOrgnumre, setValgte, setFraDato, setTilDato, backend, fødselsnummer, setFødselsnummer, setPerson} = props;

    const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        backend.person(fødselsnummer)
            .then((r) => {
                setPerson(r)
                return r
            })
            .then((r)=>{
                let orgnumre : string[] = []
                r!.vedtaksperioder.forEach(
                    (vedtaksperiode) => {
                        if(!orgnumre.includes(vedtaksperiode.orgnummer)) {
                            orgnumre.push(vedtaksperiode.orgnummer)
                        }
                    }
                )
                setOrgnumre(orgnumre)
            });
    };
    return (
        <div className="sidepanel-container">
            <h2>Personsøk</h2>
            <div className="search-container">
                <form onSubmit={handleSubmit}>
                    <Search
                        label="Søk etter fødselsnummer"
                        size="small"
                        variant="secondary"
                        onChange={(e) => setFødselsnummer(e)}
                    />
                </form>
            </div>
            <h3>Personnummer: </h3>
            12345678901
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

export default SidePanel;
