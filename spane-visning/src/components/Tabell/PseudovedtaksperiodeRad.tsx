import {HelpText, Table, Tag, Tooltip} from "@navikt/ds-react";
import React, {useState} from "react";
import SubsumsjonTabell from "./SubsumsjonTabell";
import {VedtaksperiodeDto} from "../../types";
import "./pseudovedtaksperioderad.css";
import {ErrorColored, SuccessColored, WarningColored} from "@navikt/ds-icons";


interface Props {
    vedtaksperiode: VedtaksperiodeDto;
    anonymisert: Boolean;
}

export default function PseudovedtaksperiodeRad(props: Props) {
    const {vedtaksperiode, anonymisert} = props;
    const [expand, setExpand] = useState<boolean>(false);

    function vedtakTag(str: string) {
        switch (str) {
            case "VEDTAK_FATTET":
                return <div className={"tilstands-lable-container vedtak-fattet"}><p className={"lable"}>Vedtak
                    fattet</p></div>;
            case "UAVKLART":
                return <div className={"tilstands-lable-container uavklart"}><p className={"lable"}>Uavklart</p></div>;
            case "TIL_INFOTRYGD":
                return <div className={"tilstands-lable-container til-infotrygd"}><p className={"lable"}>Til
                    infotrygd</p></div>;
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
            style={{cursor: "pointer", backgroundColor: "#E6F0FF"}}
        >
            <Table.DataCell scope="row">
                <b className={"ikke-sikkert-skjæringstidspunkt-container"}>
                    {" "}
                    {vedtaksperiode.tilstand === 'VEDTAK_FATTET' ? "Vedtaksperiode: " :
                        (vedtaksperiode.ikkeSikkertSkjæringstidspunkt ?
                            <div className={"ikke-sikkert-skjæringstidspunkt"}>
                                <Tooltip
                                    content="Usikkert skjæringstidspunkt"
                                    placement="left"
                                >
                                <WarningColored className={"warning"}/>
                                </Tooltip>

                                    Skjæringstidspunkt:
                            </div> : "Skjæringstidspunkt: ")} {" "}
                </b>{" "}
                {vedtaksperiode.tilstand === 'VEDTAK_FATTET' && vedtaksperiode.fom !== null && vedtaksperiode.tom !== null
                    ? (vedtaksperiode.fom + ' - ' + vedtaksperiode.tom) : (
                        vedtaksperiode.skjæringstidspunkt ?? "ukjent"
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
