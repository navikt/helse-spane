import React from "react";

interface Props {
  label: string;
}

function DatePicker(props: Props) {
  const { label } = props;

  return (
    <div className="date-picker-group">
      <label htmlFor="start">{label}</label>
      <input type="date" id="start" name="trip-start"></input>
    </div>
  );
}

export default DatePicker;
