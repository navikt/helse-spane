import React from "react";
import { Table } from "@navikt/ds-react";
import { PersonDto, SubsumsjonDto, VedtaksperiodeDto } from "../types";
import UtvidetTableInnhold from "./UtvidetTableInnhold";
import SubsumsjonExpandlableRow from "./SubsumsjonExpandableRow";
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
          // (
          //   <VedtaksperiodeExpandableRow />
          // )

          {
            return vedtaksperiode.subsumsjoner.map(
              (subsumsjon: SubsumsjonDto, subsumsjonIdx: number) => {
                return (
                  <SubsumsjonExpandlableRow
                    key={subsumsjonIdx}
                    subsumsjon={subsumsjon}
                    vedtaksperiodeIdx={vedtaksperiodeIdx}
                  />
                );
              }
            );
          }
      )}
    </Table.Body>
  );
}
