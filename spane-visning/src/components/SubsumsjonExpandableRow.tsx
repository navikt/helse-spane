import { Table } from "@navikt/ds-react";
import React, { useState } from "react";
import { SubsumsjonDto } from "../types";
import UtvidetSubsumsjonTableInnhold from "./UtvidetSubsumsjonTableInnhold";

interface Props {
  subsumsjon: SubsumsjonDto;
  vedtaksperiodeIdx: number;
}
export default function SubsumsjonExpandableRow(props: Props) {
  const { subsumsjon, vedtaksperiodeIdx } = props;
  const [expand, setExpand] = useState<boolean>(false);
  return (
    <Table.ExpandableRow
      content={<UtvidetSubsumsjonTableInnhold subsumsjon={subsumsjon} />}
      togglePlacement="right"
      open={expand}
      onClick={() => setExpand(!expand)}
      style={{ cursor: "pointer" }}
    >
      <Table.DataCell scope="row">{vedtaksperiodeIdx + 1}</Table.DataCell>
      <Table.DataCell scope="row">
        §{" "}
        {subsumsjon.paragraf +
          (subsumsjon.ledd ? ", " + subsumsjon.ledd + ".ledd" : "") +
          (subsumsjon.punktum ? ", " + subsumsjon.punktum + ".pkt" : "") +
          (subsumsjon.bokstav !== "null" && subsumsjon.bokstav !== null
            ? ", bokstav " + subsumsjon.bokstav
            : "")}
      </Table.DataCell>
      <Table.DataCell>{subsumsjon.utfall}</Table.DataCell>
      <Table.DataCell>{subsumsjon.tidsstempel}</Table.DataCell>
    </Table.ExpandableRow>
  );
}
