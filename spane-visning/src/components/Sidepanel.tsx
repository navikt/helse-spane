import { Checkbox, CheckboxGroup } from "@navikt/ds-react";
import React from "react";
import DatePicker from "./DatePicker";
import "./sidepanel.css";

interface Props {}

function SidePanel(props: Props) {
  const {} = props;

  return (
    <div className="sidepanel-container">
      <h3>Personnummer: </h3>
      12345678901
      <div className="date-picker-container">
        <DatePicker label="Fra" />
        <DatePicker label="Til" />
      </div>
      <div className="checkbox-group-container">
        <CheckboxGroup legend="Arbeidsgiver">
          <Checkbox>123456</Checkbox>
          <Checkbox> 98765</Checkbox>
        </CheckboxGroup>
      </div>
    </div>
  );
}

export default SidePanel;
