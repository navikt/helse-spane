import React from "react";
import { ParagrafsøkDto } from "../../types";
import { Table } from "@navikt/ds-react";
import PersonRad from "./PersonRad";

interface Props {
  paragrafSøk: ParagrafsøkDto | undefined;
  anonymisert: Boolean;
}

export default function PersonTabell(props: Props) {
  const { paragrafSøk, anonymisert } = props;

  return (
    <Table size="medium">
      {paragrafSøk?.personer && (
        <Table.Body>
          <b>Personer:</b>
          {paragrafSøk.personer.map((person, personIdx) => {
            return (
              <PersonRad
                key={personIdx}
                person={person}
                anonymisert={anonymisert}
              />
            );
          })}
        </Table.Body>
      )}
    </Table>
  );
}
