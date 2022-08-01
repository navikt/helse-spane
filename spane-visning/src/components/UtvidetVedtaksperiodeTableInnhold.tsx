import {Table} from "@navikt/ds-react";
import {SubsumsjonDto} from "../types";
import SubsumsjonExpandableRow from "./SubsumsjonExpandableRow";
import {useState} from "react";

interface Props {
    subsumsjoner: SubsumsjonDto[];
}

function UtvidetVedtaksperiodeTableInnhold(props: Props) {
    const {subsumsjoner} = props

    const [sorterteSubsumsjoner, setSorterteSubsumsjoner] = useState(subsumsjoner)

    function utfallStringVerdi(str: string) {
        switch (str) {
            case "VILKAR_OPPFYLT":
                return 0
            case "VILKAR_BEREGNET":
                return 1
            case "VILKAR_UAVKLART":
                return 2
            case "VILKAR_IKKE_OPPFYLT":
                return 3
            default:
                return 4
        }
    }

    function handleSort(sortKey: string | undefined) {
        if (!sortKey) return;
        if (sortKey === "paragraf") {
            setSorterteSubsumsjoner([...sorterteSubsumsjoner].sort((v1, v2) => {
                let kapittel1 = parseInt(v1.paragraf.split("-")[0])
                let kapittel2 = parseInt(v2.paragraf.split("-")[0])

                if (kapittel1 == kapittel2) {
                    let paragraf1 = parseInt(v1.paragraf.split("-")[1])
                    let paragraf2 = parseInt(v2.paragraf.split("-")[1])

                    if (paragraf1 == paragraf2) {
                        let ledd1 = v1.ledd
                        let ledd2 = v2.ledd

                        if (ledd1 == ledd2) {

                            let punktum1 = v1.punktum
                            let punktum2 = v2.punktum

                            if (punktum1 == punktum2) {

                                let bokstav1 = v1.bokstav
                                let bokstav2 = v2.bokstav

                                if (bokstav1 != "null" && bokstav2 != "null") {
                                    return (bokstav1 > bokstav2) ? 1 : -1
                                } else return 0 // Hvis en av bokstavene er tilstede er den etter den uten
                            } else return (punktum1 > punktum2) ? 1 : -1
                        } else return (ledd1 > ledd2) ? 1 : -1
                    } else return (paragraf1 > paragraf2) ? 1 : -1
                } else return (kapittel1 > kapittel2) ? 1 : -1
            }))
        } else if (sortKey === "utfall") {
            setSorterteSubsumsjoner([...sorterteSubsumsjoner].sort((v1, v2) => {
                    return utfallStringVerdi(v1.utfall) > utfallStringVerdi(v2.utfall) ? 1 : -1
                }
            ))
        }
    }


    return <>
        <Table size="medium"
               onSortChange={(sortKey => handleSort(sortKey))}
        >

            <Table.Header>
                <Table.Row>
                    <Table.HeaderCell/>
                    <Table.ColumnHeader scope="col" sortKey={"paragraf"} sortable>
                        Paragraf
                    </Table.ColumnHeader>
                    <Table.ColumnHeader scope="col" sortKey={"utfall"} sortable>
                        Utfall
                    </Table.ColumnHeader>
                    <Table.ColumnHeader scope="col">
                        Opprettet
                    </Table.ColumnHeader>
                </Table.Row>
            </Table.Header>
            <Table.Body>
                {
                    sorterteSubsumsjoner.map((subsumsjon, subsumsjonIdx) => {
                        return <SubsumsjonExpandableRow key={subsumsjonIdx} subsumsjon={subsumsjon}/>
                    })
                }

            </Table.Body>
        </Table>
    </>
}

export default UtvidetVedtaksperiodeTableInnhold