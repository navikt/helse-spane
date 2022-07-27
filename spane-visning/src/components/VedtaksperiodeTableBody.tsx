import React from "react";
import { Table } from "@navikt/ds-react";
import { PersonDto, VedtaksperiodeDto } from "../types";
import VedtaksperiodeExpandableRow from "./VedtaksperiodeExpandableRow";

interface Props {
  person: PersonDto;
}

export default function VedtaksperiodeTableBody(props: Props) {
  const { person } = props;

  return (
    <Table.Body>
      {person.vedtaksperioder.map(
        (vedtaksperiode: VedtaksperiodeDto, vedtaksperiodeIdx) =>
          (
            <VedtaksperiodeExpandableRow  key={vedtaksperiodeIdx} vedtaksperiode={vedtaksperiode} />
          )
      )}
    </Table.Body>
  );
}
