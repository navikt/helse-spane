import React from "react";
import { PersonDto } from "../../types";
import { Table } from "@navikt/ds-react";
import PersonRad from "./PersonRad";

interface Props {
  personer: PersonDto[] | undefined;
  anonymisert: Boolean;
}

export default function PersonTabell(props: Props) {
  const { personer, anonymisert } = props;

  return (
    <Table size="medium">
      {personer && (
        <Table.Body>
          {personer.map((person, personIdx) => {
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
