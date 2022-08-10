import { Checkbox, CheckboxGroup } from "@navikt/ds-react";
import React from "react";
import "./arbeidsgivercheckbox.css";

interface Props {
  orgnumre: string[];
  setValgte: React.Dispatch<React.SetStateAction<string[]>>;
  anonymisert: boolean;
}

export default function ArbeidsgiverCheckbox(props: Props) {
  const { orgnumre, setValgte, anonymisert } = props;

  return (
    <div className="arbeidsgiver-checkbox-group-container">
      <CheckboxGroup
        legend="Arbeidsgiver"
        onChange={(v) => {
          setValgte(v);
        }}
      >
        {orgnumre.map((orgnummer, key) => {
          return (
            <Checkbox key={key} value={orgnummer}>
              {" "}
              {!anonymisert ? orgnummer : "***********"}
            </Checkbox>
          );
        })}
      </CheckboxGroup>
    </div>
  );
}
