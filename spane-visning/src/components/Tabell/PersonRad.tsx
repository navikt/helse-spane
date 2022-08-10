import { Table } from "@navikt/ds-react";
import React, { useState } from "react";
import { PersonDto } from "../../types";
import PseudovedtaksperiodeTabell from "./PseudovedtaksperiodeTabell";

interface Props {
  person: PersonDto;
  anonymisert: boolean;
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
      style={{ cursor: "pointer", backgroundColor: "#cfd8e6" }}
    >
      <Table.DataCell scope="row">
        <b> Fødselsnummer: </b> {!anonymisert ? person.fnr : "***********"}
        {/*todo av en eller annen grunn blir ting bare anonymisert her om du har søkt på noe*/}
      </Table.DataCell>
    </Table.ExpandableRow>
  );
}
