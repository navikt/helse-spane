import React from "react";
import "./tidsromvelger.css";

interface Props {
  setFraDato: React.Dispatch<React.SetStateAction<string>>;
  setTilDato: React.Dispatch<React.SetStateAction<string>>;
}

export default function TidsromVelger(props: Props) {
  const { setFraDato, setTilDato } = props;

  return (
    <div className="tidsrom-container">
      <div className="tidsrom-group">
        <label htmlFor="start">{"Fra"}</label>
        <input
          type="date"
          id="start"
          name="trip-start"
          onChange={(event) => setFraDato(event.target.value)}
        ></input>
      </div>
      <div className="tidsrom-group">
        <label htmlFor="start">{"Til"}</label>
        <input
          type="date"
          id="start"
          name="trip-start"
          onChange={(event) => setTilDato(event.target.value)}
        ></input>
      </div>
    </div>
  );
}
