import {Table} from "@navikt/ds-react";
import React, {useState} from "react";
import UtvidetVedtaksperiodeTableInnhold from "./UtvidetVedtaksperiodeTableInnhold";
import {VedtaksperiodeDto} from "../../types";

interface Props {
  vedtaksperiode: VedtaksperiodeDto;
}

function VedtaksperiodeExpandableRow(props: Props) {
    const { vedtaksperiode } = props;
    const [expand, setExpand] = useState<boolean>(false);
    return (
      <Table.ExpandableRow
          content= {<UtvidetVedtaksperiodeTableInnhold subsumsjoner={vedtaksperiode.subsumsjoner}/>}
          togglePlacement="right"
          open={expand}
          onClick={() => setExpand(!expand)}
          style={{ cursor: "pointer", backgroundColor: "#E6F0FF" }}
      >
          <Table.DataCell scope="row"> <b> Skjæringstidspunkt: </b> {vedtaksperiode.skjæringstidspunkt ?? 'ukjent'}</Table.DataCell>
        <Table.DataCell scope="row"> <b> Arbeidsgiver: </b> {vedtaksperiode.orgnummer} </Table.DataCell>
        <Table.DataCell scope="row"> <b> Tilstand: </b> {vedtaksperiode.tilstand} </Table.DataCell>

      </Table.ExpandableRow>
  );
}

export default VedtaksperiodeExpandableRow;
