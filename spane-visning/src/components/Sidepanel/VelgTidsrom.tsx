import React from "react";

interface Props {
    setFraDato: React.Dispatch<React.SetStateAction<string>>
    setTilDato: React.Dispatch<React.SetStateAction<string>>
}

function VelgTidsrom(props: Props) {
    const {setFraDato, setTilDato} = props;

    return <div className="date-picker-container">
        <div className="date-picker-group">
            <label htmlFor="start">{"Fra"}</label>
            <input type="date" id="start" name="trip-start" onChange={event => setFraDato(event.target.value)}></input>
        </div>
        <div className="date-picker-group">
            <label htmlFor="start">{"Til"}</label>
            <input type="date" id="start" name="trip-start" onChange={event => setTilDato(event.target.value)}></input>
        </div>
    </div>
}

export default VelgTidsrom;