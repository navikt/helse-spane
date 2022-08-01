import {Table} from "@navikt/ds-react";
import {SubsumsjonDto} from "../types";
import SubsumsjonExpandableRow from "./SubsumsjonExpandableRow";
import {useState} from "react";

interface Props {
    subsumsjoner: SubsumsjonDto[];
}

var muligeUtfall = {
    "VILKAR_OPPFYLT": 0,
    "VILKAR_BEREGNET": 1,
    "VILKAR_IKKE_OPPFYLT": 2,
    "VILKAR_UAVKLART": 3,
}


function UtvidetVedtaksperiodeTableInnhold(props: Props) {
    const {subsumsjoner} = props

    const [sorterteSubsumsjoner, setSorterteSubsumsjoner] = useState(subsumsjoner)

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
                    // TODO: Sorter videre på ledd og punktum
                   return 0
                } else return (paragraf1 > paragraf2) ? 1 : -1
            } else return (kapittel1 > kapittel2) ? 1 : -1
        }))
        }
        else if (sortKey === "utfall") {
            setSorterteSubsumsjoner([...sorterteSubsumsjoner].sort((v1, v2) => {
                    // @ts-ignore
                return ((muligeUtfall[v1.utfall]) > muligeUtfall(v2.utfall)) ? 1 : -1
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