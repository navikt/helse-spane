import {Checkbox, CheckboxGroup, Search} from "@navikt/ds-react";
import React, {FormEvent, useState} from "react";
import {Backend} from "../../service";
import {PersonDto} from "../../types";
import "./søkefelt.css"

interface Props {
    fødselsnummer: string
    backend: Backend
    setFødselsnummer: React.Dispatch<React.SetStateAction<string>>
    setPerson: React.Dispatch<React.SetStateAction<PersonDto | undefined>>
    setOrgnumre: React.Dispatch<React.SetStateAction<string[]>>
    setAnonymisert: React.Dispatch<React.SetStateAction<Boolean>>
    anonymisert: Boolean

}


function Søkefelt(props: Props) {

    const {fødselsnummer, backend, setFødselsnummer, setPerson, setOrgnumre, setAnonymisert, anonymisert} = props;

    const [tempFnr, setTempFnr] = useState<string>("");
    const [feilmelding, setFeilmelding] = useState<string>("");

    const handleSubmit = (event: FormEvent<HTMLFormElement>) => {

        if (!(/^\d+$/.test(tempFnr))) {
            setFeilmelding("Personnummer kan kun være tall")
            return
        }
        if (tempFnr.length != 11) {
            setFeilmelding("Personnummer må være 11 siffer")
            return
        }

        setFødselsnummer(tempFnr)

        event.preventDefault();
        backend.person(fødselsnummer)
            .then((r) => {
                setPerson(r)
                return r
            })
            .then((r) => {
                let orgnumre: string[] = []
                r!.vedtaksperioder.forEach(
                    (vedtaksperiode) => {
                        if (!orgnumre.includes(vedtaksperiode.orgnummer)) {
                            orgnumre.push(vedtaksperiode.orgnummer)
                        }
                    }
                )
                setOrgnumre(orgnumre)
            });

    };

    return (
        <div className="search-container">
            <h2>Personsøk</h2>
            <div className="actions-container">
                <form onSubmit={handleSubmit}>
                    <Search
                        label="Søk etter fødselsnummer"
                        size="small"
                        variant="secondary"
                        onChange={(e) => setTempFnr(e)}
                        maxLength={11}
                        type={"numeric"}
                        error={feilmelding}
                    />
                </form>
                <CheckboxGroup
                    className="checbox-group"
                    legend="Anonymiser data gruppe"
                    onChange={() => fødselsnummer ? setAnonymisert(!anonymisert) : anonymisert}
                    hideLegend
                >
                    <Checkbox value="Anonymiser data">Anonymiser data</Checkbox>
                </CheckboxGroup>
            </div>
        </div>
    )
}

export default Søkefelt;
