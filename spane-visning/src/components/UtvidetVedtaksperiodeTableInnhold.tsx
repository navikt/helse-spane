import { Table } from "@navikt/ds-react";
import {SubsumsjonDto} from "../types";
import SubsumsjonExpandableRow from "./SubsumsjonExpandableRow";

interface Props {
    subsumsjoner: SubsumsjonDto[];
}


function UtvidetVedtaksperiodeTableInnhold(props: Props) {
    const { subsumsjoner } = props;

    return <>
        <Table size="medium">
            <Table.Header>
                <Table.Row>
                    <Table.HeaderCell />
                    <Table.HeaderCell scope="col">
                        Paragraf
                    </Table.HeaderCell>
                    <Table.HeaderCell scope="col">
                        Utfall
                    </Table.HeaderCell>
                    <Table.HeaderCell scope="col">
                        Opprettet
                    </Table.HeaderCell>
                </Table.Row>
            </Table.Header>
            <Table.Body>
                {
                    subsumsjoner.map((subsumsjon, subsumsjonIdx) => {
                        return <SubsumsjonExpandableRow key={subsumsjonIdx} subsumsjon={subsumsjon}/>
                    })
                }

            </Table.Body>
        </Table>
    </>
}

export default UtvidetVedtaksperiodeTableInnhold