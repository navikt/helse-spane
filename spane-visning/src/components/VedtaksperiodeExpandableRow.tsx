import {Table} from "@navikt/ds-react";
import UtvidetSubsumsjonTableInnhold from "./UtvidetSubsumsjonTableInnhold";
import React, {useState} from "react";
import UtvidetVedtaksperiodeTableInnhold from "./UtvidetVedtaksperiodeTableInnhold";

function VedtaksperiodeExpandableRow() {
    const [expand, setExpand] = useState<boolean>(false);

    return (
      <Table.ExpandableRow
          content= {<UtvidetVedtaksperiodeTableInnhold/>}
          togglePlacement="right"
          open={expand}
          onClick={() => setExpand(!expand)}
          style={{ cursor: "pointer" }}
      >
        <Table.DataCell scope="row"> hei </Table.DataCell>
        <Table.DataCell scope="row">
          hei2
        </Table.DataCell>
        <Table.DataCell>hei3</Table.DataCell>
        <Table.DataCell>hei4</Table.DataCell>
      </Table.ExpandableRow>
  );;
}

export default VedtaksperiodeExpandableRow;
