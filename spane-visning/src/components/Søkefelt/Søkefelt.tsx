import { Search } from "@navikt/ds-react";
import React, {FormEvent} from "react";
import { Backend } from "../../service";
import { PersonDto } from "../../types";

interface Props {
  fødselsnummer: string
  backend: Backend
  setFødselsnummer: React.Dispatch<React.SetStateAction<string>>
  setPerson: React.Dispatch<React.SetStateAction<PersonDto | undefined>>
  setOrgnumre: React.Dispatch<React.SetStateAction<string[]>>

}


function Søkefelt(props: Props){
  
  const { fødselsnummer, backend, setFødselsnummer, setPerson, setOrgnumre } = props;

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
    <div>
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
    </div>
  )     
}

export default Søkefelt;
