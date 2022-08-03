import {Table, Tag} from "@navikt/ds-react";
import React, {useState} from "react";
import {SubsumsjonDto} from "../../types";
import UtvidetSubsumsjonTableInnhold from "./UtvidetSubsumsjonTableInnhold";

interface Props {
    subsumsjon: SubsumsjonDto;
}

function formatDateTimeString(dateTimeString: string) {
    let dato = new Date(dateTimeString)
    let str = "";
    str += dato.getDate().toString().length === 1 ? "0" + dato.getDate() : dato.getDate()
    str += "."
    str += dato.getMonth().toString().length === 1 ? "0" + dato.getMonth() : dato.getMonth()
    str += "."
    str += dato.getFullYear()
    str += ", "
    str += dato.getHours().toString().length === 1 ? "0" + dato.getHours() : dato.getHours()
    str += ":"
    str += dato.getMinutes().toString().length === 1 ? "0" + dato.getMinutes() : dato.getMinutes()
    str += ":"
    str += dato.getSeconds().toString().length === 1 ? "0" + dato.getSeconds() : dato.getSeconds()
    str += "."
    str += dato.getMilliseconds().toString().length === 3 ? dato.getMilliseconds() : (dato.getMilliseconds().toString().length === 2 ? "0" + dato.getMilliseconds() : "00" + dato.getMilliseconds())
    return str

}

function utfallTag(str: string) {
    switch (str) {
        case "VILKAR_OPPFYLT":
            return <Tag variant={"success"}>Oppfylt</Tag>
        case "VILKAR_BEREGNET":
            return <Tag variant={"info"}>Beregnet</Tag>
        case "VILKAR_UAVKLART":
            return <Tag variant={"warning"}>Uavklart</Tag>
        case "VILKAR_IKKE_OPPFYLT":
            return <Tag variant={"error"}>Ikke oppfylt</Tag>
        default:
            return "white"
    }
}

export default function SubsumsjonExpandableRow(props: Props) {
    const {subsumsjon} = props;
    const [expand, setExpand] = useState<boolean>(false);
    return (
        <Table.ExpandableRow
            content={<UtvidetSubsumsjonTableInnhold subsumsjon={subsumsjon}/>}
            togglePlacement="right"
            open={expand}
            onClick={() => setExpand(!expand)}
            style={{cursor: "pointer"}}
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
            <Table.DataCell>{utfallTag(subsumsjon.utfall)}</Table.DataCell>
            <Table.DataCell>{formatDateTimeString(subsumsjon.tidsstempel)}</Table.DataCell>
        </Table.ExpandableRow>
    );
}
