import React from "react";

interface Props {
  label: string;
  setDato: React.Dispatch<React.SetStateAction<string>>;
}

function DatePicker(props: Props) {
  const { label, setDato } = props;

  return (
    <div className="date-picker-group">
      <label htmlFor="start">{label}</label>
      <input type="date" id="start" name="trip-start" onChange={event => setDato(event.target.value)}></input>
    </div>
  );
}

export default DatePicker;
