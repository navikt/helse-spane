import { Checkbox, CheckboxGroup, Search } from "@navikt/ds-react";
import React, {FormEvent, useState} from "react";
import { Backend } from "../../service";
import { PersonDto } from "../../types";
import "./søkefelt.css"


interface Props {
  fødselsnummer: string
  backend: Backend
  setFødselsnummer: React.Dispatch<React.SetStateAction<string>>
  setPerson: React.Dispatch<React.SetStateAction<PersonDto | undefined>>
  setOrgnumre: React.Dispatch<React.SetStateAction<string[]>>

}


function Søkefelt(props: Props){
  
  const { fødselsnummer, backend, setFødselsnummer, setPerson, setOrgnumre } = props;
  const [anonymiser, setAnonymiser] = useState<Boolean>(false);

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
    <div className="search-container">
        <h2>Personsøk</h2>
            <div className="actions-container">
                <form onSubmit={handleSubmit}>
                    <Search
                        label="Søk etter fødselsnummer"
                        size="small"
                        variant="secondary"
                        onChange={(e) => setFødselsnummer(e)}
                    />
                </form>
                <CheckboxGroup
                    className="checbox-group"
                    legend="Anonymiser data gruppe"
                    onChange={() => setAnonymiser(!anonymiser)}
                    hideLegend
                >
                    <Checkbox value="Anonymiser data" >Anonymiser data</Checkbox>
                </CheckboxGroup>
            </div>
    </div>
  )     
}

export default Søkefelt;
