import {Checkbox, CheckboxGroup, Search} from "@navikt/ds-react";
import React, {FormEvent, useState} from "react";
import {BackendParagraf, BackendPerson} from "../../service";
import {PersonDto} from "../../types";
import "./søkefelt.css";

interface Props {
    fødselsnummer: string;
    backendPerson: BackendPerson;
    backendParagraf: BackendParagraf;
    søketekst: string;
    setSøketekst: React.Dispatch<React.SetStateAction<string>>;
    setHarSøkt: React.Dispatch<React.SetStateAction<Boolean>>;
    setPerson: React.Dispatch<React.SetStateAction<PersonDto | undefined>>;
    setPersoner: React.Dispatch<React.SetStateAction<PersonDto[] | undefined>>;
    setOrgnumre: React.Dispatch<React.SetStateAction<string[]>>;
    setAnonymisert: React.Dispatch<React.SetStateAction<Boolean>>;
    anonymisert: boolean;
    fane: string;
    setResponsSuksess: React.Dispatch<React.SetStateAction<boolean | null>>;
    setLaster: React.Dispatch<React.SetStateAction<boolean>>
}

export default function Søkefelt(props: Props) {
    const {
        fødselsnummer,
        backendPerson,
        backendParagraf,
        søketekst,
        setSøketekst,
        setHarSøkt,
        setPerson,
        setPersoner,
        setOrgnumre,
        setAnonymisert,
        anonymisert,
        fane,
        setResponsSuksess,
        setLaster
    } = props;

    const [feilmelding, setFeilmelding] = useState<string>("");

    const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        if (fane === "Person") {
            if (!/^\d+$/.test(søketekst)) {
                setFeilmelding("Fødselsnummer kan kun være tall");
                return;
            }
            if (søketekst.length < 11) {
                setFeilmelding("Fødselsnummer må være 11 siffer lang");
                return;
            }
        } else {
            if (!/\d-\d/.test(søketekst)) {
                setFeilmelding("Paragraf på feil form, prøv f.eks. 8-2");
                return;
            }
        }

        setHarSøkt(true);
        setFeilmelding("");

        if (fane === "Person") {
            setLaster(true)
            return backendPerson
                .person(søketekst)
                .then((r) => {
                    if (r === null) {
                        return Promise.reject()
                    } else {
                        setResponsSuksess(true)
                        setLaster(false)
                    }
                    setPerson(r);
                    return r;
                })
                .then((r) => {
                    let orgnumre: string[] = [];
                    r?.vedtaksperioder.forEach((vedtaksperiode) => {
                        if (!orgnumre.includes(vedtaksperiode.orgnummer)) {
                            orgnumre.push(vedtaksperiode.orgnummer);
                        }
                    });
                    setOrgnumre(orgnumre);
                }).catch(() => {
                    setPerson(undefined)
                    setResponsSuksess(false)
                    setLaster(false)
                })
        } else {
            console.log("paragraf fane")
           return backendParagraf.personer(søketekst).then((r) => {
               console.log(r)
                setPersoner(r);
                setLaster(false)
                setResponsSuksess(true)
            });
        }
    };

    return (
        <div className="søkefelt-container">
            <h2>{fane === "Person" ? "Søk på person" : "Søk på paragraf"}</h2>
            <div className="søkefelt-actions">
                <form role="search" onSubmit={handleSubmit}>
                    <Search
                        label="Søk etter fødselsnummer"
                        size="small"
                        variant="secondary"
                        onChange={setSøketekst}
                        maxLength={11}
                        type={"numeric"}
                        error={feilmelding}
                    />
                </form>

                <CheckboxGroup
                    className="anonymiser-checbox-group"
                    legend="Anonymiser data gruppe"
                    onChange={() =>
                        setAnonymisert(!anonymisert)
                    }
                    hideLegend
                >
                    <Checkbox checked={anonymisert} value="Anonymiser data">Anonymiser data</Checkbox>
                </CheckboxGroup>
            </div>
        </div>
    );
}
