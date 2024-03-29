import { Table, Tooltip } from "@navikt/ds-react";
import React, { useState } from "react";
import SubsumsjonTabell from "./SubsumsjonTabell";
import { VedtaksperiodeDto } from "../../types";
import "./pseudovedtaksperioderad.css";
import { WarningColored } from "@navikt/ds-icons";
import { formatDateString } from "../../utlis";

interface Props {
  vedtaksperiode: VedtaksperiodeDto;
  anonymisert: boolean;
}

export default function PseudovedtaksperiodeRad(props: Props) {
  const { vedtaksperiode, anonymisert } = props;
  const [expand, setExpand] = useState<boolean>(false);

  function vedtakTag(str: string) {
    switch (str) {
      case "VEDTAK_FATTET":
        return (
          <div className={"vedtaksperiode-tag vedtak-fattet"}>
            <p className={"tag"}>Vedtak fattet</p>
          </div>
        );
      case "UAVKLART":
        return (
          <div className={"vedtaksperiode-tag uavklart"}>
            <p className={"tag"}>Uavklart</p>
          </div>
        );
      case "TIL_INFOTRYGD":
        return (
          <div className={"vedtaksperiode-tag til-infotrygd"}>
            <p className={"tag"}>Til infotrygd</p>
          </div>
        );
      default:
        return "white";
    }
  }

  return (
    <Table.ExpandableRow
      content={
        <SubsumsjonTabell
          subsumsjoner={vedtaksperiode.subsumsjoner}
          anonymisert={anonymisert}
        />
      }
      togglePlacement="right"
      open={expand}
      onClick={() => setExpand(!expand)}
      style={{ cursor: "pointer", backgroundColor: "#E6F0FF" }}
    >
      <Table.DataCell scope="row">
        <b className={"skjæringstidspunkt-container"}>
          {" "}
          {vedtaksperiode.tilstand === "VEDTAK_FATTET" ? (
            "Vedtaksperiode: "
          ) : vedtaksperiode.ikkeSikkertSkjæringstidspunkt ? (
            <div className={"ikke-sikkert-skjæringstidspunkt"}>
              <Tooltip content="Usikkert skjæringstidspunkt" placement="left">
                <WarningColored className={"warning"} />
              </Tooltip>
              Skjæringstidspunkt:
            </div>
          ) : (
            "Skjæringstidspunkt: "
          )}{" "}
        </b>{" "}
        {vedtaksperiode.tilstand === "VEDTAK_FATTET" &&
        vedtaksperiode.fom !== null &&
        vedtaksperiode.tom !== null ? (
          <span className="no-wrap">
            {" "}
            {formatDateString(vedtaksperiode.fom) +
              " - " +
              formatDateString(vedtaksperiode.tom)}{" "}
          </span>
        ) : (
          <span className="no-wrap">
            {" "}
            {vedtaksperiode.skjæringstidspunkt
              ? formatDateString(vedtaksperiode.skjæringstidspunkt)
              : "ukjent"}{" "}
          </span>
        )}
      </Table.DataCell>
      <Table.DataCell scope="row">
        <div className="arbeidsgiver-container">
          <b>Arbeidsgiver: </b>
          {!anonymisert ? vedtaksperiode.orgnummer : "***********"}
        </div>
      </Table.DataCell>
      <Table.DataCell scope="row">
        {" "}
        <div className={"tilstands-container"}>
          <b> Tilstand: </b>
          {vedtakTag(vedtaksperiode.tilstand)}{" "}
        </div>
      </Table.DataCell>
    </Table.ExpandableRow>
  );
}
