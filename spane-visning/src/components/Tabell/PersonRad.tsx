import { Table } from "@navikt/ds-react";
import React, { useState } from "react";
import { PersonDto } from "../../types";
import PseudovedtaksperiodeTabell from "./PseudovedtaksperiodeTabell";

interface Props {
  person: PersonDto;
  anonymisert: Boolean;
}

export default function PersonRad(props: Props) {
  const { person, anonymisert } = props;
  const [expand, setExpand] = useState<boolean>(false);
  return (
    <Table.ExpandableRow
      content={
        <PseudovedtaksperiodeTabell
          valgte={[]}
          fraDato={""}
          tilDato={""}
          person={person}
          anonymisert={anonymisert}
        />
      }
      togglePlacement="right"
      open={expand}
      onClick={() => setExpand(!expand)}
      style={{ cursor: "pointer", backgroundColor: "#E6F0FF" }}
    >
      <Table.DataCell scope="row">
        <b> Fødselsnummer: </b> {person.fnr ?? "ukjent fødselsnummer"}
      </Table.DataCell>
    </Table.ExpandableRow>
  );
}
