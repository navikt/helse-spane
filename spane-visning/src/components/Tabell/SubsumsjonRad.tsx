import { Table, Tag } from "@navikt/ds-react";
import React, { useState } from "react";
import { SubsumsjonDto } from "../../types";
import SubsumsjonEkspandertInnhold from "./SubsumsjonEkspandertInnhold";
import { formatDateTimeString } from "../../utlis";

interface Props {
  subsumsjon: SubsumsjonDto;
  anonymisert: boolean;
}

function utfallTag(str: string) {
  switch (str) {
    case "VILKAR_OPPFYLT":
      return <Tag variant={"success"}>Oppfylt</Tag>;
    case "VILKAR_BEREGNET":
      return <Tag variant={"info"}>Beregnet</Tag>;
    case "VILKAR_UAVKLART":
      return <Tag variant={"warning"}>Uavklart</Tag>;
    case "VILKAR_IKKE_OPPFYLT":
      return <Tag variant={"error"}>Ikke oppfylt</Tag>;
    default:
      return "white";
  }
}

export default function SubsumsjonRad(props: Props) {
  const { subsumsjon, anonymisert } = props;
  const [expand, setExpand] = useState<boolean>(false);
  return (
    <Table.ExpandableRow
      content={
        <SubsumsjonEkspandertInnhold
          subsumsjon={subsumsjon}
          anonymisert={anonymisert}
        />
      }
      togglePlacement="right"
      open={expand}
      onClick={() => setExpand(!expand)}
      style={{ cursor: "pointer" }}
    >
      <Table.DataCell scope="row">
        §{" "}
        {subsumsjon.paragraf +
          (subsumsjon.ledd ? ", " + subsumsjon.ledd + ".ledd" : "") +
          (subsumsjon.punktum ? ", " + subsumsjon.punktum + ".pkt" : "") +
          (subsumsjon.bokstav !== "null" && subsumsjon.bokstav !== null
            ? ", bokstav " + subsumsjon.bokstav
            : "")}
      </Table.DataCell>
      <Table.DataCell>
        {formatDateTimeString(subsumsjon.tidsstempel)}
      </Table.DataCell>
      <Table.DataCell>{utfallTag(subsumsjon.utfall)}</Table.DataCell>
    </Table.ExpandableRow>
  );
}
